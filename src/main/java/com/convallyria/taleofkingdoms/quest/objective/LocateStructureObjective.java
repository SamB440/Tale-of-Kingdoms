package com.convallyria.taleofkingdoms.quest.objective;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.StructureFeature;

public class LocateStructureObjective extends QuestObjective {

    private final StructureFeature identifier;

    public LocateStructureObjective(final StructureFeature identifier) {
        this.identifier = identifier;
    }

    @Override
    public void test(PlayerEntity player) {
        BlockPos structureLocation = player.getEntityWorld().getServer().getOverworld().locateStructure(identifier, player.getBlockPos(), 4, false);
        if (structureLocation != null) {
            increment();
        }
    }

    @Override
    public String getName() {
        return "Locate Structure";
    }
}
