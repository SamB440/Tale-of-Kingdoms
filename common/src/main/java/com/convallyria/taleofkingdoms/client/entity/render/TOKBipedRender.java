package com.convallyria.taleofkingdoms.client.entity.render;

import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class TOKBipedRender<T extends TOKEntity, M extends HumanoidModel<T>> extends HumanoidMobRenderer<TOKEntity, PlayerModel<TOKEntity>> {

    private final ResourceLocation defaultSkin;

    public TOKBipedRender(EntityRendererProvider.Context context, PlayerModel<TOKEntity> modelBipedIn,
                          float shadowSize, ResourceLocation defaultSkin) {
        super(context, modelBipedIn, shadowSize);
        this.defaultSkin = defaultSkin;
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(TOKEntity entity) {
        return entity.getSkin().orElse(defaultSkin);
    }
}
