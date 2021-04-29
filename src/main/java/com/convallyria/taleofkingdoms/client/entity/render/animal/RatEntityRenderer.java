package com.convallyria.taleofkingdoms.client.entity.render.animal;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.entity.model.RatEntityModel;
import com.convallyria.taleofkingdoms.common.entity.nature.hostile.RatEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class RatEntityRenderer extends MobEntityRenderer<RatEntity, RatEntityModel<RatEntity>> {

    private static final Identifier TEXTURE = new Identifier(TaleOfKingdoms.MODID, "textures/entity/rat/rat.png");

    public RatEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new RatEntityModel<>(), 0.7F);
    }

    @Override
    public Identifier getTexture(RatEntity boarEntity) {
        return TEXTURE;
    }
}