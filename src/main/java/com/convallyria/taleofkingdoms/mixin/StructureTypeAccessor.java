package com.convallyria.taleofkingdoms.mixin;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StructureType.class)
public interface StructureTypeAccessor {

    @Invoker
    static <S extends Structure> StructureType<S> callRegister(String id, MapCodec<S> codec) {
        throw new UnsupportedOperationException();
    }
}