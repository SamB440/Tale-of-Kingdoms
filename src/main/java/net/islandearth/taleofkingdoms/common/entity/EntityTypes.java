package net.islandearth.taleofkingdoms.common.entity;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.entity.guild.BlacksmithEntity;
import net.islandearth.taleofkingdoms.common.entity.guild.FarmerEntity;
import net.islandearth.taleofkingdoms.common.entity.guild.GuildMasterEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ObjectHolder;

@OnlyIn(Dist.CLIENT)
@ObjectHolder(TaleOfKingdoms.MODID)
public class EntityTypes {
	
	public static final EntityType<FarmerEntity> FARMER = null;
	public static final EntityType<GuildMasterEntity> GUILD_MASTER = null;
	public static final EntityType<BlacksmithEntity> BLACKSMITH = null;

	@OnlyIn(Dist.CLIENT)
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
					EntityType.Builder.<FarmerEntity>create(FarmerEntity::new, EntityClassification.MISC)
							.size(0.5f, 0.5f)
			);

			final EntityType<GuildMasterEntity> guildmaster = build(
					"guild_master",
					EntityType.Builder.<GuildMasterEntity>create(GuildMasterEntity::new, EntityClassification.MISC)
							.size(0.5f, 0.5f)
			);

			final EntityType<BlacksmithEntity> blacksmith = build(
					"blacksmith",
					EntityType.Builder.<BlacksmithEntity>create(BlacksmithEntity::new, EntityClassification.MISC)
							.size(0.5f, 0.5f)
			);

			event.getRegistry().registerAll(
					farmer,
					guildmaster,
					blacksmith
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
