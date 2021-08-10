package com.convallyria.taleofkingdoms.common.entity.ai.goal.spell;

import com.convallyria.taleofkingdoms.common.entity.generic.SpellcastingEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class BlindTargetGoal extends CastSpellGoal {

    private final SpellcastingEntity spellCaster;
    private int targetId;

    public BlindTargetGoal(SpellcastingEntity spellCaster) {
        super(spellCaster);
        this.spellCaster = spellCaster;
    }

    @Override
    public boolean canUse() {
        if (!super.canUse()) {
            return false;
        } else if (spellCaster.getTarget() == null) {
            return false;
        } else if (spellCaster.getTarget().getId() == this.targetId) {
            return false;
        } else {
            return spellCaster.level.getCurrentDifficultyAt(spellCaster.blockPosition()).isHarderThan((float) Difficulty.NORMAL.ordinal());
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
        spellCaster.swing(InteractionHand.OFF_HAND);
        spellCaster.getTarget().addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 400));
    }

    @Override
    protected SoundEvent getSoundPrepare() {
        return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
    }

    @Override
    protected SpellcastingEntity.Spell getSpell() {
        return SpellcastingEntity.Spell.BLINDNESS;
    }
}