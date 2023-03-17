package com.convallyria.taleofkingdoms.client.gui.entity.shop.widget;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.ShopEntity;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public interface ShopScreenInterface {

    ShopItem getSelectedItem();

    void setSelectedItem(ShopItem selectedItem);

    default void openSellGui(ShopEntity entity, PlayerEntity player) {
        /*
         * WHY is this what we need to do for a proper sell GUI?
         * I HATE THIS!!!!
         * someone please rewrite it so blocks are not needed
         */
        BlockPos pos = entity.getBlockPos().add(0, 2, 0);
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        if (MinecraftClient.getInstance().getServer() == null) {
            api.getClientHandler(TaleOfKingdoms.TOGGLE_SELL_GUI_PACKET_ID)
                    .handleOutgoingPacket(TaleOfKingdoms.TOGGLE_SELL_GUI_PACKET_ID,
                            player, false, entity.getGUIType());
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
    }
}