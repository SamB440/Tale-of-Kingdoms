package com.convallyria.taleofkingdoms.quest.objective;

import com.convallyria.taleofkingdoms.quest.Quest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class QuestObjective {

    private Quest quest;
    private int storyKey;
    private int completionAmount;
    private final Map<UUID, Integer> progress;
    private String completionText;

    public QuestObjective(final Quest quest) {
        this.storyKey = 0;
        this.completionAmount = 1;
        this.progress = new ConcurrentHashMap<>();
        this.completionText = "";
        this.quest = quest;
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
        if (hasCompleted(player.getUuid())) {
            return;
        }
        progress.put(player.getUuid(), progress.getOrDefault(player.getUuid(), 0) + 1);

        if (hasCompleted(player.getUuid())) {
            player.sendMessage(new LiteralText(completionText), false);
            player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1f, 0.6f);
            quest.tryComplete(player);
        }
    }

    public boolean hasCompleted(UUID uuid) {
        return progress.getOrDefault(uuid, 0) >= getCompletionAmount();
    }

    public String getCompletionText() {
        return completionText;
    }

    public void setCompletionText(String completionText) {
        this.completionText = completionText;
    }

    public abstract String getName();
}
