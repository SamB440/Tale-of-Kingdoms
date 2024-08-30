package com.convallyria.taleofkingdoms.common.entity.kingdom.warden;

import com.convallyria.taleofkingdoms.common.entity.States;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.BowAttackGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.CrossbowAttackGoal;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class ArcherHireableEntity extends WardenHireable implements CrossbowUser, RangedAttackMob, States {

    private boolean charging;
    private boolean ticked;

    public ArcherHireableEntity(@NotNull EntityType<? extends PathAwareEntity> entityType, @NotNull World world) {
        super(entityType, world);
    }

    @Environment(EnvType.CLIENT)
    public boolean isCharging() {
        return charging;
    }

    @Override
    public void setCharging(boolean charging) {
        this.charging = charging;
    }

    @Override
    public void postShoot() {
        this.despawnCounter = 0;
    }

    @Override
    public States.State getState() {
        if (this.isCharging()) {
            return States.State.CROSSBOW_CHARGE;
        } else if (this.isHolding(Items.CROSSBOW)) {
            return States.State.CROSSBOW_HOLD;
        } else {
            return this.isAttacking() ? States.State.ATTACKING : States.State.NEUTRAL;
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
    public Optional<Identifier> getSkin() {
        final ArcherLevel level = getLevel();
        return Optional.of(
            switch (level) {
                case ARCHER -> identifier("textures/entity/updated_textures/archer_base.png");
                case RANGER -> identifier("textures/entity/updated_textures/archer_ranger.png");
            }
        );
    }

    @Override
    public Text getFollowText() {
        return Text.translatable("entity_type.taleofkingdoms.archer.follow", StringUtils.capitalize(getLevel().name().toLowerCase(Locale.ROOT)));
    }

    @Override
    public Text getGuardText() {
        return Text.translatable("entity_type.taleofkingdoms.archer.guard", StringUtils.capitalize(getLevel().name().toLowerCase(Locale.ROOT)));
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        EntityData entityReturnData = super.initialize(world, difficulty, spawnReason, entityData);
        int value = ThreadLocalRandom.current().nextInt(2);
        this.setStackInHand(Hand.MAIN_HAND, new ItemStack(value == 1 ? Items.BOW : Items.CROSSBOW));
        return entityReturnData;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.ticked) return;
        this.ticked = true;
        // initGoals seems to be too early, soooo...
        if (this.getStackInHand(Hand.MAIN_HAND).getItem() == Items.BOW) {
            this.goalSelector.add(1, new BowAttackGoal<>(this, 0.7D, 10, 15.0F * (this.getLevel().ordinal() + 1)));
        } else {
            this.goalSelector.add(1, new CrossbowAttackGoal<>(this, 0.7D, 15.0F * (this.getLevel().ordinal() + 1)));
        }
    }

    @Override
    public void shootAt(LivingEntity target, float pullProgress) {
        final ItemStack shotFrom = this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW));
        ItemStack itemStack = this.getProjectileType(shotFrom);
        PersistentProjectileEntity persistentProjectileEntity = this.createArrowProjectile(itemStack, pullProgress, shotFrom);
        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.3333333333333333D) - persistentProjectileEntity.getY();
        double g = target.getZ() - this.getZ();
        double h = Math.sqrt(d * d + g * g);
        persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() + ((this.getLevel().ordinal() + 1) * 2));
        persistentProjectileEntity.setVelocity(d, e + h * 0.2f, g, 1.6F, (float)(14 - this.getWorld().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.getWorld().spawnEntity(persistentProjectileEntity);
    }

    protected PersistentProjectileEntity createArrowProjectile(ItemStack arrow, float damageModifier, ItemStack shotFrom) {
        return ProjectileUtil.createArrowProjectile(this, arrow, damageModifier, shotFrom.isEmpty() ? null : shotFrom);
    }

    @Override
    public boolean canUseRangedWeapon(RangedWeaponItem weapon) {
        return weapon == Items.BOW || weapon == Items.CROSSBOW;
    }

    @Override
    public void updateLevelledAttributes() {
        final EntityAttributeInstance attackDamage = this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        attackDamage.setBaseValue(7f + (2f * getLevel().ordinal()));
        this.current = getLevel();
    }

    @Override
    protected Text getDefaultName() {
        return Text.translatable("entity.taleofkingdoms." + getLevel().name().toLowerCase(Locale.ROOT));
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
