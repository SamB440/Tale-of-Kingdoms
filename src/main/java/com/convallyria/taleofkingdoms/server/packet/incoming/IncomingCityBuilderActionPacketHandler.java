package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.guild.CityBuilderEntity;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.kingdom.builds.BuildCosts;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.action.CityBuilderAction;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class IncomingCityBuilderActionPacketHandler extends ServerPacketHandler {

    public IncomingCityBuilderActionPacketHandler() {
        super(Packets.FOREMAN_COLLECT);
    }

    @Override
    public void handleIncomingPacket(PacketContext context, PacketByteBuf attachedData) {
        ServerPlayerEntity player = (ServerPlayerEntity) context.player();
        int entityId = attachedData.readInt();
        CityBuilderAction action = attachedData.readEnumConstant(CityBuilderAction.class);
        BuildCosts buildCosts;
        if (action == CityBuilderAction.BUILD && attachedData.isReadable()) {
            buildCosts = attachedData.readEnumConstant(BuildCosts.class);
        } else {
            buildCosts = null;
        }

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
                    if (buildCosts == null) {
                        reject(player, "Invalid build cost");
                        return;
                    }

                    cityBuilderEntity.build(player, buildCosts, kingdom);
                }
            }
        }));
    }

    @Override
    public void handleOutgoingPacket(@NotNull PlayerEntity player, @Nullable Object... data) {
        throw new IllegalArgumentException("Not supported");
    }
}
