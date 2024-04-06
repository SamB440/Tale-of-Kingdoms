package com.convallyria.taleofkingdoms.client.entity.render;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.MultiSkinned;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
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
        register(EntityTypes.KNIGHT, identifier("textures/entity/updated_textures/knight.png"));
        register(EntityTypes.INNKEEPER, identifier("textures/entity/updated_textures/innkeeper.png"));
        register(EntityTypes.HUNTER, identifier("textures/entity/hunter2.png"));
        register(EntityTypes.GUILDGUARD, identifier("textures/entity/guildmember.png"));
        register(EntityTypes.GUILDVILLAGER, identifier("textures/entity/guildmember.png"));
        register(EntityTypes.GUILDARCHER, identifier("textures/entity/guildarcher/guildarchertwo.png"));
        register(EntityTypes.BANKER, identifier("textures/entity/updated_textures/banker.png"));
        register(EntityTypes.LONE, identifier("textures/entity/lone.png"));
        register(EntityTypes.FOODSHOP, identifier("textures/entity/updated_textures/foodshop.png"));
        register(EntityTypes.GUILDCAPTAIN, identifier("textures/entity/guildcaptain.png"));
        register(EntityTypes.LONEVILLAGER, identifier("textures/entity/updated_textures/lostvillagerone.png"));
        register(EntityTypes.REFICULE_SOLDIER, identifier("textures/entity/updated_textures/reficulesoldier.png"));
        register(EntityTypes.REFICULE_GUARDIAN, identifier("textures/entity/updated_textures/reficuleguardian.png"));
        EntityRendererRegistry.register(EntityTypes.REFICULE_MAGE, (context) ->
                new ReficuleMageEntityRenderer<>(context,
                        new ImprovedPlayerEntityModel<>(context.getPart(EntityModelLayers.PLAYER), false)));

        register(EntityTypes.BANDIT, identifier("textures/entity/bandit/archer_tok.png"));

        // Player's kingdom entities
        register(EntityTypes.ITEM_SHOP, identifier("textures/entity/updated_textures/shopkeeper.png"));
        register(EntityTypes.KINGDOM_VILLAGER, identifier("textures/entity/updated_textures/woman1.png"));
        register(EntityTypes.STOCK_MARKET, identifier("textures/entity/updated_textures/stock.png"));
        register(EntityTypes.QUARRY_FOREMAN, identifier("textures/entity/updated_textures/foremanquarry.png"));
        register(EntityTypes.LUMBER_FOREMAN, identifier("textures/entity/updated_textures/foremanlumber.png"));
        register(EntityTypes.QUARRY_WORKER, identifier("textures/entity/updated_textures/worker.png"));
        register(EntityTypes.LUMBER_WORKER, identifier("textures/entity/updated_textures/worker.png"));
        register(EntityTypes.WARDEN, identifier("textures/entity/updated_textures/warden.png"));
        register(EntityTypes.WARRIOR, identifier("textures/entity/updated_textures/warrior.png"));
        register(EntityTypes.ARCHER, identifier("textures/entity/updated_textures/archer_base.png"));
        register(EntityTypes.BLOCK_SHOP, identifier("textures/entity/updated_textures/blockshop.png"));
        registerDefault(EntityTypes.HUMAN_FARMER, identifier("textures/entity/updated_textures/tok_farmer.png"));
    }

    private void register(EntityType<? extends TOKEntity> type, Identifier defaultSkin) {
        EntityRendererRegistry.register(type, (context) ->
                new TOKBipedRender<TOKEntity, PlayerEntityModel<TOKEntity>>(context,
                        new ImprovedPlayerEntityModel<>(context.getPart(EntityModelLayers.PLAYER), false),
                        0.5F,
                        defaultSkin));
    }

    private <T extends MobEntity & MultiSkinned> void  registerDefault(EntityType<T> type, Identifier defaultSkin) {
        EntityRendererRegistry.register(type, (context) ->
                new DefaultBipedRender<>(context,
                        new ImprovedPlayerEntityModel<>(context.getPart(EntityModelLayers.PLAYER), false),
                        0.5F,
                        defaultSkin));
    }
    
    public static Identifier identifier(String path) {
        return new Identifier(TaleOfKingdoms.MODID, path);
    }
}
