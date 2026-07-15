package com.convallyria.taleofkingdoms.client.entity.render;

import com.convallyria.taleofkingdoms.common.entity.MultiSkinned;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

@Environment(EnvType.CLIENT)
public class DefaultBipedRender<T extends Mob & MultiSkinned, M extends HumanoidModel<T>> extends HumanoidMobRenderer<T, PlayerModel<T>> {

    private final ResourceLocation defaultSkin;

    public DefaultBipedRender(EntityRendererProvider.Context context, PlayerModel<T> modelBipedIn,
                              float shadowSize, ResourceLocation defaultSkin) {
        super(context, modelBipedIn, shadowSize);
        this.defaultSkin = defaultSkin;
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(Mob entity) {
        MultiSkinned multiSkinned = (MultiSkinned) entity;
        return multiSkinned.getSkin().orElse(defaultSkin);
    }
}
