package com.convallyria.taleofkingdoms.common.entity.reficule;

import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.ImprovedFollowTargetGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.TeleportTowardsPlayerGoal;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ReficuleSoldierEntity extends TOKEntity implements Enemy, TeleportAbility {

    public ReficuleSoldierEntity(@NotNull EntityType<? extends PathfinderMob> entityType, @NotNull Level world) {
        super(entityType, world);
        ItemStack ironSword = new ItemStack(Items.IRON_SWORD);
        ironSword.enchant(Enchantments.MENDING, 1); // Want them to look fancy :)
        this.setItemInHand(InteractionHand.MAIN_HAND, ironSword);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new TeleportTowardsPlayerGoal(this, entity -> {
            return entity.distanceToSqr(this) < this.getAttributeValue(Attributes.FOLLOW_RANGE);
        }));
        this.targetSelector.addGoal(2, new ImprovedFollowTargetGoal<>(this, EntityType.PLAYER, true));
        this.targetSelector.addGoal(3, new ImprovedFollowTargetGoal<>(this, EntityTypes.GUILDGUARD, true));
        this.targetSelector.addGoal(4, new ImprovedFollowTargetGoal<>(this, EntityTypes.GUILDARCHER, true));
        this.targetSelector.addGoal(5, new ImprovedFollowTargetGoal<>(this, EntityTypes.HUNTER, true));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 0.8D, false));
    }

    public static AttributeSupplier.Builder createMobAttributes() { // Slightly higher stats than guild guards.
        return TOKEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.MAX_HEALTH, 40.0D) // Big increase! Needs balancing?
                .add(Attributes.ATTACK_DAMAGE, 7.5D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D);
    }

    @Override
    public boolean teleportTo(Entity entity) {
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        return this.randomTeleport(x, y, z, true);
    }

    @Override
    public boolean spreadFire() {
        return true;
    }

    @Override
    public boolean fireImmune() {
        return true;
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
