package com.convallyria.taleofkingdoms.common.quest.start;

import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import net.minecraft.entity.player.PlayerEntity;

public final class MoneyRequirement extends QuestRequirement {

    private final int amount;

    public MoneyRequirement(TaleOfKingdomsAPI plugin) {
        super(plugin);
        this.amount = 1;
    }

    @Override
    public boolean meetsRequirements(PlayerEntity player) {
        //TODO check coins
        return false;
    }

    @Override
    public String getName() {
        return "Money";
    }
}
