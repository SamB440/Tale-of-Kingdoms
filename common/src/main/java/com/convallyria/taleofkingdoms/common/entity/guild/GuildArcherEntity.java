package com.convallyria.taleofkingdoms.common.entity.guild;

import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.States;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.BowAttackGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.CrossbowAttackGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.ImprovedFollowTargetGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.WanderAroundGuildGoal;
import com.convallyria.taleofkingdoms.common.translation.Translations;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class GuildArcherEntity extends TOKEntity implements CrossbowAttackMob, RangedAttackMob, States {

    private static final List<ResourceLocation> VALID_SKINS = List.of(
            /*identifier("textures/entity/guildarcher/guildarcherone.png"),*/
            identifier("textures/entity/guildarcher/guildarchertwo.png"),
            identifier("textures/entity/guildarcher/guildarcherthree.png")
    );

    private boolean charging;
    private boolean ticked;

    private final ResourceLocation skin;

    public GuildArcherEntity(EntityType<? extends PathfinderMob> entityType, Level world) {
        super(entityType, world);
        this.skin = VALID_SKINS.get(ThreadLocalRandom.current().nextInt(VALID_SKINS.size()));
    }

    @Override
    public Optional<ResourceLocation> getSkin() {
        return Optional.of(skin);
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

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData) {
        SpawnGroupData entityReturnData = super.finalizeSpawn(world, difficulty, spawnReason, entityData);
        int value = ThreadLocalRandom.current().nextInt(2);
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(value == 1 ? Items.BOW : Items.CROSSBOW));
        if (this.getItemInHand(InteractionHand.MAIN_HAND).getItem() == Items.BOW) {
            this.goalSelector.addGoal(1, new BowAttackGoal<>(this, 0.6D, 15, 8.0F));
        } else {
            this.goalSelector.addGoal(1, new CrossbowAttackGoal<>(this, 0.6D, 12.0F));
        }
        return entityReturnData;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new WanderAroundGuildGoal(this, 0.6D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 30.0F));
        this.targetSelector.addGoal(1, new ImprovedFollowTargetGoal<>(this, EntityTypes.REFICULE_SOLDIER.get(), false));
        this.targetSelector.addGoal(2, new ImprovedFollowTargetGoal<>(this, EntityTypes.REFICULE_GUARDIAN.get(), false));
        this.targetSelector.addGoal(3, new ImprovedFollowTargetGoal<>(this, EntityTypes.REFICULE_MAGE.get(), false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Mob.class, 100,
                true, true, livingEntity -> livingEntity instanceof Enemy));
    }

    @Override
    public void tick() {
        super.tick();
        if (ticked) return;
        Item item = this.getItemInHand(InteractionHand.MAIN_HAND).getItem();
        if (item == Items.BOW) {
            this.goalSelector.addGoal(1, new BowAttackGoal<>(this, 0.6D, 15, 8.0F));
            this.ticked = true;
        } else if (item == Items.CROSSBOW) {
            this.goalSelector.addGoal(1, new CrossbowAttackGoal<>(this, 0.6D, 12.0F));
            this.ticked = true;
        }
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return TOKEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.ATTACK_DAMAGE, 15.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (hand == InteractionHand.OFF_HAND || !player.level().isClientSide()) return InteractionResult.FAIL;
        //TODO
        Translations.GUILDMEMBER_START.send(player);
        return InteractionResult.PASS;
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
    public boolean isStationary() {
        return false;
    }
}
