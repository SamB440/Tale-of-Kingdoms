package com.convallyria.taleofkingdoms.common.entity.generic;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.EntityEffectParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.EnumSet;

public abstract class SpellcastingEntity extends HostileEntity {

    private static final TrackedData<Byte> SPELL;
    protected int spellTicks;
    private SpellcastingEntity.Spell spell;

    protected SpellcastingEntity(EntityType<? extends SpellcastingEntity> entityType, World world) {
        super(entityType, world);
        this.spell = SpellcastingEntity.Spell.NONE;
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    @Override
    public void checkDespawn() { }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(SPELL, (byte)0);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.spellTicks = nbt.getInt("SpellTicks");
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
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
        if (this.getWorld().isClient) {
            return this.dataTracker.get(SPELL) > 0;
        } else {
            return this.spellTicks > 0;
        }
    }

    public void setSpell(SpellcastingEntity.Spell spell) {
        this.spell = spell;
        this.dataTracker.set(SPELL, (byte)spell.id);
    }

    protected SpellcastingEntity.Spell getSpell() {
        return !this.getWorld().isClient ? this.spell : SpellcastingEntity.Spell.byId(this.dataTracker.get(SPELL));
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        if (this.spellTicks > 0) {
            --this.spellTicks;
        }

    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient && this.isSpellcasting()) {
            Spell spell = this.getSpell();
            float f = (float) spell.particleVelocity[0];
            float g = (float) spell.particleVelocity[1];
            float h = (float) spell.particleVelocity[2];
            float i = this.bodyYaw * (float) (Math.PI / 180.0) + MathHelper.cos((float)this.age * 0.6662F) * 0.25F;
            float j = MathHelper.cos(i);
            float k = MathHelper.sin(i);
            double d = 0.6 * (double)this.getScale();
            double e = 1.8 * (double)this.getScale();
            this.getWorld()
                    .addParticle(
                            EntityEffectParticleEffect.create(ParticleTypes.ENTITY_EFFECT, f, g, h),
                            this.getX() + (double)j * d,
                            this.getY() + e,
                            this.getZ() + (double)k * d,
                            0.0,
                            0.0,
                            0.0
                    );
            this.getWorld()
                    .addParticle(
                            EntityEffectParticleEffect.create(ParticleTypes.ENTITY_EFFECT, f, g, h),
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
        SPELL = DataTracker.registerData(SpellcastingEntity.class, TrackedDataHandlerRegistry.BYTE);
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
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        public boolean canStart() {
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
                SpellcastingEntity.this.getLookControl().lookAt(SpellcastingEntity.this.getTarget(), (float) SpellcastingEntity.this.getMaxHeadRotation(), (float) SpellcastingEntity.this.getMaxLookPitchChange());
            }
        }
    }
}