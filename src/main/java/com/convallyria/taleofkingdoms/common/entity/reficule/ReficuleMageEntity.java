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
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * We copied the illusioner AI for now!
 */
public class ReficuleMageEntity extends SpellcastingEntity implements Monster, TeleportAbility, RangedAttackMob {

    // wtf are these
    private int field_7296;
    private final Vec3d[][] field_7297;

    public ReficuleMageEntity(@NotNull EntityType<? extends ReficuleMageEntity> entityType, @NotNull World world) {
        super(entityType, world);
        this.experiencePoints = 5;
        this.field_7297 = new Vec3d[2][4];

        for(int i = 0; i < 4; ++i) {
            this.field_7297[0][i] = Vec3d.ZERO;
            this.field_7297[1][i] = Vec3d.ZERO;
        }
    }

    @Override
    public boolean teleportTo(Entity entity) {
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        return this.teleport(x, y, z, true);
    }

    @Override
    public boolean spreadFire() {
        return true;
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
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new SpellcastingEntity.LookAtTargetGoal());
        this.goalSelector.add(2, new GiveInvisibilityGoal(this));
        this.goalSelector.add(3, new EncaseFireSpellGoal(this));
        this.goalSelector.add(4, new FireballSpellGoal(this));
        this.goalSelector.add(5, new BlindTargetGoal(this));
        this.goalSelector.add(6, new WanderAroundGoal(this, 0.6D));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.add(8, new LookAtEntityGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.add(1, (new RevengeGoal(this, RaiderEntity.class)).setGroupRevenge());
        this.targetSelector.add(2, new TeleportTowardsPlayerGoal(this, entity -> {
            return entity.squaredDistanceTo(this) < this.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
        }));
        this.targetSelector.add(3, (new FollowTargetGoal<>(this, PlayerEntity.class, true)).setMaxTimeWithoutVisibility(300));
        this.targetSelector.add(4, new ImprovedFollowTargetGoal<>(this, EntityTypes.GUILDGUARD, true));
        this.targetSelector.add(5, new ImprovedFollowTargetGoal<>(this, EntityTypes.GUILDARCHER, true));
        this.targetSelector.add(5, new ImprovedFollowTargetGoal<>(this, EntityTypes.HUNTER, true));
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return HostileEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 18.0D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D) // Big increase! Needs balancing?
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 9.0D)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.5D);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityTag) {
        ItemStack wand = new ItemStack(Items.STICK);
        wand.addEnchantment(Enchantments.MENDING, 1); // Want them to look fancy :)
        this.equipStack(EquipmentSlot.OFFHAND, wand);
        return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public Box getVisibilityBoundingBox() {
        return this.getBoundingBox().expand(3.0D, 0.0D, 3.0D);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.world.isClient && this.isInvisible()) {
            --this.field_7296;
            if (this.field_7296 < 0) {
                this.field_7296 = 0;
            }

            if (this.hurtTime != 1 && this.age % 1200 != 0) {
                if (this.hurtTime == this.maxHurtTime - 1) {
                    this.field_7296 = 3;

                    for(int l = 0; l < 4; ++l) {
                        this.field_7297[0][l] = this.field_7297[1][l];
                        this.field_7297[1][l] = new Vec3d(0.0D, 0.0D, 0.0D);
                    }
                }
            } else {
                this.field_7296 = 3;

                int k;
                for(k = 0; k < 4; ++k) {
                    this.field_7297[0][k] = this.field_7297[1][k];
                    this.field_7297[1][k] = new Vec3d((double)(-6.0F + (float)this.random.nextInt(13)) * 0.5D, Math.max(0, this.random.nextInt(6) - 4), (double)(-6.0F + (float)this.random.nextInt(13)) * 0.5D);
                }

                for (k = 0; k < 16; ++k) {
                    this.world.addParticle(ParticleTypes.CLOUD, this.getParticleX(0.5D), this.getRandomBodyY(), this.offsetZ(0.5D), 0.0D, 0.0D, 0.0D);
                }

                this.world.playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_ILLUSIONER_MIRROR_MOVE, this.getSoundCategory(), 1.0F, 1.0F, false);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public Vec3d[] method_7065(float f) {
        if (this.field_7296 <= 0) {
            return this.field_7297[1];
        } else {
            double d = ((float)this.field_7296 - f) / 3.0F;
            d = Math.pow(d, 0.25D);
            Vec3d[] vec3ds = new Vec3d[4];

            for(int i = 0; i < 4; ++i) {
                vec3ds[i] = this.field_7297[1][i].multiply(1.0D - d).add(this.field_7297[0][i].multiply(d));
            }

            return vec3ds;
        }
    }

    @Override
    public SoundEvent getCastSpellSound() {
        return SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL;
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        ItemStack itemStack = this.getArrowType(this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW)));
        PersistentProjectileEntity persistentProjectileEntity = this.createArrowProjectile(itemStack, pullProgress);
        double d = target.getX() - this.getX();
        double e = target.getBodyY(0.3333333333333333D) - persistentProjectileEntity.getY();
        double g = target.getZ() - this.getZ();
        double h = Math.sqrt(d * d + g * g);
        persistentProjectileEntity.setVelocity(d, e + h * 0.20000000298023224D, g, 1.6F, (float)(14 - this.world.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntity(persistentProjectileEntity);
    }

    protected PersistentProjectileEntity createArrowProjectile(ItemStack itemStack, float f) {
        return ProjectileUtil.createArrowProjectile(this, itemStack, f);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public State getState() {
        if (this.isSpellcasting()) {
            return State.SPELLCASTING;
        } else {
            return this.isAttacking() ? State.BOW_AND_ARROW : State.CROSSED;
        }
    }
}
