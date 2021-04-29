package com.convallyria.taleofkingdoms.client.entity.model;

import com.convallyria.taleofkingdoms.common.entity.nature.hostile.RatEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class RatEntityModel<T extends RatEntity> extends EntityModel<T> {
    
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart backRightLeg;
    private final ModelPart backLeftLeg;
    private final ModelPart frontRightLeg;
    private final ModelPart frontLeftLeg;
    private final ModelPart whiskers;
    
    public RatEntityModel() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        
        this.head = new ModelPart(this);
        head.setPivot(0.0F, 24.0F, 0.0F);
        head.setTextureOffset(0, 25).addCuboid(-1.0F, -5.0F, -10.0F, 2.0F, 3.0F, 5.0F, 0.0F, false);
        head.setTextureOffset(0, 14).addCuboid(-2.5F, -7.0F, -7.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        head.setTextureOffset(0, 0).addCuboid(0.5F, -7.0F, -7.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        
        this.body = new ModelPart(this);
        body.setPivot(0.0F, 24.0F, 0.0F);
        body.setTextureOffset(0, 0).addCuboid(-4.5F, -8.0F, 0.0F, 9.0F, 7.0F, 7.0F, 0.0F, false);
        body.setTextureOffset(0, 14).addCuboid(-3.5F, -6.0F, -6.0F, 7.0F, 5.0F, 6.0F, 0.0F, false);
        body.setTextureOffset(16, 15).addCuboid(-0.5F, -4.0F, 3.0F, 1.0F, 1.0F, 10.0F, 0.0F, false);
    
        this.backRightLeg = new ModelPart(this);
        backRightLeg.setPivot(0.0F, 24.0F, 0.0F);
        backRightLeg.setTextureOffset(14, 26).addCuboid(-4.0F, -1.0F, 1.0F, 2.0F, 1.0F, 4.0F, 0.0F, false);
    
        this.backLeftLeg = new ModelPart(this);
        backLeftLeg.setPivot(0.0F, 24.0F, 0.0F);
        backLeftLeg.setTextureOffset(25, 0).addCuboid(2.0F, -1.0F, 1.0F, 2.0F, 1.0F, 4.0F, 0.0F, false);
    
        this.frontRightLeg = new ModelPart(this);
        frontRightLeg.setPivot(0.0F, 24.0F, 0.0F);
        frontRightLeg.setTextureOffset(26, 26).addCuboid(-3.5F, -1.0F, -7.0F, 2.0F, 1.0F, 4.0F, 0.0F, false);
    
        this.frontLeftLeg = new ModelPart(this);
        frontLeftLeg.setPivot(0.0F, 24.0F, 0.0F);
        frontLeftLeg.setTextureOffset(28, 10).addCuboid(1.5F, -1.0F, -7.0F, 2.0F, 1.0F, 4.0F, 0.0F, false);
        
        this.whiskers = new ModelPart(this);
        whiskers.setPivot(0.0F, 24.0F, 0.0F);
        whiskers.setTextureOffset(4, 6).addCuboid(2.0F, -3.25F, -10.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);
        whiskers.setTextureOffset(2, 6).addCuboid(1.0F, -4.25F, -10.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);
        whiskers.setTextureOffset(0, 6).addCuboid(3.0F, -3.25F, -10.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);
        whiskers.setTextureOffset(4, 5).addCuboid(2.0F, -5.25F, -10.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);
        whiskers.setTextureOffset(2, 5).addCuboid(-2.0F, -4.25F, -10.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);
        whiskers.setTextureOffset(5, 0).addCuboid(-4.0F, -3.25F, -10.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);
        whiskers.setTextureOffset(0, 5).addCuboid(-3.0F, -3.25F, -10.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);
        whiskers.setTextureOffset(4, 4).addCuboid(-4.0F, -5.25F, -10.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);
        whiskers.setTextureOffset(2, 4).addCuboid(-3.0F, -5.25F, -10.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);
        whiskers.setTextureOffset(0, 4).addCuboid(3.0F, -5.25F, -10.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);
    }
    
    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.head.pitch = headPitch * 0.001F;
        this.head.yaw = headYaw * 0.001F;
        this.whiskers.pitch = headPitch * 0.001F;
        this.whiskers.yaw = headYaw * 0.001F;
        this.backRightLeg.pitch = MathHelper.cos(limbAngle * 0.5662F) * 1.4F * limbDistance;
        this.backLeftLeg.pitch = MathHelper.cos(limbAngle * 0.5662F + 3.1415927F) * 1.4F * limbDistance;
        this.frontRightLeg.pitch = MathHelper.cos(limbAngle * 0.5662F + 3.1415927F) * 1.4F * limbDistance;
        this.frontLeftLeg.pitch = MathHelper.cos(limbAngle * 0.5662F) * 1.4F * limbDistance;
    }
    
    @Override
    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(matrixStack, buffer, packedLight, packedOverlay);
        body.render(matrixStack, buffer, packedLight, packedOverlay);
        backRightLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        backLeftLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        frontRightLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        frontLeftLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        whiskers.render(matrixStack, buffer, packedLight, packedOverlay);
    }
    
    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }
}