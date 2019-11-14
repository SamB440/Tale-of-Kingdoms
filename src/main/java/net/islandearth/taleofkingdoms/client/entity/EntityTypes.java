package net.islandearth.taleofkingdoms.client.entity;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(TaleOfKingdoms.MODID)
public class EntityTypes {
	
	static final EntityType<FarmerEntity> FARMER = null;
	static final EntityType<GuildMasterEntity> GUILD_MASTER = null;

	@Mod.EventBusSubscriber(modid = TaleOfKingdoms.MODID, bus = Bus.MOD)
	public static class RegistrationHandler {
		/**
		 * Register this mod's {@link Entity} types.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
			final EntityType<FarmerEntity> farmer = build(
					"farmer",
					EntityType.Builder.<FarmerEntity>create((FarmerEntity::new), EntityClassification.MISC)
							.size(0.5f, 0.5f)
			);

			final EntityType<GuildMasterEntity> guildmaster = build(
					"guild_master",
					EntityType.Builder.<GuildMasterEntity>create((GuildMasterEntity::new), EntityClassification.MISC)
							.size(0.5f, 0.5f)
			);

			event.getRegistry().registerAll(
					farmer,
					guildmaster
			);
		}

		/**
		 * Build an {@link EntityType} from a {@link EntityType.Builder} using the specified name.
		 *
		 * @param name    The entity type name
		 * @param builder The entity type builder to build
		 * @return The built entity type
		 */
		private static <T extends Entity> EntityType<T> build(final String name, final EntityType.Builder<T> builder) {
			final ResourceLocation registryName = new ResourceLocation(TaleOfKingdoms.MODID, name);

			final EntityType<T> entityType = builder
					.build(registryName.toString());

			entityType.setRegistryName(registryName);

			return entityType;
		}
	}
}
