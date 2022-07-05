package com.convallyria.taleofkingdoms;

import com.convallyria.taleofkingdoms.common.generator.GatewayGenerator;
import com.convallyria.taleofkingdoms.common.generator.ReficuleVillageGenerator;
import com.convallyria.taleofkingdoms.common.generator.biome.TOKBiomeTags;
import com.convallyria.taleofkingdoms.common.generator.structure.GatewayStructure;
import com.convallyria.taleofkingdoms.common.generator.structure.ReficuleVillageStructure;
import com.convallyria.taleofkingdoms.common.generator.util.StructureConfigCreator;
import com.convallyria.taleofkingdoms.mixin.StructureTypeAccessor;
import com.mojang.serialization.Codec;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

public class TOKStructures {

     public static final StructurePieceType REFICULE_VILLAGE = ReficuleVillageGenerator.ReficuleVillagePiece::new;
     public static final StructureType<?> REFICULE_VILLAGE_TYPE = registerType("reficule_village", ReficuleVillageStructure.CODEC);

     public static final StructurePieceType GATEWAY = GatewayGenerator.GatewayPiece::new;
     public static final StructureType<?> GATEWAY_TYPE = registerType("gateway", GatewayStructure.CODEC);

     public static void registerStructureFeatures() {
          // Ignore this here for now, was just messing around with manually adding template pools for jigsaw structures.
//          final Function<StructurePool.Projection, SinglePoolElement> single = SinglePoolElement.ofSingle("taleofkingdoms:gateway");
//          final List<Pair<Function<StructurePool.Projection, ? extends StructurePoolElement>, Integer>> of = List.of(Pair.of(single, 1));
//          StructurePool pool = new StructurePool(new Identifier(TaleOfKingdoms.MODID, "gateway"), new Identifier("empty"), of, StructurePool.Projection.RIGID);
//          System.out.println("c: " + pool.getElementCount());
//          Registry.register(BuiltinRegistries.STRUCTURE_POOL, new Identifier(TaleOfKingdoms.MODID, "gateway"), pool);
//          final StructurePool gateway = BuiltinRegistries.STRUCTURE_POOL.get(new Identifier(TaleOfKingdoms.MODID, "gateway"));
//          System.out.println("e: " + gateway);

          registerStructure("gateway", new GatewayStructure(StructureConfigCreator
                  .create()
                  .biome(TOKBiomeTags.NO_MOUNTAINS_DESERTS)
                  .terrainAdaptation(StructureTerrainAdaptation.BEARD_THIN)
                  .build()));
          registerStructure("reficule_village", new ReficuleVillageStructure(StructureConfigCreator
                  .create()
                  .biome(TOKBiomeTags.NO_MOUNTAINS_DESERTS)
                  .terrainAdaptation(StructureTerrainAdaptation.BEARD_THIN)
                  .build()));
     }

     private static StructureType<? extends Structure> registerType(String name, Codec<? extends Structure> structure) {
          return StructureTypeAccessor.callRegister(TaleOfKingdoms.MODID + ":" + name, structure);
     }

     private static void registerStructure(String name, Structure structure) {
          Registry.register(BuiltinRegistries.STRUCTURE, new Identifier(TaleOfKingdoms.MODID, name), structure);
     }
}
