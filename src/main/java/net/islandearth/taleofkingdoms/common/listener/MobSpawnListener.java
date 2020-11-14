package net.islandearth.taleofkingdoms.common.listener;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.event.EntitySpawnCallback;
import net.islandearth.taleofkingdoms.common.item.ItemHelper;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;

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
