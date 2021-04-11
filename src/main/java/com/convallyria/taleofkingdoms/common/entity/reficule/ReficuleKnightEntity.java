package com.convallyria.taleofkingdoms.common.entity.reficule;

import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.ImprovedFollowTargetGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.TeleportTowardsPlayerGoal;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
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

public class ReficuleKnightEntity extends TOKEntity implements Monster, TeleportAbility {

    public ReficuleKnightEntity(@NotNull EntityType<? extends PathAwareEntity> entityType, @NotNull World world) {
        super(entityType, world);
        ItemStack ironSword = new ItemStack(Items.IRON_SWORD);
        ironSword.addEnchantment(Enchantments.MENDING, 1); // Want them to look fancy :)
        this.setStackInHand(Hand.MAIN_HAND, ironSword);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.targetSelector.add(1, new ImprovedFollowTargetGoal<>(this, EntityType.PLAYER, true));
        this.targetSelector.add(2, new ImprovedFollowTargetGoal<>(this, EntityTypes.GUILDGUARD, true));
        this.targetSelector.add(3, new TeleportTowardsPlayerGoal(this, entity -> {
            return entity.squaredDistanceTo(this) < this.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
        }));
        this.goalSelector.add(2, new WanderAroundGoal(this, 0.6D));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 0.8D, false));
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() { // Slightly higher stats than guild guards.
        return TOKEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0D) // Big increase! Needs balancing?
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 9.0D)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.5D);
    }

    @Override
    public boolean teleportTo(Entity entity) {
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        return this.teleport(x, y, z, true);
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public boolean isStationary() {
        return false;
    }
}
