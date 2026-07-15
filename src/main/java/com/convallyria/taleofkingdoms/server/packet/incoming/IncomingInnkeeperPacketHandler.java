package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.guild.InnkeeperEntity;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.InnkeeperActionPacket;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.utils.BlockUtils;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.convallyria.taleofkingdoms.server.TaleOfKingdomsServer;
import com.convallyria.taleofkingdoms.server.world.ServerConquestInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import java.util.Optional;

@Environment(EnvType.SERVER)
public final class IncomingInnkeeperPacketHandler extends InServerPacketHandler<InnkeeperActionPacket> {

    public IncomingInnkeeperPacketHandler() {
        super(Packets.INNKEEPER_HIRE_ROOM, InnkeeperActionPacket.CODEC);
    }

    @Override
    public void handleIncomingPacket(PacketContext context, InnkeeperActionPacket packet) {
        ServerPlayer player = (ServerPlayer) context.player();
        boolean resting = packet.resting();
        context.taskQueue().execute(() -> TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            if (!instance.isInGuild(player)) {
                reject(player, "Not in guild.");
                return;
            }

            // Search for innkeeper
            Optional<InnkeeperEntity> entity = instance.getGuildEntity(player.level(), EntityTypes.INNKEEPER);
            if (entity.isEmpty()) {
                reject(player, "Innkeeper entity not present in guild.");
                return;
            }

            final GuildPlayer guildPlayer = instance.getPlayer(player);
            if (guildPlayer.getCoins() == 0 && guildPlayer.getBankerCoins() == 0) {
                reject(player, "No coins.");
                return;
            }

            int coins = 10;
            if (guildPlayer.getCoins() < coins) {
                reject(player, "Not enough coins.");
                return;
            }

            guildPlayer.setCoins(guildPlayer.getCoins() - 10);

            if (resting) {
                BlockPos rest = BlockUtils.locateRestingPlace(instance, player);
                if (rest == null) {
                    reject(player, "No rooms available.");
                    return;
                }

                TaleOfKingdomsServer.getAPI().executeOnDedicatedServer(() -> {
                    MinecraftServer server = player.getServer();
                    server.overworld().setDayTime(1000);
                    player.teleportTo(rest.getX() + 0.5, rest.getY(), rest.getZ() + 0.5);
                    player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 1));
                    player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
                });
                return;
            }

            TaleOfKingdomsServer.getAPI().getServer().overworld().setDayTime(13000);
            ServerConquestInstance.sync(player, instance);
        }));
    }
}
