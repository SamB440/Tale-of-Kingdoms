package com.convallyria.taleofkingdoms.common.entity.ai.goal.spell;

import com.convallyria.taleofkingdoms.common.entity.generic.SpellcastingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.sound.SoundEvent;
import org.jetbrains.annotations.Nullable;

public abstract class CastSpellGoal extends Goal {

    private final SpellcastingEntity spellCaster;
    protected int spellCooldown;
    protected int startTime;

    public CastSpellGoal(SpellcastingEntity spellCaster) {
        this.spellCaster = spellCaster;
    }

    @Override
    public boolean canStart() {
        LivingEntity livingEntity = spellCaster.getTarget();
        if (livingEntity != null && livingEntity.isAlive()) {
            if (spellCaster.isSpellcasting()) {
                return false;
            } else {
                return spellCaster.age >= this.startTime;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean shouldContinue() {
        LivingEntity livingEntity = spellCaster.getTarget();
        return livingEntity != null && livingEntity.isAlive() && this.spellCooldown > 0;
    }

    @Override
    public void start() {
        this.spellCooldown = this.getInitialCooldown();
        spellCaster.setSpellTicks(this.getSpellTicks());
        this.startTime = spellCaster.age + this.startTimeDelay();
        SoundEvent soundEvent = this.getSoundPrepare();
        if (soundEvent != null) {
            spellCaster.playSound(soundEvent, 1.0F, 1.0F);
        }

        spellCaster.setSpell(this.getSpell());
    }

    @Override
    public void tick() {
        --this.spellCooldown;
        if (this.spellCooldown == 0) {
            this.castSpell();
            spellCaster.playSound(spellCaster.getCastSpellSound(), 1.0F, 1.0F);
        }
    }

    protected abstract void castSpell();

    protected int getInitialCooldown() {
        return 20;
    }

    protected abstract int getSpellTicks();

    protected abstract int startTimeDelay();

    @Nullable
    protected abstract SoundEvent getSoundPrepare();

    protected abstract SpellcastingEntity.Spell getSpell();
}