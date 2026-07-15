package com.convallyria.taleofkingdoms.common.entity.kingdom.workers;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class LumberWorkerEntity extends WorkerEntity {

    public LumberWorkerEntity(@NotNull EntityType<? extends PathfinderMob> entityType, @NotNull Level world) {
        super(entityType, world);
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_AXE));
    }
}
