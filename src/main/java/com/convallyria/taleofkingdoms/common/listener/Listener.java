package com.convallyria.taleofkingdoms.common.listener;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;

public abstract class Listener {

    public Listener() {
        TaleOfKingdoms.LOGGER.info("Registering listeners for: " + this.getClass().getName());
    }

}
