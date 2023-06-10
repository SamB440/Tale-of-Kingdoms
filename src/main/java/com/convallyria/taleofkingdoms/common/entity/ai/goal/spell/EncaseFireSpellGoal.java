package com.convallyria.taleofkingdoms.common.entity.ai.goal.spell;

import com.convallyria.taleofkingdoms.common.entity.generic.SpellcastingEntity;
import com.convallyria.taleofkingdoms.common.utils.BlockUtils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class EncaseFireSpellGoal extends CastSpellGoal {

    private final SpellcastingEntity spellCaster;
    private int targetId;

    public EncaseFireSpellGoal(SpellcastingEntity spellCaster) {
        super(spellCaster);
        this.spellCaster = spellCaster;
    }

    @Override
    public boolean canStart() {
        if (!super.canStart()) {
            return false;
        } else if (spellCaster.getTarget() == null) {
            return false;
        } else return spellCaster.getTarget().getId() != this.targetId;
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
        LivingEntity target = spellCaster.getTarget();
        for (BlockPos blockPos : BlockUtils.getNearbyBlocks(target.getBlockPos(), 1)) {
            target.getWorld().setBlockState(blockPos, Blocks.NETHERRACK.getDefaultState());
        }
        target.getWorld().setBlockState(target.getBlockPos(), Blocks.FIRE.getDefaultState());
        spellCaster.getTarget().addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200));
    }

    @Override
    protected SoundEvent getSoundPrepare() {
        return SoundEvents.ENTITY_ILLUSIONER_PREPARE_BLINDNESS;
    }

    @Override
    protected SpellcastingEntity.Spell getSpell() {
        return SpellcastingEntity.Spell.FIRE;
    }
}