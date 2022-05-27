package com.convallyria.taleofkingdoms;

import com.convallyria.taleofkingdoms.common.generator.GatewayGenerator;
import com.convallyria.taleofkingdoms.common.generator.feature.GatewayFeature;
import com.convallyria.taleofkingdoms.mixin.StructureFeatureAccessor;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.tag.BiomeTags;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class TOKStructures {

     /**
     * Registers the structure itself and sets what its path is. In this case, the
     * structure will have the Identifier of structure_tutorial:sky_structures.
     *
     * It is always a good idea to register your Structures so that other mods and datapacks can
     * use them too directly from the registries. It great for mod/datapacks compatibility.
     */
     public static final StructurePieceType GATEWAY = GatewayGenerator.GatewayPiece::new;
     public static final StructureFeature<DefaultFeatureConfig> GATEWAY_STRUCTURE = new GatewayFeature();
     public static final ConfiguredStructureFeature<?, ?> GATEWAY_CONFIGURED = GATEWAY_STRUCTURE.configure(DefaultFeatureConfig.DEFAULT, BiomeTags.MINESHAFT_HAS_STRUCTURE, true);

     public static void registerStructureFeatures() {
         // The generation step for when to generate the structure. there are 10 stages you can pick from!
         // This surface structure stage places the structure before plants and ores are generated.
         StructureFeatureAccessor.callRegister(TaleOfKingdoms.MODID + ":gateway", GATEWAY_STRUCTURE, GenerationStep.Feature.SURFACE_STRUCTURES);
     }
}
