package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.generic.HunterEntity;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
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

public final class IncomingHunterPacketHandler extends ServerPacketHandler {

    public IncomingHunterPacketHandler() {
        super(TaleOfKingdoms.HUNTER_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(ResourceLocation identifier, PacketContext context, FriendlyByteBuf attachedData) {
        ServerPlayer player = (ServerPlayer) context.player();
        UUID uuid = player.getUUID();
        String playerContext = " @ <" + player.getName().getContents() + ":" + player.getIpAddress() + ">";
        boolean retire = attachedData.readBoolean();
        context.taskQueue().execute(() -> {
            TaleOfKingdoms.getAPI().flatMap(api -> api.getConquestInstanceStorage().mostRecentInstance()).ifPresent(inst -> {
                ServerConquestInstance instance = (ServerConquestInstance) inst;
                if (!instance.isInGuild(player)) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Not in guild.");
                    return;
                }

                // Search for banker
                Optional<? extends Entity> entity = instance.getGuildEntity(player.level, EntityTypes.GUILDMASTER);
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

                    HunterEntity hunterEntity = (HunterEntity) player.getLevel().getEntity(instance.getHunterUUIDs().get(uuid).get(0));
                    if (hunterEntity == null) {
                        TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Hunter entity returned null.");
                        return;
                    }

                    hunterEntity.kill();
                    instance.removeHunter(uuid, hunterEntity.getUUID());
                    instance.setCoins(uuid, instance.getCoins(uuid) + 750);
                    instance.sync(player, null);
                    return;
                }

                if (instance.getCoins(uuid) < 1500) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Not enough coins.");
                    return;
                }

                HunterEntity hunterEntity = EntityUtils.spawnEntity(EntityTypes.HUNTER, player, entity.get().blockPosition());
                instance.addHunter(uuid, hunterEntity);
                instance.setCoins(uuid, instance.getCoins(uuid) - 1500);
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
