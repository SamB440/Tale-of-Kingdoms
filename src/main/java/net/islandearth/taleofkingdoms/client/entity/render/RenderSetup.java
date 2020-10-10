package net.islandearth.taleofkingdoms.client.entity.render;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.entity.EntityTypes;
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
        register(EntityTypes.FARMER, new Identifier(TaleOfKingdoms.MODID, "textures/entity/farmer-2.png"));
        register(EntityTypes.GUILDMASTER, new Identifier(TaleOfKingdoms.MODID, "textures/entity/guildmaster.png"));
        register(EntityTypes.BLACKSMITH, new Identifier(TaleOfKingdoms.MODID, "textures/entity/toksmith2.png"));
        register(EntityTypes.CITYBUILDER, new Identifier(TaleOfKingdoms.MODID, "textures/entity/builder2.png"));
        register(EntityTypes.KNIGHT, new Identifier(TaleOfKingdoms.MODID, "textures/entity/knight2.png"));
        register(EntityTypes.INNKEEPER, new Identifier(TaleOfKingdoms.MODID, "textures/entity/farmer-2.png"));
    }

    private void register(EntityType type, Identifier skin) {
        EntityRendererRegistry.INSTANCE.register(type, (dispatcher, context) ->
                new TOKBipedRender<MobEntity, PlayerEntityModel<MobEntity>>(dispatcher,
                        new PlayerEntityModel<>(0.0F, false),
                        0.5F,
                        skin));
    }
}
