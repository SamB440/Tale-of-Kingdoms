package com.convallyria.taleofkingdoms.common.quest.reward;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.entity.player.PlayerEntity;

public final class MoneyReward extends QuestReward {

    private final int amount;

    public MoneyReward(TaleOfKingdoms plugin) {
        super(plugin);
        this.amount = 1;
    }

    public MoneyReward(TaleOfKingdoms plugin, int amount) {
        super(plugin);
        this.amount = amount;
    }

    @Override
    public void award(PlayerEntity player) {
        //TODO add coins
    }

    @Override
    public String getName() {
        return "Money";
    }
}
