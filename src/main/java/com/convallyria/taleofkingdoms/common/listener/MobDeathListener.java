package com.convallyria.taleofkingdoms.common.listener;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.event.EntityDeathCallback;

public class MobDeathListener extends Listener {

    public MobDeathListener() {
        EntityDeathCallback.EVENT.register((source, entity) -> {
            TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                if (instance.getReficuleAttackers().contains(entity.getUuid())) {
                    instance.getReficuleAttackers().remove(entity.getUuid());
                    TaleOfKingdoms.LOGGER.debug("Reficule attacker died with UUID " + entity.getUuid());
                }
            });
        });
    }
}
