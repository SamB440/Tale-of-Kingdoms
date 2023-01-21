package com.convallyria.taleofkingdoms.common.entity.ai.goal.spell;

import com.convallyria.taleofkingdoms.common.entity.generic.SpellcastingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;

public class GiveInvisibilityGoal extends CastSpellGoal {

    private final SpellcastingEntity spellCaster;

    public GiveInvisibilityGoal(SpellcastingEntity spellCaster) {
        super(spellCaster);
        this.spellCaster = spellCaster;
    }

    @Override
    public boolean canStart() {
        if (!super.canStart()) {
            return false;
        } else {
            return !spellCaster.hasStatusEffect(StatusEffects.INVISIBILITY);
        }
    }

    @Override
    protected int getSpellTicks() {
        return 20;
    }

    @Override
    protected int startTimeDelay() {
        return 340;
    }

    @Override
    protected void castSpell() {
        spellCaster.swingHand(Hand.OFF_HAND);
        spellCaster.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 1200));
    }

    @Nullable
    @Override
    protected SoundEvent getSoundPrepare() {
        return SoundEvents.ENTITY_ILLUSIONER_PREPARE_MIRROR;
    }

    @Override
    protected SpellcastingEntity.Spell getSpell() {
        return SpellcastingEntity.Spell.DISAPPEAR;
    }
}