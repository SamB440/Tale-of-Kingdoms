package com.convallyria.taleofkingdoms.mixin.structure;

import com.convallyria.taleofkingdoms.common.generator.structure.TOKStructures;
import net.minecraft.registry.Registerable;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.Structures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Structures.class)
public class StructuresMixin {

    @Inject(method = "bootstrap", at = @At("TAIL"))
    private static void bootstrap(Registerable<Structure> structureRegisterable, CallbackInfo ci) {

        System.out.println("call register");
        // Register as features
        TOKStructures.registerStructureFeatures(structureRegisterable);
    }
}
