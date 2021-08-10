package com.convallyria.taleofkingdoms.client.gui.entity.widget;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public interface ShopScreenInterface {

    ShopItem getSelectedItem();

    void setSelectedItem(ShopItem selectedItem);

    default void openSellGui(TOKEntity entity, Player player) {
        /*
         * WHY is this what we need to do for a proper sell GUI?
         * I HATE THIS!!!!
         * someone please rewrite it so blocks are not needed
         */
        BlockPos pos = entity.blockPosition().offset(0, 2, 0);
        TaleOfKingdoms.getAPI().ifPresent(api -> {
            if (Minecraft.getInstance().getSingleplayerServer() == null) {
                api.getClientHandler(TaleOfKingdoms.TOGGLE_SELL_GUI_PACKET_ID)
                        .handleOutgoingPacket(TaleOfKingdoms.TOGGLE_SELL_GUI_PACKET_ID,
                                player,
                                null, false);
                return;
            }

            api.getScheduler().queue(server -> {
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
    }
}