package net.islandearth.taleofkingdoms.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.client.gui.RenderListener;
import net.islandearth.taleofkingdoms.common.entity.render.RenderSetup;
import net.islandearth.taleofkingdoms.common.listener.StartWorldListener;

@Environment(EnvType.CLIENT)
public class TaleOfKingdomsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        new RenderSetup(TaleOfKingdoms.getAPI().get().getMod());
        registerEvents();
    }

    private void registerEvents() {
        TaleOfKingdoms.LOGGER.info("Registering client events...");
        new RenderListener();
        new StartWorldListener();
    }
}
