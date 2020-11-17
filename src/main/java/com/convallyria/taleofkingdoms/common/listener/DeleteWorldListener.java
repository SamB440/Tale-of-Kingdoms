package com.convallyria.taleofkingdoms.common.listener;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.event.WorldDeleteCallback;

import java.io.File;

public class DeleteWorldListener extends Listener {

    public DeleteWorldListener() {
        WorldDeleteCallback.EVENT.register(worldName -> {
            File file = new File(TaleOfKingdoms.getAPI().map(TaleOfKingdomsAPI::getDataFolder).orElseThrow(() -> new IllegalArgumentException("API not present")) + "worlds/" + worldName + ".conquestworld");
            if (!file.delete() && file.exists()) {
                TaleOfKingdoms.LOGGER.error("Unable to delete " + worldName + ".conquestworld file");
            }
        });
    }
}
