package net.islandearth.taleofkingdoms.common.entity.render;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.entity.EntityTypes;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.PlayerModel;
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
		RenderingRegistry.registerEntityRenderingHandler(EntityTypes.FARMER, (EntityRendererManager rendererManager) ->
				new TOKBipedRender<MobEntity, PlayerModel<MobEntity>>(rendererManager,
						new PlayerModel<>(0.0F, false),
						0.5F,
						new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/farmer-2.png")));
		
		RenderingRegistry.registerEntityRenderingHandler(EntityTypes.GUILD_MASTER, (EntityRendererManager rendererManager) ->
				new TOKBipedRender<MobEntity, PlayerModel<MobEntity>>(rendererManager,
						new PlayerModel<>(0.0F, false),
						0.5F,
						new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/guildmaster.png")));
		RenderingRegistry.registerEntityRenderingHandler(EntityTypes.BLACKSMITH, (EntityRendererManager rendererManager) ->
				new TOKBipedRender<MobEntity, PlayerModel<MobEntity>>(rendererManager,
						new PlayerModel<>(0.0F, false),
						0.5F,
						new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/smith-2.png")));
	}
}
