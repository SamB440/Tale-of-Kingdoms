package com.convallyria.taleofkingdoms.common.listener;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.event.WorldDeleteCallback;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;

import java.io.File;

public class DeleteWorldListener extends Listener {

    public DeleteWorldListener() {
        WorldDeleteCallback.EVENT.register(worldName -> {
            final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
            if (api == null) {
                TaleOfKingdoms.LOGGER.warn("Unable to delete world as api is null.");
                return;
            }

            File file = new File(api.getDataFolder() + "worlds/" + worldName + ConquestInstance.FILE_TYPE);
            if (!file.delete() && file.exists()) {
                TaleOfKingdoms.LOGGER.error("Unable to delete " + worldName + ConquestInstance.FILE_TYPE + " file");
            }
        });
    }
}
