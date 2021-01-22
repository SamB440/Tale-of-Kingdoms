package com.convallyria.taleofkingdoms.common.quest.objective;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.event.EntityBreedCallback;
import com.convallyria.taleofkingdoms.common.quest.Quest;

public final class BreedQuestObjective extends QuestObjective {

    public BreedQuestObjective(TaleOfKingdoms plugin, Quest quest) {
        super(plugin, quest);
    }
    
    @Override
    public void registerListeners() {
        EntityBreedCallback.EVENT.register((entity, other, player) -> {
            if (this.hasCompleted(player)) return;
            this.increment(player);
        });
    }

    @Override
    public String getName() {
        return "Breed Animals";
    }
}
