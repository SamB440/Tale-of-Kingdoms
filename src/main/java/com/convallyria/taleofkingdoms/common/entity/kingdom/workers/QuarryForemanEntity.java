package com.convallyria.taleofkingdoms.common.entity.kingdom.workers;

import com.convallyria.taleofkingdoms.common.entity.kingdom.ForemanEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class QuarryForemanEntity extends ForemanEntity {

    public QuarryForemanEntity(EntityType<? extends PathfinderMob> entityType, Level world) {
        super(entityType, world);
    }
}
