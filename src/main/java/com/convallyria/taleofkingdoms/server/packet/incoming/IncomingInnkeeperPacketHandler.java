package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.guild.InnkeeperEntity;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.utils.BlockUtils;
import com.convallyria.taleofkingdoms.common.world.ServerConquestInstance;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public final class IncomingInnkeeperPacketHandler extends ServerPacketHandler {

    public IncomingInnkeeperPacketHandler() {
        super(TaleOfKingdoms.INNKEEPER_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(Identifier identifier, PacketContext context, PacketByteBuf attachedData) {
        ServerPlayerEntity player = (ServerPlayerEntity) context.player();
        UUID uuid = player.getUuid();
        String playerContext = " @ <" + player.getName().asString() + ":" + player.getIp() + ">";
        boolean resting = attachedData.readBoolean();
        context.taskQueue().execute(() -> {
            TaleOfKingdoms.getAPI().flatMap(api -> api.getConquestInstanceStorage().mostRecentInstance()).ifPresent(inst -> {
                ServerConquestInstance instance = (ServerConquestInstance) inst;
                if (!instance.isInGuild(player)) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Not in guild.");
                    return;
                }

                // Search for innkeeper
                Optional<InnkeeperEntity> entity = instance.getGuildEntity(player.world, EntityTypes.INNKEEPER);
                if (entity.isEmpty()) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Innkeeper entity not present in guild.");
                    return;
                }

                if (instance.getCoins(uuid) == 0 && instance.getBankerCoins(uuid) == 0) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": No coins.");
                    return;
                }

                int coins = 10;
                if (instance.getCoins(uuid) < coins) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Not enough coins.");
                    return;
                }

                instance.setCoins(player.getUuid(), instance.getCoins(player.getUuid()) - 10);

                if (resting) {
                    BlockPos rest = BlockUtils.locateRestingPlace(instance, player);
                    if (rest == null) {
                        TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": No rooms available.");
                        return;
                    }

                    TaleOfKingdoms.getAPI().ifPresent(api -> api.executeOnDedicatedServer(() -> {
                       MinecraftServer server = player.getServer();
                        server.getOverworld().setTimeOfDay(1000);
                        player.refreshPositionAfterTeleport(rest.getX() + 0.5, rest.getY(), rest.getZ() + 0.5);
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 1));
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0));
                    }));
                    return;
                }

                TaleOfKingdoms.getAPI().flatMap(TaleOfKingdomsAPI::getServer).ifPresent(server -> server.getOverworld().setTimeOfDay(13000));
                instance.sync(player, null);
            });
        });
    }

    @Override
    public void handleOutgoingPacket(Identifier identifier, @NotNull PlayerEntity player,
                                     @Nullable ClientConnection connection, @Nullable Object... data) {
        throw new IllegalArgumentException("Not supported");
    }
}
