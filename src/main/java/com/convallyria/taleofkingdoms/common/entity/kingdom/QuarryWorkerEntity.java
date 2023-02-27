package com.convallyria.taleofkingdoms.common.entity.kingdom;

import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.GatherResourcesPassivelyGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.WanderAroundKingdomGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class QuarryWorkerEntity extends TOKEntity {

    public QuarryWorkerEntity(@NotNull EntityType<? extends PathAwareEntity> entityType, @NotNull World world) {
        super(entityType, world);
        this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.IRON_PICKAXE));
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 5.0F, 60F));
        this.goalSelector.add(2, new GatherResourcesPassivelyGoal(this));
        this.goalSelector.add(3, new WanderAroundKingdomGoal(this, 0.6D, 50, 5, 3));
        this.goalSelector.add(4, new LookAroundGoal(this));
    }

    @Override
    public boolean isStationary() {
        return false;
    }
}
