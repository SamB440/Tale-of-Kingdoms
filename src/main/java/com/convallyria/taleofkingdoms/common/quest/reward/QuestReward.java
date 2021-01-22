package com.convallyria.taleofkingdoms.common.quest.reward;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.entity.player.PlayerEntity;

public abstract class QuestReward {

    private transient final TaleOfKingdoms plugin;

    protected QuestReward(TaleOfKingdoms plugin) {
        this.plugin = plugin;
    }

    public TaleOfKingdoms getPlugin() {
        return plugin;
    }

    /**
     * Awards this reward to the specified player
     * @param player player to award to
     */
    public abstract void award(PlayerEntity player);

    /**
     * User friendly name of this reward.
     * @return name of reward
     */
    public abstract String getName();
}
