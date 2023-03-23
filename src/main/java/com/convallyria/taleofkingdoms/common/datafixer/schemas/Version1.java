package com.convallyria.taleofkingdoms.common.datafixer.schemas;

import com.convallyria.taleofkingdoms.common.datafixer.TypeReferences;
import com.google.common.collect.Maps;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;

import java.util.Map;
import java.util.function.Supplier;

public class Version1 extends Schema {

    public Version1(int versionKey, Schema parent) {
        super(versionKey, parent);
    }

    public Version1() {
        super(1, null);
    }

    @Override
    public void registerTypes(final Schema schema, final Map<String, Supplier<TypeTemplate>> entityTypes, final Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
        schema.registerType(true, TypeReferences.RECURSIVE, () -> {
            return DSL.or(DSL.constType(DSL.intType()), DSL.constType(DSL.string()));
        });
    }

    @Override
    public Map<String, Supplier<TypeTemplate>> registerEntities(final Schema schema) {
        Map<String, Supplier<TypeTemplate>> map = Maps.newHashMap();
        return map;
    }

    @Override
    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(final Schema schema) {
        Map<String, Supplier<TypeTemplate>> map = Maps.newHashMap();
        return map;
    }

}