package com.convallyria.taleofkingdoms.common.listener;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.event.EntitySpawnCallback;
import com.convallyria.taleofkingdoms.common.item.ItemHelper;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;

import java.util.Optional;

public class MobSpawnListener extends Listener {

    public MobSpawnListener() {
        EntitySpawnCallback.EVENT.register(entity -> {
            if (ItemHelper.isHostileEntity(entity)) {
                Optional<ConquestInstance> instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance();
                return !instance.isPresent() || !instance.get().isInGuild(entity);
            }
            return true;
        });
    }
}
