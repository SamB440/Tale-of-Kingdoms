package com.convallyria.taleofkingdoms.common.entity.guild;

import com.convallyria.taleofkingdoms.common.entity.ai.goal.WanderAroundGuildGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class GuildVillagerEntity extends GuildGuardEntity {

    public GuildVillagerEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new WanderAroundGuildGoal(this, 0.6D));
        this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 30.0F));
        this.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
    }

    @Override
    public boolean isStationary() {
        return false;
    }
}
