package com.convallyria.taleofkingdoms.datagen.worldgen;

import com.convallyria.taleofkingdoms.common.generator.structure.TOKStructures;
import net.minecraft.registry.Registerable;
import net.minecraft.structure.StructureSet;
import net.minecraft.world.gen.structure.Structure;

public class TaleOfKingdomsModWorldGenBootstrap {
    private TaleOfKingdomsModWorldGenBootstrap() {
        /* No instantiation */
    }

    /**
     * Main method for creating structures.
     * <p>
     * See also <a href="https://minecraft.fandom.com/wiki/Custom_structure#Configured_Structure_Feature">Configured Structure Feature</a>
     * on the Minecraft Wiki and the <a href="https://misode.github.io/guides/adding-custom-structures/#the-structure">1.19 gist</a>.
     */
    public static void structures(Registerable<Structure> registry) {
        TOKStructures.registerStructureFeatures(registry);
    }

    /**
     * Main method for creating structure sets.
     * <p>
     * See also <a href="https://minecraft.fandom.com/wiki/Custom_structure#Structure_Set">Structure Set</a>
     * on the Minecraft Wiki and the <a href="https://misode.github.io/guides/adding-custom-structures/#the-structure-set">1.19 gist</a>.
     */
    public static void structureSets(Registerable<StructureSet> registry) {
        // TOKStructures.registerStructureSets(registry);
    }

}
