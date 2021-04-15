package com.convallyria.taleofkingdoms.common.listener;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.event.EntitySpawnCallback;
import com.convallyria.taleofkingdoms.common.item.ItemHelper;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EntityType;

import java.util.Optional;

public class MobSpawnListener extends Listener {

    private static final ImmutableList<EntityType<?>> BLACKLIST = ImmutableList.of(
            EntityTypes.REFICULE_MAGE,
            EntityTypes.REFICULE_GUARDIAN,
            EntityTypes.REFICULE_SOLDIER);

    public MobSpawnListener() {
        EntitySpawnCallback.EVENT.register(entity -> {
            if (!BLACKLIST.contains(entity.getType()) && ItemHelper.isHostileEntity(entity)) {
                Optional<ConquestInstance> instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance();
                return !instance.isPresent() || !instance.get().isInGuild(entity);
            }
            return true;
        });
    }
}
