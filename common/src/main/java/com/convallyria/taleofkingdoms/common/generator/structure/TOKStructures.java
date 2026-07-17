package com.convallyria.taleofkingdoms.common.generator.structure;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.generator.BanditCampGenerator;
import com.convallyria.taleofkingdoms.common.generator.GatewayGenerator;
import com.convallyria.taleofkingdoms.common.generator.GuildGenerator;
import com.convallyria.taleofkingdoms.common.generator.ReficuleVillageGenerator;
import com.convallyria.taleofkingdoms.common.generator.biome.TOKBiomeTags;
import com.convallyria.taleofkingdoms.common.generator.util.StructureConfigCreator;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class TOKStructures {

     // Deferred registration (see EntityTypes for the NeoForge freeze rationale).
     public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES = DeferredRegister.create(TaleOfKingdoms.MODID, Registries.STRUCTURE_PIECE);
     public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(TaleOfKingdoms.MODID, Registries.STRUCTURE_TYPE);

     public static final RegistrySupplier<StructurePieceType> REFICULE_VILLAGE = STRUCTURE_PIECES.register("reficule_village_piece", () -> (StructurePieceType) ReficuleVillageGenerator.ReficuleVillagePiece::new);
     public static final RegistrySupplier<StructureType<ReficuleVillageStructure>> REFICULE_VILLAGE_TYPE = STRUCTURE_TYPES.register("reficule_village", () -> (StructureType<ReficuleVillageStructure>) () -> ReficuleVillageStructure.CODEC);

     public static final RegistrySupplier<StructurePieceType> GATEWAY = STRUCTURE_PIECES.register("gateway_piece", () -> (StructurePieceType) GatewayGenerator.GatewayPiece::new);
     public static final RegistrySupplier<StructureType<GatewayStructure>> GATEWAY_TYPE = STRUCTURE_TYPES.register("gateway", () -> (StructureType<GatewayStructure>) () -> GatewayStructure.CODEC);

     public static final RegistrySupplier<StructurePieceType> BANDIT_CAMP = STRUCTURE_PIECES.register("bandit_camp_piece", () -> (StructurePieceType) BanditCampGenerator.BanditCampPiece::new);
     public static final RegistrySupplier<StructureType<BanditCampStructure>> BANDIT_CAMP_TYPE = STRUCTURE_TYPES.register("bandit_camp", () -> (StructureType<BanditCampStructure>) () -> BanditCampStructure.CODEC);

     public static final RegistrySupplier<StructurePieceType> GUILD = STRUCTURE_PIECES.register("guild_piece", () -> (StructurePieceType) GuildGenerator.GuildPiece::new);
     public static final RegistrySupplier<StructureType<GuildStructure>> GUILD_TYPE = STRUCTURE_TYPES.register("guild", () -> (StructureType<GuildStructure>) () -> GuildStructure.CODEC);

     // This doesn't work. We use json files by default for now.
//     public static void registerStructureSets(Registerable<StructureSet> structureSetRegisterable) {
//          RegistryEntryLookup<Structure> registryEntryLookup = structureSetRegisterable.getRegistryLookup(RegistryKeys.STRUCTURE);
//          // Salts are <day><month><year>
//          // If two are the same, add 1 day
//          structureSetRegisterable.register(TOKStructureSetKeys.GATEWAYS, new StructureSet(registryEntryLookup.getOrThrow(TOKStructureKeys.GATEWAY),
//                  new RandomSpreadStructurePlacement(12, 4, SpreadType.TRIANGULAR, 2752022)));
//          structureSetRegisterable.register(TOKStructureSetKeys.REFICULE_VILLAGES, new StructureSet(registryEntryLookup.getOrThrow(TOKStructureKeys.REFICULE_VILLAGE),
//                  new RandomSpreadStructurePlacement(32, 8, SpreadType.LINEAR, 2852022)));
//     }

     public static void registerStructureFeatures(BootstrapContext<Structure> structureRegisterable) {
          // Ignore this here for now, was just messing around with manually adding template pools for jigsaw structures.
//          final Function<StructurePool.Projection, SinglePoolElement> single = SinglePoolElement.ofSingle("taleofkingdoms:gateway");
//          final List<Pair<Function<StructurePool.Projection, ? extends StructurePoolElement>, Integer>> of = List.of(Pair.of(single, 1));
//          StructurePool pool = new StructurePool(Identifier.of(TaleOfKingdoms.MODID, "gateway"), Identifier.of("empty"), of, StructurePool.Projection.RIGID);
//          System.out.println("c: " + pool.getElementCount());
//          Registry.register(BuiltinRegistries.STRUCTURE_POOL, Identifier.of(TaleOfKingdoms.MODID, "gateway"), pool);
//          final StructurePool gateway = BuiltinRegistries.STRUCTURE_POOL.get(Identifier.of(TaleOfKingdoms.MODID, "gateway"));
//          System.out.println("e: " + gateway);
          registerStructure(structureRegisterable, TOKStructureKeys.BANDIT_CAMP, new BanditCampStructure(StructureConfigCreator
                  .create()
                  .biome(TOKBiomeTags.NO_MOUNTAINS_DESERTS)
                  .terrainAdaptation(TerrainAdjustment.BEARD_THIN)
                  .build(structureRegisterable)));
          registerStructure(structureRegisterable, TOKStructureKeys.GATEWAY, new GatewayStructure(StructureConfigCreator
                  .create()
                  .biome(TOKBiomeTags.NO_MOUNTAINS_DESERTS)
                  .terrainAdaptation(TerrainAdjustment.BEARD_THIN)
                  .build(structureRegisterable)));
          registerStructure(structureRegisterable, TOKStructureKeys.REFICULE_VILLAGE, new ReficuleVillageStructure(StructureConfigCreator
                  .create()
                  .biome(TOKBiomeTags.NO_MOUNTAINS_DESERTS)
                  .terrainAdaptation(TerrainAdjustment.BEARD_BOX)
                  .build(structureRegisterable)));
          // The guild's biome gate is deliberately permissive (every biome mod adds its biomes to
          // is_overworld); GuildStructure excludes water via TOKBiomeTags.GUILD_AVOID in code, so
          // modded land biomes that carry no tags at all can still host the guild.
          registerStructure(structureRegisterable, TOKStructureKeys.GUILD, new GuildStructure(StructureConfigCreator
                  .create()
                  .biome(BiomeTags.IS_OVERWORLD)
                  .terrainAdaptation(TerrainAdjustment.BEARD_THIN)
                  .build(structureRegisterable)));
     }

     private static void registerStructure(BootstrapContext<Structure> structureRegisterable, ResourceKey<Structure> registryKey, Structure structure) {
          structureRegisterable.register(registryKey, structure);
     }
}
