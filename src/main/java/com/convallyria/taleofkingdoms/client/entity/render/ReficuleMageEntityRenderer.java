package com.convallyria.taleofkingdoms.client.entity.render;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.reficule.ReficuleMageEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ReficuleMageEntityRenderer<T extends ReficuleMageEntity> extends MobRenderer<ReficuleMageEntity, PlayerModel<ReficuleMageEntity>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/updated_textures/reficulemage.png");

    public ReficuleMageEntityRenderer(EntityRendererProvider.Context context, PlayerModel<ReficuleMageEntity> modelBipedIn) {
        super(context, modelBipedIn, 0.5f);
        this.addLayer(new CustomHeadLayer(this, context.getModelSet()));
        this.addLayer(new ItemInHandLayer<>(this) {
            @Override
            public void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, ReficuleMageEntity reficuleMageEntity, float f, float g, float h, float j, float k, float l) {
                if (reficuleMageEntity.isSpellcasting() || reficuleMageEntity.isAggressive()) {
                    super.render(matrixStack, vertexConsumerProvider, i, reficuleMageEntity, f, g, h, j, k, l);
                }
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(ReficuleMageEntity reficuleMageEntity) {
        return TEXTURE;
    }

    @Override
    public void render(ReficuleMageEntity reficuleMageEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        if (reficuleMageEntity.isInvisible()) {
            Vec3[] vec3ds = reficuleMageEntity.method_7065(g);
            float h = this.getBob(reficuleMageEntity, g);

            for(int j = 0; j < vec3ds.length; ++j) {
                matrixStack.pushPose();
                matrixStack.translate(vec3ds[j].x + (double) Mth.cos((float)j + h * 0.5F) * 0.025D, vec3ds[j].y + (double)Mth.cos((float)j + h * 0.75F) * 0.0125D, vec3ds[j].z + (double)Mth.cos((float)j + h * 0.7F) * 0.025D);
                super.render(reficuleMageEntity, f, g, matrixStack, vertexConsumerProvider, i);
                matrixStack.popPose();
            }
        } else {
            super.render(reficuleMageEntity, f, g, matrixStack, vertexConsumerProvider, i);
        }
    }

    @Override
    protected boolean isBodyVisible(ReficuleMageEntity reficuleMageEntity) {
        return true;
    }

    @Override
    protected void scale(ReficuleMageEntity illagerEntity, PoseStack matrixStack, float f) {
        matrixStack.scale(0.9375F, 0.9375F, 0.9375F);
    }
}
