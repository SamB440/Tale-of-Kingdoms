package com.convallyria.taleofkingdoms.common.entity.generic;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import java.util.EnumSet;

public abstract class SpellcastingEntity extends Monster {

    private static final EntityDataAccessor<Byte> SPELL;
    protected int spellTicks;
    private SpellcastingEntity.Spell spell;

    protected SpellcastingEntity(EntityType<? extends SpellcastingEntity> entityType, Level world) {
        super(entityType, world);
        this.spell = SpellcastingEntity.Spell.NONE;
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceSquared) {
        return false;
    }

    @Override
    public void checkDespawn() { }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SPELL, (byte)0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.spellTicks = nbt.getInt("SpellTicks");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("SpellTicks", this.spellTicks);
    }

    @Environment(EnvType.CLIENT)
    public State getState() {
        if (this.isSpellcasting()) {
            return State.SPELLCASTING;
        } else {
            return State.CROSSED;
        }
    }

    public boolean isSpellcasting() {
        if (this.level().isClientSide) {
            return this.entityData.get(SPELL) > 0;
        } else {
            return this.spellTicks > 0;
        }
    }

    public void setSpell(SpellcastingEntity.Spell spell) {
        this.spell = spell;
        this.entityData.set(SPELL, (byte)spell.id);
    }

    protected SpellcastingEntity.Spell getSpell() {
        return !this.level().isClientSide ? this.spell : SpellcastingEntity.Spell.byId(this.entityData.get(SPELL));
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.spellTicks > 0) {
            --this.spellTicks;
        }

    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide && this.isSpellcasting()) {
            Spell spell = this.getSpell();
            float f = (float) spell.particleVelocity[0];
            float g = (float) spell.particleVelocity[1];
            float h = (float) spell.particleVelocity[2];
            float i = this.yBodyRot * (float) (Math.PI / 180.0) + Mth.cos((float)this.tickCount * 0.6662F) * 0.25F;
            float j = Mth.cos(i);
            float k = Mth.sin(i);
            double d = 0.6 * (double)this.getScale();
            double e = 1.8 * (double)this.getScale();
            this.level()
                    .addParticle(
                            ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, f, g, h),
                            this.getX() + (double)j * d,
                            this.getY() + e,
                            this.getZ() + (double)k * d,
                            0.0,
                            0.0,
                            0.0
                    );
            this.level()
                    .addParticle(
                            ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, f, g, h),
                            this.getX() - (double)j * d,
                            this.getY() + e,
                            this.getZ() - (double)k * d,
                            0.0,
                            0.0,
                            0.0
                    );
        }

    }

    public int getSpellTicks() {
        return this.spellTicks;
    }

    public void setSpellTicks(int spellTicks) {
        this.spellTicks = spellTicks;
    }

    public abstract SoundEvent getCastSpellSound();

    @Environment(EnvType.CLIENT)
    public enum State {
        CROSSED,
        ATTACKING,
        SPELLCASTING,
        BOW_AND_ARROW,
        CROSSBOW_HOLD,
        CROSSBOW_CHARGE,
        CELEBRATING,
        NEUTRAL;

        State() {
        }
    }

    static {
        SPELL = SynchedEntityData.defineId(SpellcastingEntity.class, EntityDataSerializers.BYTE);
    }

    public enum Spell {
        NONE(0, 0.0D, 0.0D, 0.0D),
        SUMMON_VEX(1, 0.7D, 0.7D, 0.8D),
        FANGS(2, 0.4D, 0.3D, 0.35D),
        WOLOLO(3, 0.7D, 0.5D, 0.2D),
        DISAPPEAR(4, 0.3D, 0.3D, 0.8D),
        BLINDNESS(5, 0.1D, 0.1D, 0.2D),
        FIRE(6, 0.1D, 0.1D, 0.2D);

        private final int id;
        private final double[] particleVelocity;

        Spell(int id, double particleVelocityX, double particleVelocityY, double particleVelocityZ) {
            this.id = id;
            this.particleVelocity = new double[]{particleVelocityX, particleVelocityY, particleVelocityZ};
        }

        public static SpellcastingEntity.Spell byId(int id) {
            SpellcastingEntity.Spell[] var1 = values();
            int var2 = var1.length;

            for (Spell spell : var1) {
                if (id == spell.id) {
                    return spell;
                }
            }

            return NONE;
        }
    }

    public class LookAtTargetGoal extends Goal {
        public LookAtTargetGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            return SpellcastingEntity.this.getSpellTicks() > 0;
        }

        public void start() {
            super.start();
            SpellcastingEntity.this.navigation.stop();
        }

        public void stop() {
            super.stop();
            SpellcastingEntity.this.setSpell(SpellcastingEntity.Spell.NONE);
        }

        public void tick() {
            if (SpellcastingEntity.this.getTarget() != null) {
                SpellcastingEntity.this.getLookControl().setLookAt(SpellcastingEntity.this.getTarget(), (float) SpellcastingEntity.this.getMaxHeadYRot(), (float) SpellcastingEntity.this.getMaxHeadXRot());
            }
        }
    }
}