package com.convallyria.taleofkingdoms.client.entity.render.animal;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.entity.model.BoarEntityModel;
import com.convallyria.taleofkingdoms.common.entity.nature.BoarEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BoarEntityRenderer extends MobEntityRenderer<BoarEntity, BoarEntityModel<BoarEntity>> {

    private static final Identifier TEXTURE = new Identifier(TaleOfKingdoms.MODID, "textures/entity/boar/boar.png");

    public BoarEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new BoarEntityModel<>(), 0.7F);
    }

    public Identifier getTexture(BoarEntity boarEntity) {
        return TEXTURE;
    }
}