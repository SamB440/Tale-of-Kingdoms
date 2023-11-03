package com.convallyria.taleofkingdoms.common.entity.reficule;

import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.BowAttackGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.ImprovedFollowTargetGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.TeleportTowardsPlayerGoal;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class ReficuleGuardianEntity extends TOKEntity implements Monster, TeleportAbility, RangedAttackMob {

    public ReficuleGuardianEntity(@NotNull EntityType<? extends PathAwareEntity> entityType, @NotNull World world) {
        super(entityType, world);
        ItemStack bow = new ItemStack(Items.BOW);
        bow.addEnchantment(Enchantments.POWER, 1); // Want them to look fancy :)
        this.setStackInHand(Hand.MAIN_HAND, bow);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.targetSelector.add(1, new TeleportTowardsPlayerGoal(this, entity -> {
            return entity.squaredDistanceTo(this) < this.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
        }));
        this.targetSelector.add(2, new ImprovedFollowTargetGoal<>(this, EntityType.PLAYER, true));
        this.targetSelector.add(3, new ImprovedFollowTargetGoal<>(this, EntityTypes.GUILDGUARD, true));
        this.targetSelector.add(4, new ImprovedFollowTargetGoal<>(this, EntityTypes.GUILDARCHER, true));
        this.targetSelector.add(5, new ImprovedFollowTargetGoal<>(this, EntityTypes.GUILDVILLAGER, false));
        this.targetSelector.add(6, new ImprovedFollowTargetGoal<>(this, EntityTypes.HUNTER, true));
        this.targetSelector.add(7, new ImprovedFollowTargetGoal<>(this, EntityTypes.BANDIT, true));
        this.goalSelector.add(1, new BowAttackGoal<>(this, 0.6D, 20, 16.0F));
        this.goalSelector.add(2, new WanderAroundGoal(this, 0.6D));
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return TOKEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0D)
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
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean isStationary() {
        return false;
    }

    @Override
    public void shootAt(LivingEntity target, float pullProgress) {
        ItemStack itemStack = this.getProjectileType(this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW)));
        PersistentProjectileEntity persistentProjectileEntity = this.createArrowProjectile(itemStack, pullProgress);
        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.3333333333333333D) - persistentProjectileEntity.getY();
        double g = target.getZ() - this.getZ();
        double h = Math.sqrt(d * d + g * g);
        persistentProjectileEntity.setVelocity(d, e + h * 0.20000000298023224D, g, 1.6F, (float)(14 - this.getWorld().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.getWorld().spawnEntity(persistentProjectileEntity);
    }

    protected PersistentProjectileEntity createArrowProjectile(ItemStack arrow, float damageModifier) {
        return ProjectileUtil.createArrowProjectile(this, arrow, damageModifier);
    }

    @Override
    public boolean canUseRangedWeapon(RangedWeaponItem weapon) {
        return weapon == Items.BOW;
    }
}
