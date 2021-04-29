package com.convallyria.taleofkingdoms.client.entity.render;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.entity.render.animal.BoarEntityRenderer;
import com.convallyria.taleofkingdoms.client.entity.render.animal.RatEntityRenderer;
import com.convallyria.taleofkingdoms.client.entity.render.reficule.ReficuleMageEntityRenderer;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;

public final class RenderSetup {

    private final TaleOfKingdoms mod;

    public RenderSetup(TaleOfKingdoms mod) {
        this.mod = mod;
        setup();
    }

    private void setup() {
        register(EntityTypes.FARMER, new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/innkeeper.png"));
        register(EntityTypes.GUILDMASTER, new Identifier(TaleOfKingdoms.MODID, "textures/entity/guildmaster.png"));
        register(EntityTypes.BLACKSMITH, new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/smith.png"));
        register(EntityTypes.CITYBUILDER, new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/builder.png"));
        register(EntityTypes.KNIGHT, new Identifier(TaleOfKingdoms.MODID, "textures/entity/knight2.png"));
        register(EntityTypes.INNKEEPER, new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/innkeeper.png"));
        register(EntityTypes.HUNTER, new Identifier(TaleOfKingdoms.MODID, "textures/entity/hunter2.png"));
        register(EntityTypes.GUILDGUARD, new Identifier(TaleOfKingdoms.MODID, "textures/entity/guildmember.png"));
        register(EntityTypes.GUILDARCHER,
                new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/guildarcherone.png"),
                new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/guildarchertwo.png"));
        register(EntityTypes.BANKER, new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/banker.png"));
        register(EntityTypes.LONE, new Identifier(TaleOfKingdoms.MODID, "textures/entity/lone.png"));
        register(EntityTypes.FOODSHOP, new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/foodshop.png"));
        register(EntityTypes.GUILDCAPTAIN, new Identifier(TaleOfKingdoms.MODID, "textures/entity/guildcaptain.png"));
        register(EntityTypes.LONEVILLAGER,
                new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/lostvillagerone.png"),
                new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/lostvillagertwo.png"),
                new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/lostvillagerthree.png"),
                new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/lostvillagerfour.png"),
                new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/lostvillagerfive.png"),
                new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/lostvillagersix.png"),
                new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/lostvillagerseven.png"),
                new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/manone.png"),
                new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/mantwo.png"),
                new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/manfive.png"));
        register(EntityTypes.REFICULE_SOLDIER, new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/reficulesoldier.png"));
        register(EntityTypes.REFICULE_GUARDIAN, new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/reficuleguardian.png"));
        register(EntityTypes.REFICULE_MAGE, new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/reficulemage.png"));

        EntityRendererRegistry.INSTANCE.register(EntityTypes.BOAR, (dispatcher, context) -> new BoarEntityRenderer(dispatcher));
        EntityRendererRegistry.INSTANCE.register(EntityTypes.RAT, (dispatcher, context) -> new RatEntityRenderer(dispatcher));
    }

    private void register(EntityType<?> type, Identifier... skins) {
        if (type == EntityTypes.REFICULE_MAGE) {
            EntityRendererRegistry.INSTANCE.register(type, (dispatcher, context) ->
                    new ReficuleMageEntityRenderer<>(dispatcher,
                            new PlayerEntityModel<>(0.0F, false)));
            return;
        }

        EntityRendererRegistry.INSTANCE.register(type, (dispatcher, context) ->
                new TOKBipedRender<MobEntity, PlayerEntityModel<MobEntity>>(dispatcher,
                        new PlayerEntityModel<>(0.0F, false),
                        0.5F,
                        skins));
    }
}
