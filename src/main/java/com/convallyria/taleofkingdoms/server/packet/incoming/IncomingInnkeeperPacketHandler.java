package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.utils.BlockUtils;
import com.convallyria.taleofkingdoms.common.world.ServerConquestInstance;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public final class IncomingInnkeeperPacketHandler extends ServerPacketHandler {

    public IncomingInnkeeperPacketHandler() {
        super(TaleOfKingdoms.INNKEEPER_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(ResourceLocation identifier, PacketContext context, FriendlyByteBuf attachedData) {
        ServerPlayer player = (ServerPlayer) context.player();
        UUID uuid = player.getUUID();
        String playerContext = " @ <" + player.getName().getContents() + ":" + player.getIpAddress() + ">";
        boolean resting = attachedData.readBoolean();
        context.taskQueue().execute(() -> {
            TaleOfKingdoms.getAPI().flatMap(api -> api.getConquestInstanceStorage().mostRecentInstance()).ifPresent(inst -> {
                ServerConquestInstance instance = (ServerConquestInstance) inst;
                if (!instance.isInGuild(player)) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Not in guild.");
                    return;
                }

                // Search for innkeeper
                Optional<? extends Entity> entity = instance.getGuildEntity(player.level, EntityTypes.INNKEEPER);
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

                instance.setCoins(player.getUUID(), instance.getCoins(player.getUUID()) - 10);

                if (resting) {
                    BlockPos rest = BlockUtils.locateRestingPlace(instance, player);
                    if (rest == null) {
                        TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": No rooms available.");
                        return;
                    }

                    TaleOfKingdoms.getAPI().ifPresent(api -> api.executeOnDedicatedServer(() -> {
                       MinecraftServer server = player.getServer();
                        server.overworld().setDayTime(1000);
                        player.moveTo(rest.getX() + 0.5, rest.getY(), rest.getZ() + 0.5);
                        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 1));
                        player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
                    }));
                    return;
                }

                TaleOfKingdoms.getAPI().flatMap(TaleOfKingdomsAPI::getServer).ifPresent(server -> server.overworld().setDayTime(13000));
                instance.sync(player, null);
            });
        });
    }

    @Override
    public void handleOutgoingPacket(ResourceLocation identifier, @NotNull Player player,
                                     @Nullable Connection connection, @Nullable Object... data) {
        throw new IllegalArgumentException("Not supported");
    }
}
