package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.world.ServerConquestInstance;
import com.convallyria.taleofkingdoms.server.packet.ServerPacketHandler;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class IncomingToggleSellGuiPacketHandler extends ServerPacketHandler {

    public IncomingToggleSellGuiPacketHandler() {
        super(TaleOfKingdoms.TOGGLE_SELL_GUI_PACKET_ID);
    }

    @Override
    public void handleIncomingPacket(Identifier identifier, PacketContext context, PacketByteBuf attachedData) {
        ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
        String playerContext = " @ <" + player.getName().asString() + ":" + player.getIp() + ">";
        boolean close = attachedData.readBoolean();
        context.getTaskQueue().execute(() -> {
            TaleOfKingdoms.getAPI().ifPresent(api -> {
                api.getConquestInstanceStorage().mostRecentInstance().ifPresent(inst -> {
                    ServerConquestInstance instance = (ServerConquestInstance) inst;
                    if (!instance.isInGuild(player)) {
                        TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Not in guild.");
                        return;
                    }

                    // Search for either foodshop or blacksmith in the guild
                    Optional<? extends Entity> entity = instance.getGuildEntity(player.world, EntityTypes.BLACKSMITH);
                    if (!entity.isPresent()) entity = instance.getGuildEntity(player.world, EntityTypes.FOODSHOP);
                    if (!entity.isPresent()) {
                        TaleOfKingdoms.LOGGER.info("Rejected " + identifier.toString() + playerContext + ": Shop entity not present in guild.");
                        return;
                    }

                    BlockPos pos = entity.get().getBlockPos().add(0, 2, 0);
                    if (close) {

                        return;
                    }
                    api.getScheduler().queue(server -> {
                        ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(player.getUuid());
                        server.getOverworld().setBlockState(pos, TaleOfKingdoms.SELL_BLOCK.getDefaultState());
                        BlockState state = server.getOverworld().getBlockState(pos);
                        NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(server.getOverworld(), pos);
                        if (screenHandlerFactory != null) {
                            //With this call the server will request the client to open the appropriate Screenhandler
                            serverPlayer.openHandledScreen(screenHandlerFactory);
                        }
                    }, 1);
                });
            });
        });
    }

    @Override
    public void handleOutgoingPacket(Identifier identifier, @NotNull PlayerEntity player,
                                     @Nullable ClientConnection connection, @Nullable Object... data) {
        throw new IllegalArgumentException("Not supported");
    }
}
