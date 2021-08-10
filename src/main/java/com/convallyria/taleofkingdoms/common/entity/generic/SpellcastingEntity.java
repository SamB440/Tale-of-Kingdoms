package com.convallyria.taleofkingdoms.common.entity.generic;

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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPELL, (byte)0);
    }

    public void readCustomDataFromTag(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.spellTicks = tag.getInt("SpellTicks");
    }

    public void writeCustomDataToTag(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("SpellTicks", this.spellTicks);
    }

    @OnlyIn(Dist.CLIENT)
    public State getState() {
        if (this.isSpellcasting()) {
            return State.SPELLCASTING;
        } else {
            return State.CROSSED;
        }
    }

    public boolean isSpellcasting() {
        if (this.level.isClientSide) {
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
        return !this.level.isClientSide ? this.spell : SpellcastingEntity.Spell.byId(this.entityData.get(SPELL));
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.spellTicks > 0) {
            --this.spellTicks;
        }

    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide && this.isSpellcasting()) {
            SpellcastingEntity.Spell spell = this.getSpell();
            double d = spell.particleVelocity[0];
            double e = spell.particleVelocity[1];
            double f = spell.particleVelocity[2];
            float g = this.yBodyRot * 0.017453292F + Mth.cos((float)this.tickCount * 0.6662F) * 0.25F;
            float h = Mth.cos(g);
            float i = Mth.sin(g);
            this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + (double)h * 0.6D, this.getY() + 1.8D, this.getZ() + (double)i * 0.6D, d, e, f);
            this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() - (double)h * 0.6D, this.getY() + 1.8D, this.getZ() - (double)i * 0.6D, d, e, f);
        }

    }

    public int getSpellTicks() {
        return this.spellTicks;
    }

    public void setSpellTicks(int spellTicks) {
        this.spellTicks = spellTicks;
    }

    public abstract SoundEvent getCastSpellSound();

    @OnlyIn(Dist.CLIENT)
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