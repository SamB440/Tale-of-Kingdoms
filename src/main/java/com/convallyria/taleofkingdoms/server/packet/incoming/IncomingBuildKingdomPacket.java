package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.guild.CityBuilderEntity;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.kingdom.poi.KingdomPOI;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.BuildKingdomPacket;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.convallyria.taleofkingdoms.server.world.ServerConquestInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public final class IncomingBuildKingdomPacket extends InServerPacketHandler<BuildKingdomPacket> {

    public IncomingBuildKingdomPacket() {
        super(Packets.BUILD_KINGDOM, BuildKingdomPacket.CODEC);
    }

    @Override
    public void handleIncomingPacket(PacketContext context, BuildKingdomPacket packet) {
        ServerPlayer player = (ServerPlayer) context.player();
        final int entityId = packet.entityId();
        context.taskQueue().execute(() -> {
            final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
            api.getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                final Entity entity = player.level().getEntity(entityId);
                if (!(entity instanceof CityBuilderEntity cityBuilderEntity) || player.distanceTo(cityBuilderEntity) > 5) {
                    reject(player, "Invalid entity ID / Distance");
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

                final Vec3 centre = instance.getCentre();
                boolean isWithin = player.blockPosition().closerThan(new Vec3i((int) centre.x(), (int) centre.y(), (int) centre.z()), 500);
                if (isWithin) {
                    reject(player, "Too close to guild");
                    return;
                }

                BlockPos pos = player.blockPosition().subtract(new Vec3i(0, 25, 85));
                final PlayerKingdom playerKingdom = new PlayerKingdom(pos);
                guildPlayer.setKingdom(playerKingdom);

                // Paste their kingdom
                TaleOfKingdoms.getAPI().getSchematicHandler().pasteSchematic(Schematic.TIER_1_KINGDOM, player, pos).thenAccept(box -> {
                    BlockPos start = new BlockPos(box.maxX(), box.maxY(), box.maxZ());
                    BlockPos end = new BlockPos(box.minX(), box.minY(), box.minZ());
                    playerKingdom.setStart(start);
                    playerKingdom.setEnd(end);

                    // Make city builder stop following player and move to well POI
                    cityBuilderEntity.stopFollowingPlayer();
                    // Teleport to the player first, should avoid getting stuck in ground
                    cityBuilderEntity.teleportTo(player.getX(), player.getY(), player.getZ());
                    // Now move to the well location
                    cityBuilderEntity.setTarget(playerKingdom.getPOIPos(KingdomPOI.CITY_BUILDER_WELL_POI));

                    ServerConquestInstance.sync(player, instance);
                });
            });
        });
    }
}
