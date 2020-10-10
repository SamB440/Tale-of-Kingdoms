package net.islandearth.taleofkingdoms.common.listener;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;

public abstract class Listener {

    public Listener() {
        TaleOfKingdoms.LOGGER.info("Registering listeners for: " + this.getClass().getName());
    }

}
