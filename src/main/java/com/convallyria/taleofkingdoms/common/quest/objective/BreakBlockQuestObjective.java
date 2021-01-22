package com.convallyria.taleofkingdoms.common.quest.objective;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.quest.Quest;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Material;

public final class BreakBlockQuestObjective extends QuestObjective {

    private Material blockType;

    public BreakBlockQuestObjective(TaleOfKingdoms plugin, Quest quest) {
        super(plugin, quest);
    }
    
    @Override
    public void registerListeners() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
            if (this.hasCompleted(player)) return;
            boolean flag = blockType == null || blockType == state.getMaterial();
            if (flag) {
                this.increment(player);
            }
        });
        
    }

    public Material getBlockType() {
        return blockType;
    }

    public void setBlockType(Material blockType) {
        this.blockType = blockType;
    }

    @Override
    public String getName() {
        return "Break Block";
    }
}
