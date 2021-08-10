package com.convallyria.taleofkingdoms.client.entity.render;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@OnlyIn(Dist.CLIENT)
public class TOKBipedRender<T extends Mob, M extends HumanoidModel<T>> extends HumanoidMobRenderer<Mob, PlayerModel<Mob>> {

    private final List<ResourceLocation> skins;
    private final Map<UUID, ResourceLocation> defaultSkin;

    public TOKBipedRender(EntityRendererProvider.Context context, PlayerModel<Mob> modelBipedIn,
                          float shadowSize, ResourceLocation... skins) {
        super(context, modelBipedIn, shadowSize);
        this.skins = new ArrayList<>();
        this.defaultSkin = new ConcurrentHashMap<>();
        Collections.addAll(this.skins, skins);
    }

    @Override
    public ResourceLocation getTextureLocation(Mob entity) {
        Random random = ThreadLocalRandom.current();
        defaultSkin.putIfAbsent(entity.getUUID(), skins.get(random.nextInt(skins.size())));
        return defaultSkin.get(entity.getUUID());
    }
}
