package com.convallyria.taleofkingdoms.common.datafixer;

import com.mojang.datafixers.DSL;

/**
 * Represents all the type references Minecraft's datafixer can fix.
 */
public class TypeReferences {
    /**
     * Represents a {@link com.convallyria.taleofkingdoms.common.world.ConquestInstance}
     */
    public static final DSL.TypeReference CONQUEST = () -> "conquest";

    /**
     * Dummy type reference that is required by the data fixer upper library (due to a bug).
     * <a href="https://github.com/Mojang/DataFixerUpper/issues/45">...</a>
     * @deprecated Dummy type
     */
    @Deprecated
    public static final DSL.TypeReference RECURSIVE = () -> "recursive";
}

