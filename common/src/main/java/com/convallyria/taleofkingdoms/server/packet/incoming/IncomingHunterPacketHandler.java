package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.generic.HunterEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildMasterEntity;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.HireHunterPacket;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.convallyria.taleofkingdoms.server.TaleOfKingdomsServer;
import com.convallyria.taleofkingdoms.server.world.ServerConquestInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import java.util.Optional;

@Environment(EnvType.SERVER)
public final class IncomingHunterPacketHandler extends InServerPacketHandler<HireHunterPacket> {

    public IncomingHunterPacketHandler() {
        super(Packets.HIRE_HUNTER, HireHunterPacket.CODEC);
    }

    @Override
    public void handleIncomingPacket(PacketContext context, HireHunterPacket packet) {
        ServerPlayer player = (ServerPlayer) context.player();
        boolean retire = packet.retire();
        context.taskQueue().execute(() -> TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            final GuildPlayer guildPlayer = instance.getPlayer(player);
            if (!instance.isInGuild(player)) {
                reject(player, "Not in guild.");
                return;
            }

            // Search for banker
            Optional<GuildMasterEntity> entity = instance.getGuildEntity(player.level(), EntityTypes.GUILDMASTER.get());
            if (entity.isEmpty()) {
                reject(player, "Guildmaster entity not present in guild.");
                return;
            }

            if (guildPlayer.getCoins() == 0 && guildPlayer.getBankerCoins() == 0) {
                reject(player, "No coins.");
                return;
            }

            if (retire) {
                if (guildPlayer.getHunters().isEmpty()) {
                    reject(player, "No hunters to retire.");
                    return;
                }

                HunterEntity hunterEntity = (HunterEntity) player.serverLevel().getEntity(guildPlayer.getHunters().get(0));
                //TODO we need to match client logic here
                if (hunterEntity == null) {
                    reject(player, "Hunter entity returned null.");
                    return;
                }

                TaleOfKingdomsServer.getAPI().executeOnDedicatedServer(() -> hunterEntity.remove(Entity.RemovalReason.DISCARDED));
                guildPlayer.getHunters().remove(hunterEntity.getUUID());
                guildPlayer.setCoins(guildPlayer.getCoins() + 750);
                ServerConquestInstance.sync(player, instance);
                return;
            }

            if (guildPlayer.getCoins() < 1500) {
                reject(player, "Not enough coins.");
                return;
            }

            HunterEntity hunterEntity = EntityUtils.spawnEntity(EntityTypes.HUNTER.get(), player, entity.get().blockPosition());
            guildPlayer.getHunters().add(hunterEntity.getUUID());
            guildPlayer.setCoins(guildPlayer.getCoins() - 1500);
            ServerConquestInstance.sync(player, instance);
        }));
    }
}
