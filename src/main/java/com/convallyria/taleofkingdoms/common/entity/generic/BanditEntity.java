package com.convallyria.taleofkingdoms.common.entity.generic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.States;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.BowAttackGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.CrossbowAttackGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.ImprovedFollowTargetGoal;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

public class BanditEntity extends TOKEntity implements CrossbowUser, RangedAttackMob, States, Monster {

    public static final Identifier[] SKINS = new Identifier[]{
            new Identifier(TaleOfKingdoms.MODID, "textures/entity/bandit/archer_tok.png"),
            new Identifier(TaleOfKingdoms.MODID, "textures/entity/bandit/bandit_ruiner.png"),
            new Identifier(TaleOfKingdoms.MODID, "textures/entity/bandit/brigand_chief_2.png"),
            new Identifier(TaleOfKingdoms.MODID, "textures/entity/bandit/custom_adventurer_norska_warrior_1.png"),
            new Identifier(TaleOfKingdoms.MODID, "textures/entity/bandit/custom_adventurer_stealthy_thief.png"),
            new Identifier(TaleOfKingdoms.MODID, "textures/entity/bandit/custom_outlaw_merry_man_1.png"),
            new Identifier(TaleOfKingdoms.MODID, "textures/entity/bandit/custom_outlaw_merry_man_2.png"),
            new Identifier(TaleOfKingdoms.MODID, "textures/entity/bandit/custom_outlaw_merry_man_3.png"),
            new Identifier(TaleOfKingdoms.MODID, "textures/entity/bandit/tok_bandit_hood_1.png"),
            new Identifier(TaleOfKingdoms.MODID, "textures/entity/bandit/tok_brigand.png"),
            new Identifier(TaleOfKingdoms.MODID, "textures/entity/bandit/tok_champion_bandit.png"),
            new Identifier(TaleOfKingdoms.MODID, "textures/entity/bandit/tok_enemy_archer.png"),
            new Identifier(TaleOfKingdoms.MODID, "textures/entity/bandit/tok_enemy_leader.png"),
            new Identifier(TaleOfKingdoms.MODID, "textures/entity/bandit/tok_pillager_1.png"),
            new Identifier(TaleOfKingdoms.MODID, "textures/entity/bandit/tok_ruined_bandit.png"),
            new Identifier(TaleOfKingdoms.MODID, "textures/entity/bandit/tok_ruined_leader.png")
    };

    private final BowAttackGoal<BanditEntity> bowAttackGoal = new BowAttackGoal<>(this, 0.6D, 20, 16.0F);
    private final CrossbowAttackGoal<BanditEntity> crossbowAttackGoal = new CrossbowAttackGoal<>(this, 0.6D, 12.0F);
    private final MeleeAttackGoal meleeAttackGoal = new MeleeAttackGoal(this, 0.8D, false) {
        public void stop() {
            super.stop();
            BanditEntity.this.setAttacking(false);
        }

        public void start() {
            super.start();
            BanditEntity.this.setAttacking(true);
        }
    };

    private boolean charging;
    private boolean ticked;

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

    public BanditEntity(@NotNull EntityType<? extends PathAwareEntity> entityType, @NotNull World world) {
        super(entityType, world);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess serverWorldAccess, LocalDifficulty localDifficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound nbtCompound) {
        this.initEquipment(localDifficulty);
        this.updateEnchantments(localDifficulty);
        return super.initialize(serverWorldAccess, localDifficulty, spawnReason, entityData, nbtCompound);
    }

    @Override
    protected void initEquipment(LocalDifficulty localDifficulty) {
        Item item = ThreadLocalRandom.current().nextBoolean() ? Items.IRON_SWORD : ThreadLocalRandom.current().nextBoolean() ? Items.BOW : Items.CROSSBOW;
        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(item));
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

    @Override
    protected void initGoals() {
        super.initGoals();
        this.targetSelector.add(1, new ImprovedFollowTargetGoal<>(this, EntityType.PLAYER, true));
        this.targetSelector.add(2, new ImprovedFollowTargetGoal<>(this, EntityTypes.HUNTER, true));
        this.targetSelector.add(3, new ImprovedFollowTargetGoal<>(this, EntityTypes.KNIGHT, true));
        this.targetSelector.add(4, new ImprovedFollowTargetGoal<>(this, EntityTypes.GUILDGUARD, true));
        this.targetSelector.add(5, new ImprovedFollowTargetGoal<>(this, EntityTypes.GUILDARCHER, true));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, MobEntity.class, 100, true, true, livingEntity -> livingEntity instanceof Monster));
        this.goalSelector.add(2, new WanderAroundGoal(this, 0.6D));
        applyEntityAI();
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return TOKEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0D)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20.0D);
    }

    @Override
    public boolean isStationary() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (ticked) return;
        Item item = this.getStackInHand(Hand.MAIN_HAND).getItem();
        if (item == Items.IRON_SWORD) {
            this.goalSelector.add(1, this.meleeAttackGoal);
            this.ticked = true;
        } else if (item == Items.BOW) {
            int i = 20;
            if (this.world.getDifficulty() != Difficulty.HARD) {
                i = 40;
            }

            this.bowAttackGoal.setAttackInterval(i);
            this.goalSelector.add(1, this.bowAttackGoal);
            this.ticked = true;
        } else if (item == Items.CROSSBOW) {
            this.goalSelector.add(1, this.crossbowAttackGoal);
            this.ticked = true;
        }
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        ItemStack itemStack = this.getArrowType(this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW)));
        PersistentProjectileEntity persistentProjectileEntity = this.createArrowProjectile(itemStack, pullProgress);
        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.3333333333333333D) - persistentProjectileEntity.getY();
        double f = target.getZ() - this.getZ();
        double g = MathHelper.sqrt((float) (d * d + f * f));
        persistentProjectileEntity.setVelocity(d, e + g * 0.20000000298023224D, f, 1.6F, (float)(14 - this.world.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntity(persistentProjectileEntity);
    }

    @Override
    public void shoot(LivingEntity target, ItemStack crossbow, ProjectileEntity projectile, float multiShotSpray) {
        this.shoot(this, target, projectile, multiShotSpray, 1.6F);
    }

    protected PersistentProjectileEntity createArrowProjectile(ItemStack arrow, float damageModifier) {
        return ProjectileUtil.createArrowProjectile(this, arrow, damageModifier);
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean canUseRangedWeapon(RangedWeaponItem weapon) {
        return weapon == Items.BOW || weapon == Items.CROSSBOW;
    }

}
