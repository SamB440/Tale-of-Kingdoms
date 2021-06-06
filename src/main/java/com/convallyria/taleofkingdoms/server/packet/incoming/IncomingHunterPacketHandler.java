package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.generic.HunterEntity;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import com.convallyria.taleofkingdoms.common.world.ServerConquestInstance;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public final class IncomingHunterPacketHandler extends ServerPacketHandler {

    public IncomingHunterPacketHandler() {
        super(TaleOfKingdoms.HUNTER_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(Identifier identifier, PacketContext context, PacketByteBuf attachedData) {
        ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
        UUID uuid = player.getUuid();
        String playerContext = " @ <" + player.getName().asString() + ":" + player.getIp() + ">";
        boolean retire = attachedData.readBoolean();
        context.getTaskQueue().execute(() -> {
            TaleOfKingdoms.getAPI().flatMap(api -> api.getConquestInstanceStorage().mostRecentInstance()).ifPresent(inst -> {
                ServerConquestInstance instance = (ServerConquestInstance) inst;
                if (!instance.isInGuild(player)) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Not in guild.");
                    return;
                }

                // Search for banker
                Optional<? extends Entity> entity = instance.getGuildEntity(player.world, EntityTypes.GUILDMASTER);
                if (entity.isEmpty()) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Guildmaster entity not present in guild.");
                    return;
                }

                if (instance.getCoins(uuid) == 0 && instance.getBankerCoins(uuid) == 0) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": No coins.");
                    return;
                }

                if (retire) {
                    if (!instance.getHunterUUIDs().containsKey(uuid) || instance.getHunterUUIDs().get(uuid).isEmpty()) {
                        TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": No hunters to retire.");
                        return;
                    }

                    HunterEntity hunterEntity = (HunterEntity) player.getServerWorld().getEntity(instance.getHunterUUIDs().get(uuid).get(0));
                    if (hunterEntity == null) {
                        TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Hunter entity returned null.");
                        return;
                    }

                    hunterEntity.kill();
                    instance.removeHunter(uuid, hunterEntity.getUuid());
                    instance.setCoins(uuid, instance.getCoins(uuid) + 750);
                    instance.sync(player, null);
                    return;
                }

                if (instance.getCoins(uuid) < 1500) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Not enough coins.");
                    return;
                }

                HunterEntity hunterEntity = EntityUtils.spawnEntity(EntityTypes.HUNTER, player, entity.get().getBlockPos());
                instance.addHunter(uuid, hunterEntity);
                instance.setCoins(uuid, instance.getCoins(uuid) - 1500);
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
