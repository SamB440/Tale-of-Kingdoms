package com.convallyria.taleofkingdoms.common.quest.objective;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.quest.Quest;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.managers.QuesteManagers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public abstract class QuestObjective {

    private transient TaleOfKingdoms plugin;

    private final String questName;
    private int completionAmount;
    private final Map<UUID, Integer> progress;
    private int storyModeKey;
    private String displayName;

    protected QuestObjective(TaleOfKingdoms plugin, Quest quest) {
        this.plugin = plugin;
        this.questName = quest.getName();
        this.completionAmount = 10;
        this.progress = new ConcurrentHashMap<>();
        this.storyModeKey = 0; // Auto set it as first - maybe change this in the future to set it as last compared to other objectives.
        this.registerListeners();
    }

    public abstract void registerListeners();
    
    @NotNull
    public TaleOfKingdoms getPlugin() {
        if (plugin == null) this.plugin = TaleOfKingdoms.getAPI().get().getMod();
        return plugin;
    }

    @NotNull
    public String getQuestName() {
        return questName;
    }

    @Nullable
    public Quest getQuest() {
        QuesteManagers manager = (QuesteManagers) TaleOfKingdoms.getAPI().get().getManager("Queste");
        return manager.getQuesteCache().getQuest(getQuestName());
    }

    public int getCompletionAmount() {
        return completionAmount;
    }

    public void setCompletionAmount(int completionAmount) {
        this.completionAmount = completionAmount;
    }

    public int getIncrement(@NotNull PlayerEntity player) {
        return progress.getOrDefault(player.getUuid(), 0);
    }

    public boolean increment(@NotNull PlayerEntity player) {
        if (TaleOfKingdoms.getAPI().isPresent()) {
            TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI().get();
            Optional<ConquestInstance> instance = api.getConquestInstanceStorage().mostRecentInstance();
            if (instance.isPresent()) {
                for (Quest quest : instance.get().getActiveQuests(player)) {
                    if (quest.getName().equals(this.getQuestName())) {
                        for (QuestObjective otherObjective : quest.getObjectives()) {
                            // If the player has not completed another objective, story mode is enabled, and our story
                            // is later on, do not continue.
                            if (!otherObjective.hasCompleted(player)
                                    && quest.isStoryMode()
                                    && this.getStoryModeKey() > otherObjective.getStoryModeKey()) {
                                return false;
                            }
                        }
        
                        // Increase progress, update bossbar, if this objective is completed play effects
                        progress.put(player.getUuid(), progress.getOrDefault(player.getUuid(), 0) + 1);
                        instance.get().update(quest, player);
                        if (progress.get(player.getUuid()) >= completionAmount) {
                            QuestObjective currentObjective = quest.getCurrentObjective(player);
                            if (currentObjective != null) {
                                Translations.OBJECTIVE_COMPLETE.send(player, this.getStoryModeKey() + 1,
                                        quest.getObjectives().size(),
                                        this.getStoryModeKey() + 2,
                                        quest.getObjectives().size(),
                                        getName());
                                player.playSound(SoundEvents.UI_TOAST_IN, 1f, 1f);
                            }
                            quest.tryComplete(player); // Attempt completion of quest
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void setIncrement(@NotNull PlayerEntity player, int increment) {
        progress.put(player.getUuid(), increment);
    }

    public int getStoryModeKey() {
        return storyModeKey;
    }

    public void setStoryModeKey(int storyModeKey) {
        this.storyModeKey = storyModeKey;
    }

    public boolean hasCompleted(@NotNull PlayerEntity player) {
        return progress.getOrDefault(player.getUuid(), 0) == completionAmount;
    }

    public String getDisplayName() {
        if (displayName == null) displayName = getName();
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public abstract String getName();
}
