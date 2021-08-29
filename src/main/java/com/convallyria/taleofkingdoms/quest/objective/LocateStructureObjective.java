package com.convallyria.taleofkingdoms.quest.objective;

import com.convallyria.taleofkingdoms.quest.Quest;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.StructureFeature;

public class LocateStructureObjective extends QuestObjective {

    private final StructureFeature<?> identifier;

    public LocateStructureObjective(final Quest quest, final StructureFeature<?> identifier) {
        super(quest);
        this.identifier = identifier;
    }

    @Override
    public void test(PlayerEntity player) {
        BlockPos playerPos = player.getBlockPos();
        BlockPos structureLocation = MinecraftClient.getInstance().getServer().getOverworld().locateStructure(identifier, playerPos, 0, false);
        if (structureLocation != null) {
            increment(player);
        }
    }

    @Override
    public String getName() {
        return "Locate Structure";
    }
}
