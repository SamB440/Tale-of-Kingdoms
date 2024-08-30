package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.guild.CityBuilderEntity;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.kingdom.builds.BuildCosts;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.action.CityBuilderAction;
import com.convallyria.taleofkingdoms.common.packet.c2s.CityBuilderActionPacket;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.convallyria.taleofkingdoms.server.world.ServerConquestInstance;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public final class IncomingCityBuilderActionPacketHandler extends InServerPacketHandler<CityBuilderActionPacket> {

    public IncomingCityBuilderActionPacketHandler() {
        super(Packets.CITYBUILDER_ACTION, CityBuilderActionPacket.CODEC);
    }

    @Override
    public void handleIncomingPacket(PacketContext context, CityBuilderActionPacket packet) {
        final ServerPlayerEntity player = (ServerPlayerEntity) context.player();
        final int entityId = packet.entityId();
        final CityBuilderAction action = packet.action();
        final Optional<BuildCosts> buildCosts = packet.costs();
        context.taskQueue().execute(() -> TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            final Entity entityById = player.getWorld().getEntityById(entityId);
            if (!(entityById instanceof CityBuilderEntity cityBuilderEntity) || entityById.distanceTo(player) > 5) {
                reject(player, "Invalid entity ID / Distance");
                return;
            }

            if (action == null) {
                reject(player, "Invalid action");
                return;
            }

            final GuildPlayer guildPlayer = instance.getPlayer(player.getUuid());
            if (guildPlayer == null) {
                reject(player, "No player");
                return;
            }

            final PlayerKingdom kingdom = guildPlayer.getKingdom();
            if (kingdom == null) {
                reject(player, "No kingdom");
                return;
            }

            switch (action) {
                case GIVE_64_WOOD -> cityBuilderEntity.give64wood(player);
                case GIVE_64_STONE -> cityBuilderEntity.give64stone(player);
                case FIX_KINGDOM -> cityBuilderEntity.fixKingdom(player, kingdom);
                case BUILD -> {
                    if (buildCosts.isEmpty()) {
                        reject(player, "Invalid build cost");
                        return;
                    }

                    if (cityBuilderEntity.getWood() < buildCosts.get().getWood() || cityBuilderEntity.getStone() < buildCosts.get().getStone()) {
                        reject(player, "Not enough resources");
                        return;
                    }

                    if (kingdom.getTier() != buildCosts.get().getTier()) {
                        reject(player, "Invalid build for tier");
                        return;
                    }

                    cityBuilderEntity.build(player, buildCosts.get(), kingdom).thenAccept((v) -> ServerConquestInstance.sync(player, instance));
                }
            }
        }));
    }
}
