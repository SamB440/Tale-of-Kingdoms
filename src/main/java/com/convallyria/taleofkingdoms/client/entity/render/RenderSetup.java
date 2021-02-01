package com.convallyria.taleofkingdoms.client.entity.render;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
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
        register(EntityTypes.BANKER, new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/banker.png"));
        register(EntityTypes.LONE, new Identifier(TaleOfKingdoms.MODID, "textures/entity/lone.png"));
        register(EntityTypes.FOODSHOP, new Identifier(TaleOfKingdoms.MODID, "textures/entity/updated_textures/foodshop.png"));
        register(EntityTypes.GUILDCAPTAIN, new Identifier(TaleOfKingdoms.MODID, "textures/entity/guildcaptain.png"));
    }

    private void register(EntityType<?> type, Identifier skin) {
        EntityRendererRegistry.INSTANCE.register(type, (dispatcher, context) ->
                new TOKBipedRender<MobEntity, PlayerEntityModel<MobEntity>>(dispatcher,
                        new PlayerEntityModel<>(0.0F, false),
                        0.5F,
                        skin));
    }
}
