package net.islandearth.taleofkingdoms.common.listener;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.TaleOfKingdomsAPI;
import net.islandearth.taleofkingdoms.common.event.WorldDeleteCallback;

import java.io.File;

public class DeleteWorldListener extends Listener {

    public DeleteWorldListener() {
        WorldDeleteCallback.EVENT.register(worldName -> {
            File file = new File(TaleOfKingdoms.getAPI().map(TaleOfKingdomsAPI::getDataFolder).orElseThrow(() -> new IllegalArgumentException("API not present")) + "worlds/" + worldName + ".conquestworld");
            if (!file.delete()) {
                TaleOfKingdoms.LOGGER.error("Unable to delete " + worldName + ".conquestworld file");
            }
        });
    }
}
