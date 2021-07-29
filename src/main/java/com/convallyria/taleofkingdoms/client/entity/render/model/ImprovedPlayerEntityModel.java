package com.convallyria.taleofkingdoms.client.entity.render.model;

import com.convallyria.taleofkingdoms.common.entity.States;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class ImprovedPlayerEntityModel<T extends LivingEntity> extends PlayerEntityModel<T> {

    public ImprovedPlayerEntityModel(ModelPart modelPart, boolean bl) {
        super(modelPart, bl);
    }

    @Override
    public void setAngles(T livingEntity, float f, float g, float h, float i, float j) {
        super.setAngles(livingEntity, f, g, h, i, j);
        if (livingEntity instanceof States states) {
            States.State state = states.getState();
            if (state == States.State.ATTACKING) {
                if (livingEntity.getMainHandStack().isEmpty()) {
                    CrossbowPosing.meleeAttack(this.leftArm, this.rightArm, true, this.handSwingProgress, h);
                } else {
                    CrossbowPosing.meleeAttack(this.rightArm, this.leftArm, (MobEntity) livingEntity, this.handSwingProgress, h);
                }
            } else if (state == States.State.SPELLCASTING) {
                this.rightArm.pivotZ = 0.0F;
                this.rightArm.pivotX = -5.0F;
                this.leftArm.pivotZ = 0.0F;
                this.leftArm.pivotX = 5.0F;
                this.rightArm.pitch = MathHelper.cos(h * 0.6662F) * 0.25F;
                this.leftArm.pitch = MathHelper.cos(h * 0.6662F) * 0.25F;
                this.rightArm.roll = 2.3561945F;
                this.leftArm.roll = -2.3561945F;
                this.rightArm.yaw = 0.0F;
                this.leftArm.yaw = 0.0F;
            } else if (state == States.State.BOW_AND_ARROW) {
                this.rightArm.yaw = -0.1F + this.head.yaw;
                this.rightArm.pitch = -1.5707964F + this.head.pitch;
                this.leftArm.pitch = -0.9424779F + this.head.pitch;
                this.leftArm.yaw = this.head.yaw - 0.4F;
                this.leftArm.roll = 1.5707964F;
            } else if (state == States.State.CROSSBOW_HOLD) {
                CrossbowPosing.hold(this.rightArm, this.leftArm, this.head, true);
            } else if (state == States.State.CROSSBOW_CHARGE) {
                CrossbowPosing.charge(this.rightArm, this.leftArm, livingEntity, true);
            } else if (state == States.State.CELEBRATING) {
                this.rightArm.pivotZ = 0.0F;
                this.rightArm.pivotX = -5.0F;
                this.rightArm.pitch = MathHelper.cos(h * 0.6662F) * 0.05F;
                this.rightArm.roll = 2.670354F;
                this.rightArm.yaw = 0.0F;
                this.leftArm.pivotZ = 0.0F;
                this.leftArm.pivotX = 5.0F;
                this.leftArm.pitch = MathHelper.cos(h * 0.6662F) * 0.05F;
                this.leftArm.roll = -2.3561945F;
                this.leftArm.yaw = 0.0F;
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
