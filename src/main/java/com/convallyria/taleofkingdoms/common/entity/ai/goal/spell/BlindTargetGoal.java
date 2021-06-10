package com.convallyria.taleofkingdoms.common.entity.ai.goal.spell;

import com.convallyria.taleofkingdoms.common.entity.generic.SpellcastingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.world.Difficulty;

public class BlindTargetGoal extends CastSpellGoal {

    private final SpellcastingEntity spellCaster;
    private int targetId;

    public BlindTargetGoal(SpellcastingEntity spellCaster) {
        super(spellCaster);
        this.spellCaster = spellCaster;
    }

    @Override
    public boolean canStart() {
        if (!super.canStart()) {
            return false;
        } else if (spellCaster.getTarget() == null) {
            return false;
        } else if (spellCaster.getTarget().getId() == this.targetId) {
            return false;
        } else {
            return spellCaster.world.getLocalDifficulty(spellCaster.getBlockPos()).isHarderThan((float) Difficulty.NORMAL.ordinal());
        }
    }

    @Override
    public void start() {
        super.start();
        this.targetId = spellCaster.getTarget().getId();
    }

    @Override
    protected int getSpellTicks() {
        return 20;
    }

    @Override
    protected int startTimeDelay() {
        return 180;
    }

    @Override
    protected void castSpell() {
        spellCaster.swingHand(Hand.OFF_HAND);
        spellCaster.getTarget().addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 400));
    }

    @Override
    protected SoundEvent getSoundPrepare() {
        return SoundEvents.ENTITY_ILLUSIONER_PREPARE_BLINDNESS;
    }

    @Override
    protected SpellcastingEntity.Spell getSpell() {
        return SpellcastingEntity.Spell.BLINDNESS;
    }
}