package com.convallyria.taleofkingdoms.common.entity.kingdom.warden;

import com.convallyria.taleofkingdoms.common.entity.States;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.BowAttackGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.CrossbowAttackGoal;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class ArcherHireableEntity extends WardenHireable implements CrossbowAttackMob, RangedAttackMob, States {

    private boolean charging;
    private boolean ticked;

    public ArcherHireableEntity(@NotNull EntityType<? extends PathfinderMob> entityType, @NotNull Level world) {
        super(entityType, world);
    }

    @Environment(EnvType.CLIENT)
    public boolean isCharging() {
        return charging;
    }

    @Override
    public void setChargingCrossbow(boolean charging) {
        this.charging = charging;
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    @Override
    public States.State getState() {
        if (this.isCharging()) {
            return States.State.CROSSBOW_CHARGE;
        } else if (this.isHolding(Items.CROSSBOW)) {
            return States.State.CROSSBOW_HOLD;
        } else {
            return this.isAggressive() ? States.State.ATTACKING : States.State.NEUTRAL;
        }
    }

    public ArcherLevel getLevel() {
        ArcherLevel highest = ArcherLevel.ARCHER;
        for (ArcherLevel warriorLevel : ArcherLevel.values()) {
            if (getExperiencePoints() >= warriorLevel.getLevelRequired() && warriorLevel.getLevelRequired() > highest.getLevelRequired()) {
                highest = warriorLevel;
            }
        }
        return highest;
    }

    private ArcherLevel current = ArcherLevel.ARCHER;

    @Override
    protected boolean tryLevelUp() {
        final ArcherLevel newLevel = getLevel();
        final boolean changed = current != newLevel;
        current = newLevel;
        return changed;
    }

    @Override
    public Optional<ResourceLocation> getSkin() {
        final ArcherLevel level = getLevel();
        return Optional.of(
            switch (level) {
                case ARCHER -> identifier("textures/entity/updated_textures/archer_base.png");
                case RANGER -> identifier("textures/entity/updated_textures/archer_ranger.png");
            }
        );
    }

    @Override
    public Component getFollowText() {
        return Component.translatable("entity_type.taleofkingdoms.archer.follow", StringUtils.capitalize(getLevel().name().toLowerCase(Locale.ROOT)));
    }

    @Override
    public Component getGuardText() {
        return Component.translatable("entity_type.taleofkingdoms.archer.guard", StringUtils.capitalize(getLevel().name().toLowerCase(Locale.ROOT)));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData) {
        SpawnGroupData entityReturnData = super.finalizeSpawn(world, difficulty, spawnReason, entityData);
        int value = ThreadLocalRandom.current().nextInt(2);
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(value == 1 ? Items.BOW : Items.CROSSBOW));
        return entityReturnData;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.ticked) return;
        this.ticked = true;
        // initGoals seems to be too early, soooo...
        if (this.getItemInHand(InteractionHand.MAIN_HAND).getItem() == Items.BOW) {
            this.goalSelector.addGoal(1, new BowAttackGoal<>(this, 0.7D, 10, 15.0F * (this.getLevel().ordinal() + 1)));
        } else {
            this.goalSelector.addGoal(1, new CrossbowAttackGoal<>(this, 0.7D, 15.0F * (this.getLevel().ordinal() + 1)));
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float pullProgress) {
        final ItemStack shotFrom = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW));
        ItemStack itemStack = this.getProjectile(shotFrom);
        AbstractArrow persistentProjectileEntity = this.createArrowProjectile(itemStack, pullProgress, shotFrom);
        double d = target.getX() - this.getX();
        double e = target.getY(0.3333333333333333D) - persistentProjectileEntity.getY();
        double g = target.getZ() - this.getZ();
        double h = Math.sqrt(d * d + g * g);
        persistentProjectileEntity.setBaseDamage(persistentProjectileEntity.getBaseDamage() + ((this.getLevel().ordinal() + 1) * 2));
        persistentProjectileEntity.shoot(d, e + h * 0.2f, g, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(persistentProjectileEntity);
    }

    protected AbstractArrow createArrowProjectile(ItemStack arrow, float damageModifier, ItemStack shotFrom) {
        return ProjectileUtil.getMobArrow(this, arrow, damageModifier, shotFrom.isEmpty() ? null : shotFrom);
    }

    @Override
    public boolean canFireProjectileWeapon(ProjectileWeaponItem weapon) {
        return weapon == Items.BOW || weapon == Items.CROSSBOW;
    }

    @Override
    public void updateLevelledAttributes() {
        final AttributeInstance attackDamage = this.getAttribute(Attributes.ATTACK_DAMAGE);
        attackDamage.setBaseValue(7f + (2f * getLevel().ordinal()));
        this.current = getLevel();
    }

    @Override
    protected Component getTypeName() {
        return Component.translatable("entity.taleofkingdoms." + getLevel().name().toLowerCase(Locale.ROOT));
    }

    public enum ArcherLevel {
        ARCHER(0),
        RANGER(5);

        private final int levelRequired;

        ArcherLevel(int levelRequired) {
            this.levelRequired = levelRequired;
        }

        public int getLevelRequired() {
            return levelRequired;
        }
    }
}
