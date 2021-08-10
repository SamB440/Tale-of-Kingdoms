package com.convallyria.taleofkingdoms.common.entity.ai.goal.spell;

import com.convallyria.taleofkingdoms.common.entity.generic.SpellcastingEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import org.jetbrains.annotations.Nullable;

public class GiveInvisibilityGoal extends CastSpellGoal {

    private final SpellcastingEntity spellCaster;

    public GiveInvisibilityGoal(SpellcastingEntity spellCaster) {
        super(spellCaster);
        this.spellCaster = spellCaster;
    }

    @Override
    public boolean canUse() {
        if (!super.canUse()) {
            return false;
        } else {
            return !spellCaster.hasEffect(MobEffects.INVISIBILITY);
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
        spellCaster.swing(InteractionHand.OFF_HAND);
        spellCaster.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 1200));
        System.out.println("added invisibility: " + spellCaster.hasEffect(MobEffects.INVISIBILITY));
    }

    @Nullable
    @Override
    protected SoundEvent getSoundPrepare() {
        return SoundEvents.ILLUSIONER_PREPARE_MIRROR;
    }

    @Override
    protected SpellcastingEntity.Spell getSpell() {
        return SpellcastingEntity.Spell.DISAPPEAR;
    }
}