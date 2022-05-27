package com.convallyria.taleofkingdoms;

import com.convallyria.taleofkingdoms.common.generator.GatewayGenerator;
import com.convallyria.taleofkingdoms.common.generator.ReficuleVillageGenerator;
import com.convallyria.taleofkingdoms.common.generator.feature.GatewayFeature;
import com.convallyria.taleofkingdoms.common.generator.feature.ReficuleVillageFeature;
import com.convallyria.taleofkingdoms.mixin.StructureFeatureAccessor;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.tag.BiomeTags;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class TOKStructures {

     public static final StructurePieceType REFICULE_VILLAGE = ReficuleVillageGenerator.ReficuleVillagePiece::new;
     public static final StructureFeature<DefaultFeatureConfig> REFICULE_VILLAGE_STRUCTURE = new ReficuleVillageFeature();
     public static final ConfiguredStructureFeature<?, ?> REFICULE_VILLAGE_CONFIGURED = REFICULE_VILLAGE_STRUCTURE.configure(DefaultFeatureConfig.DEFAULT, BiomeTags.MINESHAFT_HAS_STRUCTURE, true);

     public static final StructurePieceType GATEWAY = GatewayGenerator.GatewayPiece::new;
     public static final StructureFeature<DefaultFeatureConfig> GATEWAY_STRUCTURE = new GatewayFeature();
     public static final ConfiguredStructureFeature<?, ?> GATEWAY_CONFIGURED = GATEWAY_STRUCTURE.configure(DefaultFeatureConfig.DEFAULT, BiomeTags.MINESHAFT_HAS_STRUCTURE, true);

     public static void registerStructureFeatures() {
          // The generation step for when to generate the structure. there are 10 stages you can pick from!
          // This surface structure stage places the structure before plants and ores are generated.
          register("gateway", GATEWAY_STRUCTURE, GenerationStep.Feature.SURFACE_STRUCTURES);
          register("reficule_village", REFICULE_VILLAGE_STRUCTURE, GenerationStep.Feature.SURFACE_STRUCTURES);
     }

     private static void register(String name, StructureFeature<DefaultFeatureConfig> feature, GenerationStep.Feature step) {
          StructureFeatureAccessor.callRegister(TaleOfKingdoms.MODID + ":" + name, feature, step);
     }
}
