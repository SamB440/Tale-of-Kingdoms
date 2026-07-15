package com.convallyria.taleofkingdoms.datagen;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.datagen.loottables.TOKChestLootTableGenerator;
import com.convallyria.taleofkingdoms.datagen.worldgen.TaleOfKingdomsModWorldGenBootstrap;
import com.convallyria.taleofkingdoms.datagen.worldgen.TaleOfKingdomsModWorldGenProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class TaleOfKingdomsModDataGeneratorEntry implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        final FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
//        Closure<DataProvider> add = new Closure<DataProvider>(this, this) {
//            public DataProvider doCall(FabricDataGenerator.Pack.RegistryDependentFactory factory) {
//                return pack.addProvider(factory);
//            }
//
//        };

        pack.addProvider(TOKChestLootTableGenerator::new);
        pack.addProvider(TaleOfKingdomsModWorldGenProvider::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.STRUCTURE, TaleOfKingdomsModWorldGenBootstrap::structures);
        registryBuilder.add(Registries.STRUCTURE_SET, TaleOfKingdomsModWorldGenBootstrap::structureSets);
    }

    @Override
    public String getEffectiveModId() {
        return TaleOfKingdoms.MODID;
    }

}
