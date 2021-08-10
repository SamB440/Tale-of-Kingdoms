package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.guild.banker.BankerMethod;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.world.ServerConquestInstance;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public final class IncomingBankerInteractPacketHandler extends ServerPacketHandler {

    public IncomingBankerInteractPacketHandler() {
        super(TaleOfKingdoms.BANKER_INTERACT_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(ResourceLocation identifier, PacketContext context, FriendlyByteBuf attachedData) {
        ServerPlayer player = (ServerPlayer) context;
        UUID uuid = player.getUUID();
        String playerContext = " @ <" + player.getName().getContents() + ":" + player.getIpAddress() + ">";
        BankerMethod method = attachedData.readEnum(BankerMethod.class);
        int coins = attachedData.readInt();
        context.taskQueue().execute(() -> {
            TaleOfKingdoms.getAPI().flatMap(api -> api.getConquestInstanceStorage().mostRecentInstance()).ifPresent(inst -> {
                ServerConquestInstance instance = (ServerConquestInstance) inst;
                if (!instance.isInGuild(player)) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Not in guild.");
                    return;
                }

                // Search for banker
                Optional<? extends Entity> entity = instance.getGuildEntity(player.level, EntityTypes.BANKER);
                if (!entity.isPresent()) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Banker entity not present in guild.");
                    return;
                }

                if (instance.getCoins(uuid) == 0 && instance.getBankerCoins(uuid) == 0) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": No coins.");
                    return;
                }

                if (method == BankerMethod.DEPOSIT) {
                    if (instance.getCoins(uuid) < coins) {
                        TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Not enough coins.");
                        return;
                    }
                    instance.setCoins(uuid, instance.getCoins(uuid) - coins);
                    instance.setBankerCoins(uuid, instance.getBankerCoins(uuid) + coins);
                } else {
                    if (instance.getBankerCoins(uuid) < coins) {
                        TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Not enough coins.");
                        return;
                    }
                    instance.setBankerCoins(uuid, instance.getBankerCoins(uuid) - coins);
                    instance.addCoins(uuid, coins);
                }
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
