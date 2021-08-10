package com.convallyria.taleofkingdoms.common.entity.ai.goal.spell;

import com.convallyria.taleofkingdoms.common.entity.generic.SpellcastingEntity;
import com.convallyria.taleofkingdoms.common.utils.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;

public class EncaseFireSpellGoal extends CastSpellGoal {

    private final SpellcastingEntity spellCaster;
    private int targetId;

    public EncaseFireSpellGoal(SpellcastingEntity spellCaster) {
        super(spellCaster);
        this.spellCaster = spellCaster;
    }

    @Override
    public boolean canUse() {
        if (!super.canUse()) {
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
        spellCaster.swing(InteractionHand.OFF_HAND);
        LivingEntity target = spellCaster.getTarget();
        for (BlockPos blockPos : BlockUtils.getNearbyBlocks(target.blockPosition(), 1)) {
            target.level.setBlockAndUpdate(blockPos, Blocks.NETHERRACK.defaultBlockState());
        }
        target.level.setBlockAndUpdate(target.blockPosition(), Blocks.FIRE.defaultBlockState());
        spellCaster.getTarget().addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200));
    }

    @Override
    protected SoundEvent getSoundPrepare() {
        return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
    }

    @Override
    protected SpellcastingEntity.Spell getSpell() {
        return SpellcastingEntity.Spell.FIRE;
    }
}