package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.generic.HunterEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildMasterEntity;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
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
        super(TaleOfKingdoms.HUNTER_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(Identifier identifier, PacketContext context, PacketByteBuf attachedData) {
        ServerPlayerEntity player = (ServerPlayerEntity) context.player();
        UUID uuid = player.getUuid();
        String playerContext = identifier.toString() + " @ <" + player.getName().getString() + ":" + player.getIp() + ">";
        boolean retire = attachedData.readBoolean();
        context.taskQueue().execute(() -> {
            TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(inst -> {
                ServerConquestInstance instance = (ServerConquestInstance) inst;
                if (!instance.isInGuild(player)) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + playerContext + ": Not in guild.");
                    return;
                }

                // Search for banker
                Optional<GuildMasterEntity> entity = instance.getGuildEntity(player.world, EntityTypes.GUILDMASTER);
                if (entity.isEmpty()) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + playerContext + ": Guildmaster entity not present in guild.");
                    return;
                }

                if (instance.getCoins(uuid) == 0 && instance.getBankerCoins(uuid) == 0) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + playerContext + ": No coins.");
                    return;
                }

                if (retire) {
                    if (!instance.getHunterUUIDs().containsKey(uuid) || instance.getHunterUUIDs().get(uuid).isEmpty()) {
                        TaleOfKingdoms.LOGGER.info("Rejected " + playerContext + ": No hunters to retire.");
                        return;
                    }

                    HunterEntity hunterEntity = (HunterEntity) player.getWorld().getEntity(instance.getHunterUUIDs().get(uuid).get(0));
                    //TODO we need to match client logic here
                    if (hunterEntity == null) {
                        TaleOfKingdoms.LOGGER.info("Rejected " + playerContext + ": Hunter entity returned null.");
                        return;
                    }

                    hunterEntity.kill();
                    instance.removeHunter(uuid, hunterEntity.getUuid());
                    instance.setCoins(uuid, instance.getCoins(uuid) + 750);
                    instance.sync(player);
                    return;
                }

                if (instance.getCoins(uuid) < 1500) {
                    TaleOfKingdoms.LOGGER.info("Rejected " + playerContext + ": Not enough coins.");
                    return;
                }

                HunterEntity hunterEntity = EntityUtils.spawnEntity(EntityTypes.HUNTER, player, entity.get().getBlockPos());
                instance.addHunter(uuid, hunterEntity);
                instance.setCoins(uuid, instance.getCoins(uuid) - 1500);
                instance.sync(player);
            });
        });
    }

    @Override
    public void handleOutgoingPacket(Identifier identifier, @NotNull PlayerEntity player, @Nullable Object... data) {
        throw new IllegalArgumentException("Not supported");
    }
}
