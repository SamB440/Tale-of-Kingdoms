package com.convallyria.taleofkingdoms.client.entity.render;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.reficule.ReficuleMageEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.IllusionerEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.IllusionerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class ReficuleMageEntityRenderer<T extends ReficuleMageEntity> extends MobEntityRenderer<ReficuleMageEntity, PlayerEntityModel<ReficuleMageEntity>> {

    private static final Identifier TEXTURE = new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/reficulemage.png");

    public ReficuleMageEntityRenderer(EntityRenderDispatcher entityRenderDispatcher, PlayerEntityModel<ReficuleMageEntity> modelBipedIn) {
        super(entityRenderDispatcher, modelBipedIn, 0.5F);
        this.addFeature(new HeadFeatureRenderer<>(this));
        this.addFeature(new HeldItemFeatureRenderer<ReficuleMageEntity, PlayerEntityModel<ReficuleMageEntity>>(this) {
            public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, ReficuleMageEntity reficuleMageEntity, float f, float g, float h, float j, float k, float l) {
                if (reficuleMageEntity.isSpellcasting() || reficuleMageEntity.isAttacking()) {
                    super.render(matrixStack, vertexConsumerProvider, i, reficuleMageEntity, f, g, h, j, k, l);
                }

            }
        });
    }

    @Override
    public Identifier getTexture(ReficuleMageEntity reficuleMageEntity) {
        return TEXTURE;
    }

    @Override
    public void render(ReficuleMageEntity reficuleMageEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if (reficuleMageEntity.isInvisible()) {
            Vec3d[] vec3ds = reficuleMageEntity.method_7065(g);
            float h = this.getAnimationProgress(reficuleMageEntity, g);

            for(int j = 0; j < vec3ds.length; ++j) {
                matrixStack.push();
                matrixStack.translate(vec3ds[j].x + (double) MathHelper.cos((float)j + h * 0.5F) * 0.025D, vec3ds[j].y + (double)MathHelper.cos((float)j + h * 0.75F) * 0.0125D, vec3ds[j].z + (double)MathHelper.cos((float)j + h * 0.7F) * 0.025D);
                super.render(reficuleMageEntity, f, g, matrixStack, vertexConsumerProvider, i);
                matrixStack.pop();
            }
        } else {
            super.render(reficuleMageEntity, f, g, matrixStack, vertexConsumerProvider, i);
        }
    }

    @Override
    protected boolean isVisible(ReficuleMageEntity reficuleMageEntity) {
        return true;
    }

    @Override
    protected void scale(ReficuleMageEntity illagerEntity, MatrixStack matrixStack, float f) {
        matrixStack.scale(0.9375F, 0.9375F, 0.9375F);
    }
}
