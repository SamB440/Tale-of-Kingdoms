package net.islandearth.taleofkingdoms.common.entity.render;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.entity.EntityTypes;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public final class RenderSetup {
	
	private final TaleOfKingdoms mod;
	
	public RenderSetup(TaleOfKingdoms mod) {
		this.mod = mod;
		setup();
	}
	
	private void setup() {
		register(EntityTypes.FARMER, new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/farmer-2.png"));
		register(EntityTypes.GUILD_MASTER, new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/guildmaster.png"));
		register(EntityTypes.BLACKSMITH, new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/toksmith2.png"));
		register(EntityTypes.CITY_BUILDER, new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/builder2.png"));
		register(EntityTypes.KNIGHT, new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/knight2.png"));
	}

	private void register(EntityType type, ResourceLocation skin) {
		RenderingRegistry.registerEntityRenderingHandler(type, (EntityRendererManager rendererManager) ->
				new TOKBipedRender<MobEntity, PlayerModel<MobEntity>>(rendererManager,
						new PlayerModel<>(0.0F, false),
						0.5F,
						skin));
	}
}
