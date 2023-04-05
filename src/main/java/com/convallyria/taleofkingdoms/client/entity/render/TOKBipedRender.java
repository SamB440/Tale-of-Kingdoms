package com.convallyria.taleofkingdoms.client.entity.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Environment(EnvType.CLIENT)
public class TOKBipedRender<T extends MobEntity, M extends BipedEntityModel<T>> extends BipedEntityRenderer<MobEntity, PlayerEntityModel<MobEntity>> {

    private final List<Identifier> skins;
    private final Map<UUID, Identifier> defaultSkin;

    public TOKBipedRender(EntityRendererFactory.Context context, PlayerEntityModel<MobEntity> modelBipedIn,
                          float shadowSize, Identifier... skins) {
        super(context, modelBipedIn, shadowSize);
        this.skins = new ArrayList<>();
        this.defaultSkin = new ConcurrentHashMap<>();
        Collections.addAll(this.skins, skins);
        this.addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
    }

    @Override
    public Identifier getTexture(MobEntity entity) {
        final Identifier identifier = defaultSkin.get(entity.getUuid());
        if (identifier == null) {
            Random random = ThreadLocalRandom.current();
            return defaultSkin.put(entity.getUuid(), skins.get(random.nextInt(skins.size())));
        }
        return identifier;
    }
}
