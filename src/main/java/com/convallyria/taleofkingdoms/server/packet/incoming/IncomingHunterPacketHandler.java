package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.generic.HunterEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildMasterEntity;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.convallyria.taleofkingdoms.server.world.ServerConquestInstance;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public final class IncomingHunterPacketHandler extends ServerPacketHandler {

    public IncomingHunterPacketHandler() {
        super(Packets.HUNTER_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(Identifier identifier, PacketContext context, PacketByteBuf attachedData) {
        ServerPlayerEntity player = (ServerPlayerEntity) context.player();
        UUID uuid = player.getUuid();
        boolean retire = attachedData.readBoolean();
        context.taskQueue().execute(() -> TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            final GuildPlayer guildPlayer = instance.getPlayer(player);
            if (!instance.isInGuild(player)) {
                reject(player, "Not in guild.");
                return;
            }

            // Search for banker
            Optional<GuildMasterEntity> entity = instance.getGuildEntity(player.world, EntityTypes.GUILDMASTER);
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

                HunterEntity hunterEntity = (HunterEntity) player.getWorld().getEntity(guildPlayer.getHunters().get(0));
                //TODO we need to match client logic here
                if (hunterEntity == null) {
                    reject(player, "Hunter entity returned null.");
                    return;
                }

                hunterEntity.kill();
                guildPlayer.getHunters().remove(hunterEntity.getUuid());
                guildPlayer.setCoins(guildPlayer.getCoins() + 750);
                ServerConquestInstance.sync(player, instance);
                return;
            }

            if (guildPlayer.getCoins() < 1500) {
                reject(player, "Not enough coins.");
                return;
            }

            HunterEntity hunterEntity = EntityUtils.spawnEntity(EntityTypes.HUNTER, player, entity.get().getBlockPos());
            guildPlayer.getHunters().add(hunterEntity.getUuid());
            guildPlayer.setCoins(guildPlayer.getCoins() - 1500);
            ServerConquestInstance.sync(player, instance);
        }));
    }

    @Override
    public void handleOutgoingPacket(Identifier identifier, @NotNull PlayerEntity player, @Nullable Object... data) {
        throw new IllegalArgumentException("Not supported");
    }
}
