package com.convallyria.taleofkingdoms.common.entity.kingdom.workers;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class QuarryWorkerEntity extends WorkerEntity {

    public QuarryWorkerEntity(@NotNull EntityType<? extends PathAwareEntity> entityType, @NotNull World world) {
        super(entityType, world);
        this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.IRON_PICKAXE));
    }
}
