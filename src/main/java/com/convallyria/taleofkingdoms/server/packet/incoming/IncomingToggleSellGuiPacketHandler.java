package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.world.ServerConquestInstance;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class IncomingToggleSellGuiPacketHandler extends ServerPacketHandler {

    public IncomingToggleSellGuiPacketHandler() {
        super(TaleOfKingdoms.TOGGLE_SELL_GUI_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(ResourceLocation identifier, PacketContext context, FriendlyByteBuf attachedData) {
        ServerPlayer player = (ServerPlayer) context.player();
        String playerContext = " @ <" + player.getName().getContents() + ":" + player.getIpAddress() + ">";
        boolean close = attachedData.readBoolean();
        context.taskQueue().execute(() -> {
            TaleOfKingdoms.getAPI().ifPresent(api -> {
                api.getConquestInstanceStorage().mostRecentInstance().ifPresent(inst -> {
                    ServerConquestInstance instance = (ServerConquestInstance) inst;
                    if (!instance.isInGuild(player)) {
                        TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Not in guild.");
                        return;
                    }

                    // Search for either foodshop or blacksmith in the guild
                    Optional<? extends Entity> entity = instance.getGuildEntity(player.level, EntityTypes.BLACKSMITH);
                    if (entity.isEmpty()) entity = instance.getGuildEntity(player.level, EntityTypes.FOODSHOP);
                    if (entity.isEmpty()) {
                        TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Shop entity not present in guild.");
                        return;
                    }

                    BlockPos pos = entity.get().blockPosition().offset(0, 2, 0);
                    api.getScheduler().queue(server -> {
                        if (close) {
                            server.overworld().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                            return;
                        }

                        ServerPlayer serverPlayer = server.getPlayerList().getPlayer(player.getUUID());
                        server.overworld().setBlockAndUpdate(pos, TaleOfKingdoms.SELL_BLOCK.defaultBlockState());
                        BlockState state = server.overworld().getBlockState(pos);
                        MenuProvider screenHandlerFactory = state.getMenuProvider(server.overworld(), pos);
                        if (screenHandlerFactory != null) {
                            //With this call the server will request the client to open the appropriate Screenhandler
                            serverPlayer.openMenu(screenHandlerFactory);
                        }
                    }, 1);
                });
            });
        });
    }

    @Override
    public void handleOutgoingPacket(ResourceLocation identifier, @NotNull Player player,
                                     @Nullable Connection connection, @Nullable Object... data) {
        throw new IllegalArgumentException("Not supported");
    }
}
