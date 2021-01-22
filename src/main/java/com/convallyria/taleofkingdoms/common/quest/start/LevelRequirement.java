package com.convallyria.taleofkingdoms.common.quest.start;

import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import net.minecraft.entity.player.PlayerEntity;

public final class LevelRequirement extends QuestRequirement {

    private final int level;

    public LevelRequirement(TaleOfKingdomsAPI plugin) {
        super(plugin);
        this.level = 1;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public boolean meetsRequirements(PlayerEntity player) {
        return player.experienceLevel >= 1;
    }

    @Override
    public String getName() {
        return "Level";
    }
}
