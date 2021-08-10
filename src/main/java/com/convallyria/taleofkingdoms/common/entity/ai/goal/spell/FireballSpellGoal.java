package com.convallyria.taleofkingdoms.common.entity.ai.goal.spell;

import com.convallyria.taleofkingdoms.common.entity.generic.SpellcastingEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.phys.Vec3;

public class FireballSpellGoal extends CastSpellGoal {

    private final SpellcastingEntity spellCaster;
    private int targetId;

    public FireballSpellGoal(SpellcastingEntity spellCaster) {
        super(spellCaster);
        this.spellCaster = spellCaster;
    }

    @Override
    public boolean canUse() {
        if (!super.canUse()) {
            return false;
        } else return spellCaster.getTarget() != null;
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
        Vec3 vec3d = spellCaster.getViewVector(1.0F);
        double x = spellCaster.getX();
        double y = spellCaster.getEyeY();
        double z = spellCaster.getZ();
        LargeFireball fireballEntity = new LargeFireball(spellCaster.level, spellCaster, x, y, z, 1);
        fireballEntity.absMoveTo(spellCaster.getX() + vec3d.x * 4.0D, spellCaster.getY(0.5D) + 0.5D, fireballEntity.getZ() + vec3d.z * 4.0D);
        double d = target.getX() - spellCaster.getX();
        double e = target.getY(0.3333333333333333D) - fireballEntity.getY();
        double f = target.getZ() - spellCaster.getZ();
        double g = Mth.sqrt((float) (d * d + f * f)); // TODO update for 1.17
        fireballEntity.shoot(d, e + g * 0.20000000298023224D, f, 1.6F, (float)(14 - spellCaster.level.getDifficulty().getId() * 4));
        spellCaster.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (spellCaster.getRandom().nextFloat() * 0.4F + 0.8F));
        spellCaster.level.addFreshEntity(fireballEntity);
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