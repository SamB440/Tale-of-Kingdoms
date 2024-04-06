package com.convallyria.taleofkingdoms.client.entity.render;

import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class TOKBipedRender<T extends TOKEntity, M extends BipedEntityModel<T>> extends BipedEntityRenderer<TOKEntity, PlayerEntityModel<TOKEntity>> {

    private final Identifier defaultSkin;

    public TOKBipedRender(EntityRendererFactory.Context context, PlayerEntityModel<TOKEntity> modelBipedIn,
                          float shadowSize, Identifier defaultSkin) {
        super(context, modelBipedIn, shadowSize);
        this.defaultSkin = defaultSkin;
        this.addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
    }

    @Override
    public Identifier getTexture(TOKEntity entity) {
        return entity.getSkin().orElse(defaultSkin);
    }
}
