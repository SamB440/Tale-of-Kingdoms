package com.convallyria.taleofkingdoms.common.entity.bandit;

import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.ImprovedFollowTargetGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class BanditEntity extends TOKEntity implements Monster {

    public BanditEntity(@NotNull EntityType<? extends PathAwareEntity> entityType, @NotNull World world) {
        super(entityType, world);
        ItemStack ironSword = new ItemStack(Items.IRON_SWORD);
        this.setStackInHand(Hand.MAIN_HAND, ironSword);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.targetSelector.add(1, new ImprovedFollowTargetGoal<>(this, EntityType.PLAYER, true));
        this.targetSelector.add(2, new ImprovedFollowTargetGoal<>(this, EntityTypes.GUILDGUARD, true));
        this.targetSelector.add(3, new ImprovedFollowTargetGoal<>(this, EntityTypes.GUILDARCHER, true));
        this.targetSelector.add(4, new ImprovedFollowTargetGoal<>(this, EntityTypes.HUNTER, true));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 0.8D, false));
        this.goalSelector.add(2, new WanderAroundGoal(this, 0.6D));
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return TOKEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 30.0D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 24.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4D)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0D);
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean isStationary() {
        return false;
    }
}
