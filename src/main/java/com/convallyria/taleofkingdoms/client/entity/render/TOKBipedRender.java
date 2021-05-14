package com.convallyria.taleofkingdoms.client.entity.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Environment(EnvType.CLIENT)
public class TOKBipedRender<T extends MobEntity, M extends BipedEntityModel<T>> extends BipedEntityRenderer<MobEntity, PlayerEntityModel<MobEntity>> {

    private final List<Identifier> skins;
    private final Map<UUID, Identifier> defaultSkin;

    public TOKBipedRender(EntityRenderDispatcher renderManagerIn, PlayerEntityModel<MobEntity> modelBipedIn,
                          float shadowSize, Identifier... skins) {
        super(renderManagerIn, modelBipedIn, shadowSize);
        this.skins = new ArrayList<>();
        this.defaultSkin = new ConcurrentHashMap<>();
        Collections.addAll(this.skins, skins);
    }

    @Override
    public Identifier getTexture(MobEntity entity) {
        Random random = ThreadLocalRandom.current();
        defaultSkin.putIfAbsent(entity.getUuid(), skins.get(random.nextInt(skins.size())));
        return defaultSkin.get(entity.getUuid());
    }
}
