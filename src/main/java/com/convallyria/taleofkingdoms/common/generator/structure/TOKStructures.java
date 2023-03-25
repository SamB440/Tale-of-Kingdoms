package com.convallyria.taleofkingdoms.common.generator.structure;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.generator.GatewayGenerator;
import com.convallyria.taleofkingdoms.common.generator.ReficuleVillageGenerator;
import com.convallyria.taleofkingdoms.common.generator.biome.TOKBiomeTags;
import com.convallyria.taleofkingdoms.common.generator.util.StructureConfigCreator;
import com.convallyria.taleofkingdoms.mixin.StructureTypeAccessor;
import com.mojang.serialization.Codec;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

public class TOKStructures {

     public static final StructurePieceType REFICULE_VILLAGE = ReficuleVillageGenerator.ReficuleVillagePiece::new;
     public static final StructureType<?> REFICULE_VILLAGE_TYPE = registerType("reficule_village", ReficuleVillageStructure.CODEC);

     public static final StructurePieceType GATEWAY = GatewayGenerator.GatewayPiece::new;
     public static final StructureType<?> GATEWAY_TYPE = registerType("gateway", GatewayStructure.CODEC);

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

     public static void registerStructureFeatures(Registerable<Structure> structureRegisterable) {
          // Ignore this here for now, was just messing around with manually adding template pools for jigsaw structures.
//          final Function<StructurePool.Projection, SinglePoolElement> single = SinglePoolElement.ofSingle("taleofkingdoms:gateway");
//          final List<Pair<Function<StructurePool.Projection, ? extends StructurePoolElement>, Integer>> of = List.of(Pair.of(single, 1));
//          StructurePool pool = new StructurePool(new Identifier(TaleOfKingdoms.MODID, "gateway"), new Identifier("empty"), of, StructurePool.Projection.RIGID);
//          System.out.println("c: " + pool.getElementCount());
//          Registry.register(BuiltinRegistries.STRUCTURE_POOL, new Identifier(TaleOfKingdoms.MODID, "gateway"), pool);
//          final StructurePool gateway = BuiltinRegistries.STRUCTURE_POOL.get(new Identifier(TaleOfKingdoms.MODID, "gateway"));
//          System.out.println("e: " + gateway);
          registerStructure(structureRegisterable, TOKStructureKeys.GATEWAY, new GatewayStructure(StructureConfigCreator
                  .create()
                  .biome(TOKBiomeTags.NO_MOUNTAINS_DESERTS)
                  .terrainAdaptation(StructureTerrainAdaptation.BEARD_THIN)
                  .build(structureRegisterable)));
          registerStructure(structureRegisterable, TOKStructureKeys.REFICULE_VILLAGE, new ReficuleVillageStructure(StructureConfigCreator
                  .create()
                  .biome(TOKBiomeTags.NO_MOUNTAINS_DESERTS)
                  .terrainAdaptation(StructureTerrainAdaptation.BEARD_BOX)
                  .build(structureRegisterable)));
     }

     private static StructureType<? extends Structure> registerType(String name, Codec<? extends Structure> structure) {
          return StructureTypeAccessor.callRegister(TaleOfKingdoms.MODID + ":" + name, structure);
     }

     private static void registerStructure(Registerable<Structure> structureRegisterable, RegistryKey<Structure> registryKey, Structure structure) {
          structureRegisterable.register(registryKey, structure);
     }
}
