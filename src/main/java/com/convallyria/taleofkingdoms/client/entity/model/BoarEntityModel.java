package com.convallyria.taleofkingdoms.client.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class BoarEntityModel<T extends Entity> extends EntityModel<T> {

    private final ModelPart body;
    private final ModelPart rotation;
    private final ModelPart body_sub_1;
    private final ModelPart head;
    private final ModelPart mouth;
    private final ModelPart bone;
    private final ModelPart leg1;
    private final ModelPart bone3;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart spine_fluff;
    private final ModelPart ramp_r1;
    private final ModelPart head_fluff;
    private final ModelPart toplegs;

    public BoarEntityModel() {
        textureWidth = 64;
        textureHeight = 64;
        body = new ModelPart(this);
        body.setPivot(0.0F, 11.0F, 2.0F);

        rotation = new ModelPart(this);
        rotation.setPivot(0.0F, 0.0F, 0.0F);
        body.addChild(rotation);
        setRotationAngle(rotation, 1.5708F, 0.0F, 0.0F);

        body_sub_1 = new ModelPart(this);
        body_sub_1.setPivot(0.0F, 0.0F, 0.0F);
        rotation.addChild(body_sub_1);
        body_sub_1.setTextureOffset(0, 0).addCuboid(-5.0F, -10.25F, -8.0F, 10.0F, 17.0F, 9.0F, 0.0F, false);

        head = new ModelPart(this);
        head.setPivot(0.0F, 10.25F, -6.0F);
        head.setTextureOffset(0, 26).addCuboid(-4.0F, -1.75F, -8.0F, 8.0F, 9.0F, 8.0F, 0.0F, false);
        head.setTextureOffset(29, 0).addCuboid(-2.25F, 1.0F, -12.5F, 4.0F, 4.0F, 5.0F, 0.0F, false);

        mouth = new ModelPart(this);
        mouth.setPivot(0.0F, 13.75F, 6.0F);
        head.addChild(mouth);
        mouth.setTextureOffset(32, 33).addCuboid(0.5F, -10.45F, -17.75F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        mouth.setTextureOffset(32, 33).addCuboid(-3.0F, -10.45F, -17.75F, 2.0F, 2.0F, 1.0F, 0.0F, true);
        mouth.setTextureOffset(24, 28).addCuboid(-1.75F, -9.5F, -18.25F, 3.0F, 1.0F, 4.0F, 0.0F, false);

        bone = new ModelPart(this);
        bone.setPivot(22.4F, 24.725F, -39.95F);
        head.addChild(bone);

        head_fluff = new ModelPart(this);
        head_fluff.setPivot(0.0F, 13.75F, 6.0F);
        head.addChild(head_fluff);
        head_fluff.setTextureOffset(32, 28).addCuboid(-1.0F, -17.5F, -13.75F, 2.0F, 2.0F, 8.0F, 0.0F, false);

        leg1 = new ModelPart(this);
        leg1.setPivot(3.0F, 18.0F, 7.0F);
        leg1.setTextureOffset(8, 43).addCuboid(0.0F, -5.0F, -0.25F, 2.0F, 11.0F, 2.0F, 0.0F, false);
        leg1.setTextureOffset(34, 30).addCuboid(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        bone3 = new ModelPart(this);
        bone3.setPivot(0.0F, 0.0F, 0.0F);
        leg1.addChild(bone3);
        bone3.setTextureOffset(24, 30).addCuboid(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        leg2 = new ModelPart(this);
        leg2.setPivot(-3.0F, 18.0F, 7.0F);
        leg2.setTextureOffset(0, 43).addCuboid(-2.0F, -5.0F, -0.5F, 2.0F, 11.0F, 2.0F, 0.0F, false);
        leg2.setTextureOffset(29, 2).addCuboid(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        leg3 = new ModelPart(this);
        leg3.setPivot(3.0F, 18.0F, -5.0F);
        leg3.setTextureOffset(24, 43).addCuboid(0.0F, -3.0F, -2.75F, 2.0F, 9.0F, 2.0F, 0.0F, false);

        leg4 = new ModelPart(this);
        leg4.setPivot(-3.0F, 18.0F, -5.0F);
        leg4.setTextureOffset(16, 43).addCuboid(-2.0F, -3.0F, -2.75F, 2.0F, 9.0F, 2.0F, 0.0F, false);

        spine_fluff = new ModelPart(this);
        spine_fluff.setPivot(0.0F, 24.0F, 0.0F);
        spine_fluff.setTextureOffset(25, 13).addCuboid(-1.0F, -15.75F, -6.0F, 2.0F, 2.0F, 13.0F, 0.0F, false);
        spine_fluff.setTextureOffset(0, 0).addCuboid(-1.0F, -16.25F, -6.0F, 2.0F, 1.0F, 13.0F, -0.1F, false);

        ramp_r1 = new ModelPart(this);
        ramp_r1.setPivot(0.0F, 0.0F, 0.0F);
        spine_fluff.addChild(ramp_r1);
        setRotationAngle(ramp_r1, -0.4363F, 0.0F, 0.0F);
        ramp_r1.setTextureOffset(0, 0).addCuboid(-1.0F, -13.5F, -13.0F, 2.0F, 2.0F, 3.0F, -0.2F, false);

        toplegs = new ModelPart(this);
        toplegs.setPivot(0.0F, 24.0F, 0.0F);
        toplegs.setTextureOffset(32, 38).addCuboid(-5.75F, -10.5F, 5.0F, 4.0F, 5.0F, 4.0F, 0.0F, false);
        toplegs.setTextureOffset(42, 9).addCuboid(1.75F, -10.5F, 6.0F, 4.0F, 5.0F, 3.0F, 0.0F, false);
        toplegs.setTextureOffset(0, 26).addCuboid(4.75F, -10.5F, -8.25F, 1.0F, 5.0F, 3.0F, 0.0F, false);
        toplegs.setTextureOffset(0, 0).addCuboid(-5.75F, -10.5F, -8.25F, 1.0F, 5.0F, 3.0F, 0.0F, false);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.head.pitch = headPitch * 0.017453292F;
        this.head.yaw = headYaw * 0.017453292F;
        this.leg2.pitch = MathHelper.cos(limbAngle * 0.5662F) * 1.4F * limbDistance;
        this.leg1.pitch = MathHelper.cos(limbAngle * 0.5662F + 3.1415927F) * 1.4F * limbDistance;
        this.leg4.pitch = MathHelper.cos(limbAngle * 0.5662F + 3.1415927F) * 1.4F * limbDistance;
        this.leg3.pitch = MathHelper.cos(limbAngle * 0.5662F) * 1.4F * limbDistance;
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body.render(matrixStack, buffer, packedLight, packedOverlay);
        head.render(matrixStack, buffer, packedLight, packedOverlay);
        leg1.render(matrixStack, buffer, packedLight, packedOverlay);
        leg2.render(matrixStack, buffer, packedLight, packedOverlay);
        leg3.render(matrixStack, buffer, packedLight, packedOverlay);
        leg4.render(matrixStack, buffer, packedLight, packedOverlay);
        spine_fluff.render(matrixStack, buffer, packedLight, packedOverlay);
        toplegs.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }
}