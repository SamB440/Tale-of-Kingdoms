package com.convallyria.taleofkingdoms.common.quest.objective;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.event.tok.BlockPlaceCallback;
import com.convallyria.taleofkingdoms.common.quest.Quest;
import net.minecraft.block.Material;

public final class PlaceBlockQuestObjective extends QuestObjective {

    private Material blockType;

    public PlaceBlockQuestObjective(TaleOfKingdoms plugin, Quest quest) {
        super(plugin, quest);
    }
    
    @Override
    public void registerListeners() {
        BlockPlaceCallback.EVENT.register((block, player) -> {
            if (this.hasCompleted(player)) return;
            boolean flag = blockType == null || blockType == block.getMaterial();
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
        return "Place Block";
    }
}
