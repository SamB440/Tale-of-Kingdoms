package com.convallyria.taleofkingdoms.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StructureType.class)
public interface StructureTypeAccessor {

    @Invoker
    static <S extends Structure> StructureType<S> callRegister(String id, Codec<S> codec) {
        throw new UnsupportedOperationException();
    }
}