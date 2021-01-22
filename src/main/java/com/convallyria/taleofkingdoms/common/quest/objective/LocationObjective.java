package com.convallyria.taleofkingdoms.common.quest.objective;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.quest.Quest;
import net.minecraft.util.math.BlockPos;

public abstract class LocationObjective extends QuestObjective {

    private BlockPos location;

    protected LocationObjective(TaleOfKingdoms plugin, Quest quest) {
        super(plugin, quest);
    }

    public void setLocation(BlockPos location) {
        this.location = location;
    }

    public BlockPos getLocation() {
        return location;
    }
}
