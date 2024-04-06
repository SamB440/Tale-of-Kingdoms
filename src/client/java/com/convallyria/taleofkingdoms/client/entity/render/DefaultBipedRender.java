package com.convallyria.taleofkingdoms.client.entity.render;

import com.convallyria.taleofkingdoms.common.entity.MultiSkinned;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class DefaultBipedRender<T extends MobEntity & MultiSkinned, M extends BipedEntityModel<T>> extends BipedEntityRenderer<T, PlayerEntityModel<T>> {

    private final Identifier defaultSkin;

    public DefaultBipedRender(EntityRendererFactory.Context context, PlayerEntityModel<T> modelBipedIn,
                              float shadowSize, Identifier defaultSkin) {
        super(context, modelBipedIn, shadowSize);
        this.defaultSkin = defaultSkin;
        this.addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
    }

    @Override
    public Identifier getTexture(MobEntity entity) {
        MultiSkinned multiSkinned = (MultiSkinned) entity;
        return multiSkinned.getSkin().orElse(defaultSkin);
    }
}
