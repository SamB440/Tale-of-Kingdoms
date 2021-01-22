package com.convallyria.taleofkingdoms.managers;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.quest.Quest;
import com.convallyria.taleofkingdoms.common.quest.objective.BreakBlockQuestObjective;
import com.convallyria.taleofkingdoms.common.quest.objective.BreedQuestObjective;
import com.convallyria.taleofkingdoms.common.quest.objective.KillEntityQuestObjective;
import com.convallyria.taleofkingdoms.common.quest.objective.PlaceBlockQuestObjective;
import com.convallyria.taleofkingdoms.common.quest.objective.QuestObjective;
import com.convallyria.taleofkingdoms.common.quest.objective.QuestObjectiveRegistry;
import com.convallyria.taleofkingdoms.common.quest.reward.ExperienceReward;
import com.convallyria.taleofkingdoms.common.quest.reward.ItemReward;
import com.convallyria.taleofkingdoms.common.quest.reward.MessageReward;
import com.convallyria.taleofkingdoms.common.quest.reward.MoneyReward;
import com.convallyria.taleofkingdoms.common.quest.reward.QuestReward;
import com.convallyria.taleofkingdoms.common.quest.reward.QuestRewardRegistry;
import com.convallyria.taleofkingdoms.common.quest.start.ItemRequirement;
import com.convallyria.taleofkingdoms.common.quest.start.LevelRequirement;
import com.convallyria.taleofkingdoms.common.quest.start.MoneyRequirement;
import com.convallyria.taleofkingdoms.common.quest.start.QuestQuestRequirement;
import com.convallyria.taleofkingdoms.common.quest.start.QuestRequirement;
import com.convallyria.taleofkingdoms.common.quest.start.QuestRequirementRegistry;
import com.convallyria.taleofkingdoms.managers.data.QuesteCache;
import com.convallyria.taleofkingdoms.managers.registry.QuesteRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QuesteManagers implements IManager {
    
    private final QuesteCache questeCache;
    private final Map<Class<? extends QuesteRegistry<?>>, QuesteRegistry<?>> registry;

    public QuesteManagers(TaleOfKingdoms plugin) {
        this.questeCache = new QuesteCache(plugin);
        this.registry = new ConcurrentHashMap<>();
        registry.put(QuestObjectiveRegistry.class, new QuestObjectiveRegistry());
        registry.put(QuestRewardRegistry.class, new QuestRewardRegistry());
        registry.put(QuestRequirementRegistry.class, new QuestRequirementRegistry());
        registerObjectives();
        registerRewards();
        registerRequirements();
        Quest quest = new Quest("Breed");
        quest.setStoryMode(true);
        quest.setCanRestart(true);
        quest.setTime(60);
        BreedQuestObjective objective = new BreedQuestObjective(plugin, quest);
        objective.setStoryModeKey(0);
        objective.setCompletionAmount(2);
        quest.addObjective(objective);
        quest.addReward(new ExperienceReward(plugin));
        questeCache.addQuest(quest);
    }

    public QuesteCache getQuesteCache() {
        return questeCache;
    }

    public Map<Class<? extends QuesteRegistry<?>>, QuesteRegistry<?>> getQuestRegistry() {
        return registry;
    }

    @Nullable
    public QuesteRegistry<?> getQuestRegistry(Class<? extends QuesteRegistry<?>> clazz) {
        return registry.get(clazz);
    }
    
    private void registerObjectives() {
        QuesteRegistry<QuestObjective> registry = (QuestObjectiveRegistry) getQuestRegistry(QuestObjectiveRegistry.class);
        if (registry == null) {
            return;
        }
        registry.register(BreakBlockQuestObjective.class);
        registry.register(BreedQuestObjective.class);
        registry.register(KillEntityQuestObjective.class);
        registry.register(PlaceBlockQuestObjective.class);
    }
    
    private void registerRewards() {
        QuesteRegistry<QuestReward> registry = (QuestRewardRegistry) getQuestRegistry(QuestRewardRegistry.class);
        if (registry == null) {
            return;
        }
        registry.register(ExperienceReward.class);
        registry.register(ItemReward.class);
        registry.register(MessageReward.class);
        registry.register(MoneyReward.class);
    }
    
    private void registerRequirements() {
        QuesteRegistry<QuestRequirement> registry = (QuestRequirementRegistry) getQuestRegistry(QuestRequirementRegistry.class);
        if (registry == null) {
            return;
        }
        registry.register(LevelRequirement.class);
        registry.register(ItemRequirement.class);
        registry.register(MoneyRequirement.class);
        registry.register(QuestQuestRequirement.class);
    }
    
    @Override
    public String getName() {
        return "Queste";
    }
}
