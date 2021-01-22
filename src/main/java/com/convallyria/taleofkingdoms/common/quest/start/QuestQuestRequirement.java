package com.convallyria.taleofkingdoms.common.quest.start;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.quest.Quest;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;

public final class QuestQuestRequirement extends QuestRequirement {

    private String questName;

    protected QuestQuestRequirement(TaleOfKingdomsAPI plugin) {
        super(plugin);
        this.questName = "example";
    }

    public String getQuestName() {
        return questName;
    }

    public void setQuestName(String questName) {
        this.questName = questName;
    }

    @Override
    public boolean meetsRequirements(PlayerEntity player) {
        if (TaleOfKingdoms.getAPI().isPresent()) {
            TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI().get();
            Optional<ConquestInstance> instance = api.getConquestInstanceStorage().mostRecentInstance();
            if (instance.isPresent()) {
                for (Quest completedQuest : instance.get().getCompletedQuests(player)) {
                    if (completedQuest.getName().equals(questName)) return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "Quest";
    }
}
