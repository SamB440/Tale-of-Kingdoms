package com.convallyria.taleofkingdoms.datagen.worldgen;

import com.convallyria.taleofkingdoms.common.generator.structure.TOKStructures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;

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
    public static void structures(BootstrapContext<Structure> registry) {
        TOKStructures.registerStructureFeatures(registry);
    }

    /**
     * Main method for creating structure sets.
     * <p>
     * See also <a href="https://minecraft.fandom.com/wiki/Custom_structure#Structure_Set">Structure Set</a>
     * on the Minecraft Wiki and the <a href="https://misode.github.io/guides/adding-custom-structures/#the-structure-set">1.19 gist</a>.
     */
    public static void structureSets(BootstrapContext<StructureSet> registry) {
        // TOKStructures.registerStructureSets(registry);
    }

}
