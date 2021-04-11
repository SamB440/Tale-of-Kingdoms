package com.convallyria.taleofkingdoms.common.entity.ai.goal.spell;

import com.convallyria.taleofkingdoms.common.entity.generic.SpellcastingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class FireballSpellGoal extends CastSpellGoal {

    private final SpellcastingEntity spellCaster;
    private int targetId;

    public FireballSpellGoal(SpellcastingEntity spellCaster) {
        super(spellCaster);
        this.spellCaster = spellCaster;
    }

    @Override
    public boolean canStart() {
        if (!super.canStart()) {
            return false;
        } else return spellCaster.getTarget() != null;
    }

    @Override
    public void start() {
        super.start();
        this.targetId = spellCaster.getTarget().getEntityId();
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
        Vec3d vec3d = spellCaster.getRotationVec(1.0F);
        double x = spellCaster.getX();
        double y = spellCaster.getEyeY();
        double z = spellCaster.getZ();
        FireballEntity fireballEntity = new FireballEntity(spellCaster.world, spellCaster, x, y, z);
        fireballEntity.explosionPower = 1;
        fireballEntity.updatePosition(spellCaster.getX() + vec3d.x * 4.0D, spellCaster.getBodyY(0.5D) + 0.5D, fireballEntity.getZ() + vec3d.z * 4.0D);
        double d = target.getX() - spellCaster.getX();
        double e = target.getBodyY(0.3333333333333333D) - fireballEntity.getY();
        double f = target.getZ() - spellCaster.getZ();
        double g = MathHelper.sqrt(d * d + f * f);
        fireballEntity.setVelocity(d, e + g * 0.20000000298023224D, f, 1.6F, (float)(14 - spellCaster.world.getDifficulty().getId() * 4));
        spellCaster.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (spellCaster.getRandom().nextFloat() * 0.4F + 0.8F));
        spellCaster.world.spawnEntity(fireballEntity);
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