package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.guild.BankerEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.banker.BankerMethod;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.world.ServerConquestInstance;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public final class IncomingBankerInteractPacketHandler extends ServerPacketHandler {

    public IncomingBankerInteractPacketHandler() {
        super(TaleOfKingdoms.BANKER_INTERACT_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(Identifier identifier, PacketContext context, PacketByteBuf attachedData) {
        ServerPlayerEntity player = (ServerPlayerEntity) context;
        UUID uuid = player.getUuid();
        String playerContext = " @ <" + player.getName().asString() + ":" + player.getIp() + ">";
        BankerMethod method = attachedData.readEnumConstant(BankerMethod.class);
        int coins = attachedData.readInt();
        context.taskQueue().execute(() -> {
            TaleOfKingdoms.getAPI().flatMap(api -> api.getConquestInstanceStorage().mostRecentInstance()).ifPresent(inst -> {
                ServerConquestInstance instance = (ServerConquestInstance) inst;
                if (!instance.isInGuild(player)) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Not in guild.");
                    return;
                }

                // Search for banker
                Optional<BankerEntity> entity = instance.getGuildEntity(player.world, EntityTypes.BANKER);
                if (entity.isEmpty()) {
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
                instance.sync(player);
            });
        });
    }

    @Override
    public void handleOutgoingPacket(Identifier identifier, @NotNull PlayerEntity player, @Nullable Object... data) {
        throw new IllegalArgumentException("Not supported");
    }
}
