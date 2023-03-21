package com.convallyria.taleofkingdoms.client.entity.render;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;

public record RenderSetup(TaleOfKingdoms mod) {

    public RenderSetup(TaleOfKingdoms mod) {
        this.mod = mod;
        setup();
    }

    private void setup() {
        register(EntityTypes.FARMER, identifier("textures/entity/updated_textures/innkeeper.png"));
        register(EntityTypes.GUILDMASTER, identifier("textures/entity/guildmaster.png"));
        register(EntityTypes.GUILDMASTER_DEFENDER, identifier("textures/entity/guildmaster.png"));
        register(EntityTypes.BLACKSMITH, identifier("textures/entity/updated_textures/smith.png"));
        register(EntityTypes.CITYBUILDER, identifier("textures/entity/builder2.png"));
        register(EntityTypes.KNIGHT, identifier("textures/entity/knight2.png"));
        register(EntityTypes.INNKEEPER, identifier("textures/entity/updated_textures/innkeeper.png"));
        register(EntityTypes.HUNTER, identifier("textures/entity/hunter2.png"));
        register(EntityTypes.GUILDGUARD, identifier("textures/entity/guildmember.png"));
        register(EntityTypes.GUILDVILLAGER, identifier("textures/entity/guildmember.png"));
        register(EntityTypes.GUILDARCHER,
                /*identifier("textures/entity/guildarcher/guildarcherone.png"),*/
                identifier("textures/entity/guildarcher/guildarchertwo.png")
                /*identifier("textures/entity/guildarcher/guildarcherthree.png")*/);
        register(EntityTypes.BANKER, identifier("textures/entity/updated_textures/banker.png"));
        register(EntityTypes.LONE, identifier("textures/entity/lone.png"));
        register(EntityTypes.FOODSHOP, identifier("textures/entity/updated_textures/foodshop.png"));
        register(EntityTypes.GUILDCAPTAIN, identifier("textures/entity/guildcaptain.png"));
        register(EntityTypes.LONEVILLAGER,
                identifier("textures/entity/updated_textures/lostvillagerone.png"),
                identifier("textures/entity/updated_textures/lostvillagertwo.png"),
                identifier("textures/entity/updated_textures/lostvillagerthree.png"),
                identifier("textures/entity/updated_textures/lostvillagerfour.png"),
                identifier("textures/entity/updated_textures/lostvillagerfive.png"),
                identifier("textures/entity/updated_textures/lostvillagersix.png"),
                identifier("textures/entity/updated_textures/lostvillagerseven.png"),
                identifier("textures/entity/updated_textures/manone.png"),
                identifier("textures/entity/updated_textures/mantwo.png"),
                identifier("textures/entity/updated_textures/manfive.png"));
        register(EntityTypes.REFICULE_SOLDIER, identifier("textures/entity/updated_textures/reficulesoldier.png"));
        register(EntityTypes.REFICULE_GUARDIAN, identifier("textures/entity/updated_textures/reficuleguardian.png"));
        register(EntityTypes.REFICULE_MAGE, identifier("textures/entity/updated_textures/reficulemage.png"));

        // Player's kingdom entities
        register(EntityTypes.ITEM_SHOP, identifier("textures/entity/updated_textures/shopkeeper.png"));
        register(EntityTypes.KINGDOM_VILLAGER,
                identifier("textures/entity/updated_textures/woman1.png"),
                identifier("textures/entity/updated_textures/manone.png"),
                identifier("textures/entity/updated_textures/mantwo.png"),
                identifier("textures/entity/updated_textures/manthree.png"),
                identifier("textures/entity/updated_textures/manfour.png"),
                identifier("textures/entity/updated_textures/manfive.png"),
                identifier("textures/entity/updated_textures/mansix.png"));
        register(EntityTypes.STOCK_MARKET, identifier("textures/entity/updated_textures/stock.png"));
        register(EntityTypes.QUARRY_FOREMAN, identifier("textures/entity/updated_textures/foremanquarry.png"));
        register(EntityTypes.LUMBER_FOREMAN, identifier("textures/entity/updated_textures/foremanlumber.png"));
        register(EntityTypes.QUARRY_WORKER, identifier("textures/entity/updated_textures/worker.png"));
    }

    private void register(EntityType<? extends MobEntity> type, Identifier... skins) {
        if (type == EntityTypes.REFICULE_MAGE) {
            EntityRendererRegistry.register(type, (context) ->
                    new ReficuleMageEntityRenderer(context,
                            new PlayerEntityModel<>(context.getPart(EntityModelLayers.PLAYER), false)));
            return;
        }

        EntityRendererRegistry.register(type, (context) ->
                new TOKBipedRender<MobEntity, PlayerEntityModel<MobEntity>>(context,
                        new PlayerEntityModel<>(context.getPart(EntityModelLayers.PLAYER), false),
                        0.5F,
                        skins));
    }
    
    private static Identifier identifier(String path) {
        return new Identifier(TaleOfKingdoms.MODID, path);
    }
}
