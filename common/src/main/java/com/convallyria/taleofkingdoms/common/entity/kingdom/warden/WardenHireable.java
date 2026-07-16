package com.convallyria.taleofkingdoms.common.entity.kingdom.warden;

import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.FollowPlayerGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.ImprovedFollowTargetGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.WanderAroundGuildGoal;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class WardenHireable extends TOKEntity {

    private int internalTotalExperience;

    private static final EntityDataAccessor<Integer> TOTAL_EXPERIENCE;

    static {
        TOTAL_EXPERIENCE = SynchedEntityData.defineId(WardenHireable.class, EntityDataSerializers.INT);
    }

    public WardenHireable(@NotNull EntityType<? extends PathfinderMob> entityType, @NotNull Level world) {
        super(entityType, world);
    }

    public int getExperiencePoints() {
        return this.entityData.get(TOTAL_EXPERIENCE);
    }

    @Override
    public abstract Optional<ResourceLocation> getSkin();

    private Goal followGoal;

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (hand == InteractionHand.OFF_HAND) return InteractionResult.FAIL;
        this.toggleFollowGoal(player);
        return super.mobInteract(player, hand);
    }

    public void toggleFollowGoal(Player cause) {
        if (followGoal != null) {
            if (!this.level().isClientSide()) cause.sendSystemMessage(getGuardText());
            this.targetSelector.removeGoal(followGoal);
            this.followGoal = null;
        } else {
            if (!this.level().isClientSide()) cause.sendSystemMessage(getFollowText());
            this.targetSelector.addGoal(5, followGoal = new FollowPlayerGoal(this, 0.6D, 2.5f, 30));
        }
    }

    public boolean isFollowingPlayer() {
        return followGoal != null;
    }

    public abstract Component getFollowText();

    public abstract Component getGuardText();

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TOTAL_EXPERIENCE, internalTotalExperience);
    }

    @Override
    public boolean killedEntity(ServerLevel world, LivingEntity other) {
        this.internalTotalExperience++;
        this.entityData.set(TOTAL_EXPERIENCE, this.internalTotalExperience);
        if (this.tryLevelUp()) {
            this.updateLevelledAttributes();
            this.playLevelUpEffects();
            this.setHealth(this.getMaxHealth());
        }
        return super.killedEntity(world, other);
    }

    protected abstract boolean tryLevelUp();

    protected void playLevelUpEffects() {
        for (int i = 0; i < 5; ++i) {
            double d = this.random.nextGaussian() * 0.02;
            double e = this.random.nextGaussian() * 0.02;
            double f = this.random.nextGaussian() * 0.02;
            this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0), this.getRandomY() + 1.0, this.getRandomZ(1.0), d, e, f);
        }
        this.level().playLocalSound(this, SoundEvents.ANVIL_USE, SoundSource.PLAYERS, 1f, 1f);
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

    public static AttributeSupplier.Builder createMobAttributes() {
        return TOKEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 60)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D);
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean isStationary() {
        return false;
    }

    public abstract void updateLevelledAttributes();

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.internalTotalExperience = nbt.getInt("XpTotal");
        this.entityData.set(TOTAL_EXPERIENCE, internalTotalExperience);
        this.updateLevelledAttributes();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("XpTotal", internalTotalExperience);
    }
}
