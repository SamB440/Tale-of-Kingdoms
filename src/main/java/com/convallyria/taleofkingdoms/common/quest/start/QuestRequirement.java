package com.convallyria.taleofkingdoms.common.quest.start;

import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import net.minecraft.entity.player.PlayerEntity;

public abstract class QuestRequirement {

    private transient final TaleOfKingdomsAPI plugin;

    protected QuestRequirement(TaleOfKingdomsAPI plugin) {
        this.plugin = plugin;
    }

    public TaleOfKingdomsAPI getPlugin() {
        return plugin;
    }

    public abstract boolean meetsRequirements(PlayerEntity player);

    public abstract String getName();
}
