package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.guild.CityBuilderEntity;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.kingdom.poi.KingdomPOI;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import com.convallyria.taleofkingdoms.server.world.ServerConquestInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class IncomingBuildKingdomPacket extends ServerPacketHandler {

    public IncomingBuildKingdomPacket() {
        super(Packets.BUILD_KINGDOM_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(Identifier identifier, PacketContext context, PacketByteBuf attachedData) {
        ServerPlayerEntity player = (ServerPlayerEntity) context.player();

        context.taskQueue().execute(() -> {
            final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
            api.getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                final int entityId = attachedData.readInt();
                final Entity entity = player.world.getEntityById(entityId);
                if (!(entity instanceof CityBuilderEntity cityBuilderEntity)) {
                    reject(player, "Invalid entity ID");
                    return;
                }

                final GuildPlayer guildPlayer = instance.getPlayer(player);
                if (guildPlayer.getKingdom() != null) {
                    reject(player, "Kingdom already built");
                    return;
                }

                if (guildPlayer.getWorthiness() < 1500) {
                    reject(player, "Not enough worthiness");
                    return;
                }

                final Vec3d centre = instance.getCentre();
                boolean isWithin = player.getBlockPos().isWithinDistance(new Vec3i((int) centre.getX(), (int) centre.getY(), (int) centre.getZ()), 3000);
//                if (!isWithin) {
//                    reject(player, "Too close to guild");
//                    return;
//                }

                BlockPos pos = player.getBlockPos().subtract(new Vec3i(0, 25, 85));
                final PlayerKingdom playerKingdom = new PlayerKingdom(pos);
                guildPlayer.setKingdom(playerKingdom);

                // Paste their kingdom
                TaleOfKingdoms.getAPI().getSchematicHandler().pasteSchematic(Schematic.TIER_1_KINGDOM, player, pos).thenAccept(box -> {
                    BlockPos start = new BlockPos(box.getMaxX(), box.getMaxY(), box.getMaxZ());
                    BlockPos end = new BlockPos(box.getMinX(), box.getMinY(), box.getMinZ());
                    playerKingdom.setStart(start);
                    playerKingdom.setEnd(end);

                    // Make city builder stop following player and move to well POI
                    cityBuilderEntity.stopFollowingPlayer();
                    // Teleport to the player first, should avoid getting stuck in ground
                    cityBuilderEntity.refreshPositionAfterTeleport(player.getX(), player.getY(), player.getZ());
                    // Now move to the well location
                    cityBuilderEntity.setTarget(playerKingdom.getPOIPos(KingdomPOI.CITY_BUILDER_WELL_POI));
                });
                ServerConquestInstance.sync(player, instance);
            });
        });
    }



    @Override
    public void handleOutgoingPacket(Identifier identifier, @NotNull PlayerEntity player, @Nullable Object... data) {
        throw new IllegalArgumentException("Not supported");
    }
}
