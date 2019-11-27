package net.islandearth.taleofkingdoms.client;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.client.entity.FarmerEntity;
import net.islandearth.taleofkingdoms.client.entity.GuildMasterEntity;
import net.islandearth.taleofkingdoms.client.entity.render.TOKBipedRender;
import net.islandearth.taleofkingdoms.client.gui.RenderListener;
import net.islandearth.taleofkingdoms.common.listener.CoinListener;
import net.islandearth.taleofkingdoms.common.listener.StartWorldListener;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
public class ClientSetup {

	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent fcse) {
		registerEvents();

		RenderingRegistry.registerEntityRenderingHandler(FarmerEntity.class, (EntityRendererManager rendererManager) -> {
			return new TOKBipedRender<MobEntity, PlayerModel<MobEntity>>(rendererManager,
					new PlayerModel<>(0.0F, false),
					0.5F,
					new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/farmer-2.png"));
		});

		RenderingRegistry.registerEntityRenderingHandler(GuildMasterEntity.class, (EntityRendererManager rendererManager) -> {
			return new TOKBipedRender<MobEntity, PlayerModel<MobEntity>>(rendererManager,
					new PlayerModel<>(0.0F, false),
					0.5F,
					new ResourceLocation(TaleOfKingdoms.MODID, "textures/entity/guildmaster.png"));
		});
	}

	private static void registerEvents() {
		IEventBus bus = MinecraftForge.EVENT_BUS;
		bus.register(new StartWorldListener());
		bus.register(new RenderListener());
		bus.register(new CoinListener());
	}
}
