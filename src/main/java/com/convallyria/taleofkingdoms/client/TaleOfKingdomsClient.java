package com.convallyria.taleofkingdoms.client;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.entity.render.RenderSetup;
import com.convallyria.taleofkingdoms.client.gui.RenderListener;
import com.convallyria.taleofkingdoms.client.packet.incoming.IncomingInstanceSyncPacketHandler;
import com.convallyria.taleofkingdoms.common.listener.StartWorldListener;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class TaleOfKingdomsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        new RenderSetup(TaleOfKingdoms.getAPI().get().getMod());
        registerPacketHandlers();
        registerEvents();
    }

    private void registerPacketHandlers() {
        new IncomingInstanceSyncPacketHandler();
    }

    private void registerEvents() {
        TaleOfKingdoms.LOGGER.info("Registering client events...");
        new RenderListener();
        new StartWorldListener();
    }
}
