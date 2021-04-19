package com.convallyria.taleofkingdoms.client;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.entity.render.RenderSetup;
import com.convallyria.taleofkingdoms.client.gui.RenderListener;
import com.convallyria.taleofkingdoms.client.gui.shop.ScreenSellItem;
import com.convallyria.taleofkingdoms.client.packet.ClientPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.both.BothSignContractPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.incoming.IncomingInstanceSyncPacketHandler;
import com.convallyria.taleofkingdoms.common.listener.GameInstanceListener;
import com.convallyria.taleofkingdoms.common.listener.StartWorldListener;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(EnvType.CLIENT)
public class TaleOfKingdomsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        new RenderSetup(TaleOfKingdoms.getAPI().get().getMod());
        registerPacketHandlers();
        registerEvents();
        ScreenRegistry.register(TaleOfKingdoms.SELL_SCREEN_HANDLER, ScreenSellItem::new);
    }

    private void registerPacketHandlers() {
        registerHandler(new IncomingInstanceSyncPacketHandler());
        registerHandler(new BothSignContractPacketHandler());
    }

    private void registerListeners() {
        new GameInstanceListener();
    }

    protected void registerHandler(ClientPacketHandler clientPacketHandler) {
        TaleOfKingdoms.getAPI().ifPresent(api -> api.registerClientHandler(clientPacketHandler));
    }

    private void registerEvents() {
        TaleOfKingdoms.LOGGER.info("Registering client events...");
        new RenderListener();
        new StartWorldListener();
    }
}
