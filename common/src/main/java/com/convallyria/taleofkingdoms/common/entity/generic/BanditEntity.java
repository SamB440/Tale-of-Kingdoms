package com.convallyria.taleofkingdoms.common.entity.generic;

import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.States;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.BowAttackGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.CrossbowAttackGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.ImprovedFollowTargetGoal;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class BanditEntity extends TOKEntity implements CrossbowAttackMob, RangedAttackMob, States, Enemy {

    public static final List<ResourceLocation> VALID_SKINS = List.of(
            identifier("textures/entity/bandit/archer_tok.png"),
            identifier("textures/entity/bandit/bandit_ruiner.png"),
            identifier("textures/entity/bandit/brigand_chief_2.png"),
            identifier("textures/entity/bandit/custom_adventurer_norska_warrior_1.png"),
            identifier("textures/entity/bandit/custom_adventurer_stealthy_thief.png"),
            identifier("textures/entity/bandit/custom_outlaw_merry_man_1.png"),
            identifier("textures/entity/bandit/custom_outlaw_merry_man_2.png"),
            identifier("textures/entity/bandit/custom_outlaw_merry_man_3.png"),
            identifier("textures/entity/bandit/tok_bandit_hood_1.png"),
            identifier("textures/entity/bandit/tok_brigand.png"),
            identifier("textures/entity/bandit/tok_champion_bandit.png"),
            identifier("textures/entity/bandit/tok_enemy_archer.png"),
            identifier("textures/entity/bandit/tok_enemy_leader.png"),
            identifier("textures/entity/bandit/tok_pillager_1.png"),
            identifier("textures/entity/bandit/tok_ruined_bandit.png"),
            identifier("textures/entity/bandit/tok_ruined_leader.png")
    );

    private final BowAttackGoal<BanditEntity> bowAttackGoal = new BowAttackGoal<>(this, 0.6D, 20, 16.0F);
    private final CrossbowAttackGoal<BanditEntity> crossbowAttackGoal = new CrossbowAttackGoal<>(this, 0.6D, 12.0F);
    private final MeleeAttackGoal meleeAttackGoal = new MeleeAttackGoal(this, 0.8D, false) {
        public void stop() {
            super.stop();
            BanditEntity.this.setAggressive(false);
        }

        public void start() {
            super.start();
            BanditEntity.this.setAggressive(true);
        }
    };

    private boolean charging;
    private boolean ticked;

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

    private final ResourceLocation skin;

    public BanditEntity(@NotNull EntityType<? extends PathfinderMob> entityType, @NotNull Level world) {
        super(entityType, world);
        this.skin = VALID_SKINS.get(ThreadLocalRandom.current().nextInt(VALID_SKINS.size()));
    }

    @Override
    public Optional<ResourceLocation> getSkin() {
        return Optional.of(skin);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverWorldAccess, DifficultyInstance localDifficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData) {
        this.populateDefaultEquipmentSlots(random, localDifficulty);
        this.populateDefaultEquipmentEnchantments(serverWorldAccess, random, localDifficulty);
        return super.finalizeSpawn(serverWorldAccess, localDifficulty, spawnReason, entityData);
    }

    @Override
    protected void populateDefaultEquipmentSlots(net.minecraft.util.RandomSource random, DifficultyInstance localDifficulty) {
        super.populateDefaultEquipmentSlots(random, localDifficulty);
        Item item = random.nextBoolean() ? Items.IRON_SWORD : random.nextBoolean() ? Items.BOW : Items.CROSSBOW;
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(item));
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

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new ImprovedFollowTargetGoal<>(this, EntityType.PLAYER, true));
        this.targetSelector.addGoal(2, new ImprovedFollowTargetGoal<>(this, EntityTypes.HUNTER.get(), true));
        this.targetSelector.addGoal(3, new ImprovedFollowTargetGoal<>(this, EntityTypes.KNIGHT.get(), true));
        this.targetSelector.addGoal(4, new ImprovedFollowTargetGoal<>(this, EntityTypes.WARRIOR.get(), true));
        this.targetSelector.addGoal(5, new ImprovedFollowTargetGoal<>(this, EntityTypes.ARCHER.get(), true));
        this.targetSelector.addGoal(6, new ImprovedFollowTargetGoal<>(this, EntityTypes.GUILDGUARD.get(), true));
        this.targetSelector.addGoal(7, new ImprovedFollowTargetGoal<>(this, EntityTypes.GUILDARCHER.get(), true));
        this.targetSelector.addGoal(8, new ImprovedFollowTargetGoal<>(this, EntityTypes.GUILDVILLAGER.get(), true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Mob.class, 100, true, true, livingEntity -> livingEntity instanceof Enemy && !(livingEntity instanceof BanditEntity)));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.6D));
        applyEntityAI();
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return TOKEntity.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE, 2.5D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 20.0D);
    }

    @Override
    public boolean isStationary() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (ticked) return;
        Item item = this.getItemInHand(InteractionHand.MAIN_HAND).getItem();
        if (item == Items.IRON_SWORD) {
            this.goalSelector.addGoal(1, this.meleeAttackGoal);
            this.ticked = true;
        } else if (item == Items.BOW) {
            int i = 20;
            if (this.level().getDifficulty() != Difficulty.HARD) {
                i = 40;
            }

            this.bowAttackGoal.setAttackInterval(i);
            this.goalSelector.addGoal(1, this.bowAttackGoal);
            this.ticked = true;
        } else if (item == Items.CROSSBOW) {
            this.goalSelector.addGoal(1, this.crossbowAttackGoal);
            this.ticked = true;
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float pullProgress) {
        final ItemStack shotFrom = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW));
        ItemStack itemStack = this.getProjectile(shotFrom);
        AbstractArrow persistentProjectileEntity = this.createArrowProjectile(itemStack, pullProgress, shotFrom);
        double d = target.getX() - this.getX();
        double e = target.getY(0.3333333333333333D) - persistentProjectileEntity.getY();
        double f = target.getZ() - this.getZ();
        double g = Mth.sqrt((float) (d * d + f * f));
        persistentProjectileEntity.shoot(d, e + g * 0.20000000298023224D, f, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(persistentProjectileEntity);
    }

    protected AbstractArrow createArrowProjectile(ItemStack arrow, float damageModifier, ItemStack shotFrom) {
        return ProjectileUtil.getMobArrow(this, arrow, damageModifier, shotFrom.isEmpty() ? null : shotFrom);
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean canFireProjectileWeapon(ProjectileWeaponItem weapon) {
        return weapon == Items.BOW || weapon == Items.CROSSBOW;
    }

}