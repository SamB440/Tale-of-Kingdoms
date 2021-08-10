package com.convallyria.taleofkingdoms.client.entity.render;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public record RenderSetup(TaleOfKingdoms mod) {

    public RenderSetup(TaleOfKingdoms mod) {
        this.mod = mod;
        setup();
    }

    private void setup() {
        register(EntityTypes.FARMER, new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/updated_textures/innkeeper.png"));
        register(EntityTypes.GUILDMASTER, new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/guildmaster.png"));
        register(EntityTypes.GUILDMASTER_DEFENDER, new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/guildmaster.png"));
        register(EntityTypes.BLACKSMITH, new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/updated_textures/smith.png"));
        register(EntityTypes.CITYBUILDER, new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/updated_textures/builder.png"));
        register(EntityTypes.KNIGHT, new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/knight2.png"));
        register(EntityTypes.INNKEEPER, new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/updated_textures/innkeeper.png"));
        register(EntityTypes.HUNTER, new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/hunter2.png"));
        register(EntityTypes.GUILDGUARD, new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/guildmember.png"));
        register(EntityTypes.GUILDARCHER,
                new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/updated_textures/guildarcherone.png"),
                new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/updated_textures/guildarchertwo.png"));
        register(EntityTypes.BANKER, new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/updated_textures/banker.png"));
        register(EntityTypes.LONE, new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/lone.png"));
        register(EntityTypes.FOODSHOP, new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/updated_textures/foodshop.png"));
        register(EntityTypes.GUILDCAPTAIN, new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/guildcaptain.png"));
        register(EntityTypes.LONEVILLAGER,
                new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/updated_textures/lostvillagerone.png"),
                new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/updated_textures/lostvillagertwo.png"),
                new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/updated_textures/lostvillagerthree.png"),
                new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/updated_textures/lostvillagerfour.png"),
                new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/updated_textures/lostvillagerfive.png"),
                new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/updated_textures/lostvillagersix.png"),
                new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/updated_textures/lostvillagerseven.png"),
                new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/updated_textures/manone.png"),
                new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/updated_textures/mantwo.png"),
                new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/updated_textures/manfive.png"));
        register(EntityTypes.REFICULE_SOLDIER, new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/updated_textures/reficulesoldier.png"));
        register(EntityTypes.REFICULE_GUARDIAN, new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/updated_textures/reficuleguardian.png"));
        register(EntityTypes.REFICULE_MAGE, new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/updated_textures/reficulemage.png"));
    }

    private void register(EntityType type, ResourceLocation... skins) {
        Minecraft.getInstance()();
        if (type == EntityTypes.REFICULE_MAGE) {
            EntityRendererRegistry.INSTANCE.register(type, (context) ->
                    new ReficuleMageEntityRenderer(context,
                            new Pla<>(context.getPart(EntityModelLayers.PLAYER), false)));
            return;
        }

        EntityRendererRegistry.INSTANCE.register(type, (context) ->
                new TOKBipedRender<MobEntity, PlayerEntityModel<MobEntity>>(context,
                        new PlayerEntityModel<>(context.getPart(EntityModelLayers.PLAYER), false),
                        0.5F,
                        skins));
    }
}
