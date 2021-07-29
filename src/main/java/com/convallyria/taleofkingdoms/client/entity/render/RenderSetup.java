package com.convallyria.taleofkingdoms.client.entity.render;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.entity.render.model.ImprovedPlayerEntityModel;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.generic.BanditEntity;
import com.convallyria.taleofkingdoms.common.entity.generic.ForemanEntity;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;

public record RenderSetup(TaleOfKingdoms mod) {

    public RenderSetup(TaleOfKingdoms mod) {
        this.mod = mod;
        setup();
    }

    private void setup() {
        register(EntityTypes.FARMER, new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/innkeeper.png"));
        register(EntityTypes.GUILDMASTER, new Identifier(TaleOfKingdoms.MODID, "textures/entity/guildmaster.png"));
        register(EntityTypes.GUILDMASTER_DEFENDER, new Identifier(TaleOfKingdoms.MODID, "textures/entity/guildmaster.png"));
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
        register(EntityTypes.BANDIT, BanditEntity.SKINS);
        register(EntityTypes.FOREMAN, ForemanEntity.SKINS);
    }

    private void register(EntityType type, Identifier... skins) {
        if (type == EntityTypes.REFICULE_MAGE) {
            EntityRendererRegistry.INSTANCE.register(type, (context) ->
                    new ReficuleMageEntityRenderer(context,
                            new ImprovedPlayerEntityModel<>(context.getPart(EntityModelLayers.PLAYER), false)));
            return;
        }

        EntityRendererRegistry.INSTANCE.register(type, (context) ->
                new TOKBipedRender<MobEntity, PlayerEntityModel<MobEntity>>(context,
                        new ImprovedPlayerEntityModel<>(context.getPart(EntityModelLayers.PLAYER), false),
                        0.5F,
                        skins));
    }
}
