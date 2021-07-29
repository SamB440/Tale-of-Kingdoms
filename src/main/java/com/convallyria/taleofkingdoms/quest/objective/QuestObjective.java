package com.convallyria.taleofkingdoms.quest.objective;

import net.minecraft.entity.player.PlayerEntity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class QuestObjective {

    private int storyKey;
    private int completionAmount;
    private final Map<UUID, Integer> progress;

    public QuestObjective() {
        this.storyKey = 0;
        this.progress = new ConcurrentHashMap<>();
    }

    public abstract void test(PlayerEntity player);

    public int getStoryKey() {
        return storyKey;
    }

    public void setStoryKey(int storyKey) {
        this.storyKey = storyKey;
    }

    public int getCompletionAmount() {
        return completionAmount;
    }

    public void setCompletionAmount(int completionAmount) {
        this.completionAmount = completionAmount;
    }

    public void increment(PlayerEntity player) {
        if (progress.containsKey(player.getUuid()) && progress.get(player.getUuid()) >= getCompletionAmount()) {
            return;
        }
        progress.put(player.getUuid(), progress.getOrDefault(player.getUuid(), 0) + 1))
    }

    public boolean hasCompleted(UUID uuid) {
        return progress.getOrDefault(uuid, 1) >= getCompletionAmount();
    }

    public abstract String getName();
}
