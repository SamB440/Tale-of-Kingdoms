package com.convallyria.taleofkingdoms.client.entity.render;

import com.convallyria.taleofkingdoms.common.entity.States;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

@Environment(EnvType.CLIENT)
public class ImprovedPlayerEntityModel<T extends LivingEntity> extends PlayerModel<T> {

    public ImprovedPlayerEntityModel(ModelPart modelPart, boolean bl) {
        super(modelPart, bl);
    }

    @Override
    public void setupAnim(T livingEntity, float f, float g, float h, float i, float j) {
        super.setupAnim(livingEntity, f, g, h, i, j);
        if (livingEntity instanceof States states) {
            States.State state = states.getState();
            if (state == States.State.ATTACKING) {
                if (livingEntity.getMainHandItem().isEmpty()) {
                    AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, true, this.attackTime, h);
                } else {
                    AnimationUtils.swingWeaponDown(this.rightArm, this.leftArm, (Mob) livingEntity, this.attackTime, h);
                }
            } else if (state == States.State.SPELLCASTING) {
                this.rightArm.z = 0.0F;
                this.rightArm.x = -5.0F;
                this.leftArm.z = 0.0F;
                this.leftArm.x = 5.0F;
                this.rightArm.xRot = Mth.cos(h * 0.6662F) * 0.25F;
                this.leftArm.xRot = Mth.cos(h * 0.6662F) * 0.25F;
                this.rightArm.zRot = 2.3561945F;
                this.leftArm.zRot = -2.3561945F;
                this.rightArm.yRot = 0.0F;
                this.leftArm.yRot = 0.0F;
            } else if (state == States.State.BOW_AND_ARROW) {
                this.rightArm.yRot = -0.1F + this.head.yRot;
                this.rightArm.xRot = -1.5707964F + this.head.xRot;
                this.leftArm.xRot = -0.9424779F + this.head.xRot;
                this.leftArm.yRot = this.head.yRot - 0.4F;
                this.leftArm.zRot = 1.5707964F;
            } else if (state == States.State.CROSSBOW_HOLD) {
                AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
            } else if (state == States.State.CROSSBOW_CHARGE) {
                AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, livingEntity, true);
            } else if (state == States.State.CELEBRATING) {
                this.rightArm.z = 0.0F;
                this.rightArm.x = -5.0F;
                this.rightArm.xRot = Mth.cos(h * 0.6662F) * 0.05F;
                this.rightArm.zRot = 2.670354F;
                this.rightArm.yRot = 0.0F;
                this.leftArm.z = 0.0F;
                this.leftArm.x = 5.0F;
                this.leftArm.xRot = Mth.cos(h * 0.6662F) * 0.05F;
                this.leftArm.zRot = -2.3561945F;
                this.leftArm.yRot = 0.0F;
            }

            boolean bl = state == States.State.CROSSED;
            //this.arms.visible = bl;
            this.rightSleeve.visible = bl;
            this.leftSleeve.visible = bl;
            this.leftArm.visible = !bl;
            this.rightArm.visible = !bl;
        }
    }
}