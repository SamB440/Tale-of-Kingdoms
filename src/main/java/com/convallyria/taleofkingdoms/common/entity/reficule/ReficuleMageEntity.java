package com.convallyria.taleofkingdoms.common.entity.reficule;

import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.ImprovedFollowTargetGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.TeleportTowardsPlayerGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.spell.BlindTargetGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.spell.EncaseFireSpellGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.spell.FireballSpellGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.spell.GiveInvisibilityGoal;
import com.convallyria.taleofkingdoms.common.entity.generic.SpellcastingEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * We copied the illusioner AI for now!
 */
public class ReficuleMageEntity extends SpellcastingEntity implements Enemy, TeleportAbility, RangedAttackMob {

    // wtf are these
    private int field_7296;
    private final Vec3[][] field_7297;

    public ReficuleMageEntity(@NotNull EntityType<? extends ReficuleMageEntity> entityType, @NotNull Level world) {
        super(entityType, world);
        this.xpReward = 5;
        this.field_7297 = new Vec3[2][4];

        for(int i = 0; i < 4; ++i) {
            this.field_7297[0][i] = Vec3.ZERO;
            this.field_7297[1][i] = Vec3.ZERO;
        }
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
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SpellcastingEntity.LookAtTargetGoal());
        this.goalSelector.addGoal(2, new GiveInvisibilityGoal(this));
        this.goalSelector.addGoal(3, new EncaseFireSpellGoal(this));
        this.goalSelector.addGoal(4, new FireballSpellGoal(this));
        this.goalSelector.addGoal(5, new BlindTargetGoal(this));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(1, new TeleportTowardsPlayerGoal(this, entity -> {
            return entity.distanceToSqr(this) < this.getAttributeValue(Attributes.FOLLOW_RANGE);
        }));
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, Player.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new ImprovedFollowTargetGoal<>(this, EntityTypes.GUILDGUARD, false));
        this.targetSelector.addGoal(4, new ImprovedFollowTargetGoal<>(this, EntityTypes.GUILDARCHER, false));
        this.targetSelector.addGoal(5, new ImprovedFollowTargetGoal<>(this, EntityTypes.HUNTER, false));
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.FOLLOW_RANGE, 18.0D)
                .add(Attributes.MAX_HEALTH, 20.0D) // Big increase! Needs balancing?
                .add(Attributes.ATTACK_DAMAGE, 9.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData, @Nullable CompoundTag entityTag) {
        ItemStack wand = new ItemStack(Items.STICK);
        wand.enchant(Enchantments.MENDING, 1); // Want them to look fancy :)
        this.setItemSlot(EquipmentSlot.OFFHAND, wand);
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData, entityTag);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public AABB getBoundingBoxForCulling() {
        return this.getBoundingBox().inflate(3.0D, 0.0D, 3.0D);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level.isClientSide && this.isInvisible()) {
            --this.field_7296;
            if (this.field_7296 < 0) {
                this.field_7296 = 0;
            }

            if (this.hurtTime != 1 && this.tickCount % 1200 != 0) {
                if (this.hurtTime == this.hurtDuration - 1) {
                    this.field_7296 = 3;

                    for(int l = 0; l < 4; ++l) {
                        this.field_7297[0][l] = this.field_7297[1][l];
                        this.field_7297[1][l] = new Vec3(0.0D, 0.0D, 0.0D);
                    }
                }
            } else {
                this.field_7296 = 3;
                float f = -6.0F;

                int k;
                for(k = 0; k < 4; ++k) {
                    this.field_7297[0][k] = this.field_7297[1][k];
                    this.field_7297[1][k] = new Vec3((double)(-6.0F + (float)this.random.nextInt(13)) * 0.5D, Math.max(0, this.random.nextInt(6) - 4), (double)(-6.0F + (float)this.random.nextInt(13)) * 0.5D);
                }

                for(k = 0; k < 16; ++k) {
                    this.level.addParticle(ParticleTypes.CLOUD, this.getRandomX(0.5D), this.getRandomY(), this.getZ(0.5D), 0.0D, 0.0D, 0.0D);
                }

                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ILLUSIONER_MIRROR_MOVE, this.getSoundSource(), 1.0F, 1.0F, false);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public Vec3[] method_7065(float f) {
        if (this.field_7296 <= 0) {
            return this.field_7297[1];
        } else {
            double d = ((float)this.field_7296 - f) / 3.0F;
            d = Math.pow(d, 0.25D);
            Vec3[] vec3ds = new Vec3[4];

            for(int i = 0; i < 4; ++i) {
                vec3ds[i] = this.field_7297[1][i].scale(1.0D - d).add(this.field_7297[0][i].scale(d));
            }

            return vec3ds;
        }
    }

    @Override
    public SoundEvent getCastSpellSound() {
        return SoundEvents.ILLUSIONER_CAST_SPELL;
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float f) {
        ItemStack itemStack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW)));
        AbstractArrow persistentProjectileEntity = ProjectileUtil.getMobArrow(this, itemStack, f);
        double d = livingEntity.getX() - this.getX();
        double e = livingEntity.getY(0.3333333333333333D) - persistentProjectileEntity.getY();
        double g = livingEntity.getZ() - this.getZ();
        double h = Math.sqrt(d * d + g * g);
        persistentProjectileEntity.shoot(d, e + h * 0.20000000298023224D, g, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(persistentProjectileEntity);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public State getState() {
        if (this.isSpellcasting()) {
            return State.SPELLCASTING;
        } else {
            return this.isAggressive() ? State.BOW_AND_ARROW : State.CROSSED;
        }
    }
}
