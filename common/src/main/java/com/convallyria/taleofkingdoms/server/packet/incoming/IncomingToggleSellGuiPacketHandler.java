package com.convallyria.taleofkingdoms.server.packet.incoming;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.ShopEntity;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.ToggleSellGuiPacket;
import com.convallyria.taleofkingdoms.common.packet.context.PacketContext;
import com.convallyria.taleofkingdoms.common.shop.ShopParser;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class IncomingToggleSellGuiPacketHandler extends InServerPacketHandler<ToggleSellGuiPacket> {

    public IncomingToggleSellGuiPacketHandler() {
        super(Packets.TOGGLE_SELL_GUI, ToggleSellGuiPacket.CODEC);
    }

    @Override
    public void handleIncomingPacket(PacketContext context, ToggleSellGuiPacket packet) {
        ServerPlayer player = (ServerPlayer) context.player();
        boolean close = packet.close();
        ShopParser.GUI type = packet.guiType();
        context.taskQueue().execute(() -> {
            final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
            api.getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                if (!instance.isInGuild(player)) {
                    reject(player, "Not in guild.");
                    return;
                }

                // Search for either foodshop, itemshop, or blacksmith in the guild
                Optional<? extends ShopEntity> entity = Optional.empty();
                switch (type) {
                    case BLACKSMITH -> entity = instance.search(player, player.level(), EntityTypes.BLACKSMITH.get());
                    case FOOD -> entity = instance.search(player, player.level(), EntityTypes.FOODSHOP.get());
                    case ITEM -> entity = instance.search(player, player.level(), EntityTypes.ITEM_SHOP.get());
                }

                if (entity.isEmpty()) {
                    reject(player, "Shop entity not present in guild.");
                    return;
                }

                BlockPos pos = entity.get().blockPosition().offset(0, 2, 0);
                api.getScheduler().queue(server -> {
                    if (close) {
                        server.overworld().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                        return;
                    }

                    ServerPlayer serverPlayer = server.getPlayerList().getPlayer(player.getUUID());
                    server.overworld().setBlockAndUpdate(pos, TaleOfKingdoms.SELL_BLOCK.get().defaultBlockState());
                    BlockState state = server.overworld().getBlockState(pos);
                    MenuProvider screenHandlerFactory = state.getMenuProvider(server.overworld(), pos);
                    if (screenHandlerFactory != null) {
                        //With this call the server will request the client to open the appropriate Screenhandler
                        serverPlayer.openMenu(screenHandlerFactory);
                    }
                }, 1);
            });
        });
    }
}
