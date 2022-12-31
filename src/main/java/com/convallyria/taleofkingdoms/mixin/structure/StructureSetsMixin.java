package com.convallyria.taleofkingdoms.mixin.structure;

import com.convallyria.taleofkingdoms.common.generator.structure.TOKStructures;
import net.minecraft.registry.Registerable;
import net.minecraft.structure.StructureSet;
import net.minecraft.structure.StructureSets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StructureSets.class)
public interface StructureSetsMixin {

    @Inject(method = "bootstrap", at = @At("TAIL"))
    private static void bootstrap(Registerable<StructureSet> structureRegisterable, CallbackInfo ci) {
        System.out.println("call register set");
        // Register as features
        TOKStructures.registerStructureSets(structureRegisterable);
    }
}
