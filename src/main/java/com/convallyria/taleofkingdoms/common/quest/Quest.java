package com.convallyria.taleofkingdoms.common.quest;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.quest.objective.QuestObjective;
import com.convallyria.taleofkingdoms.common.quest.reward.QuestReward;
import com.convallyria.taleofkingdoms.common.quest.start.QuestRequirement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class Quest {

    private transient TaleOfKingdomsAPI plugin;
    private final String name;
    private String displayName;
    private boolean canRestart;
    private final List<QuestObjective> objectives;
    private final List<QuestReward> rewards;
    private final List<QuestRequirement> requirements;
    private boolean storyMode;
    private SoundEvent completeSound;
    private int time;

    public Quest(@NotNull String name) {
        this.name = name;
        this.objectives = new ArrayList<>();
        this.rewards = new ArrayList<>();
        this.requirements = new ArrayList<>();
        this.completeSound = SoundEvents.UI_TOAST_CHALLENGE_COMPLETE;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        if (displayName == null) this.displayName = name;
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean canRestart() {
        return canRestart;
    }

    public void setCanRestart(boolean canRestart) {
        this.canRestart = canRestart;
    }

    public void addObjective(QuestObjective objective) {
        objectives.add(objective);
    }

    public void removeObjective(QuestObjective objective) {
        objectives.remove(objective);
    }

    public List<QuestObjective> getObjectives() {
        return objectives;
    }

    @NotNull
    public List<QuestObjective> getObjectivesFromType(Class<? extends QuestObjective> type) {
        List<QuestObjective> objectivesByType = new ArrayList<>();
        for (QuestObjective objective : getObjectives()) {
            if (objective.getClass().isInstance(type)) {
                objectivesByType.add(objective);
            }
        }
        return objectivesByType;
    }

    public void addReward(@NotNull QuestReward reward) {
        rewards.add(reward);
    }

    public void removeReward(@NotNull QuestReward reward) {
        rewards.remove(reward);
    }

    public List<QuestReward> getRewards() {
        return rewards;
    }

    public void addRequirement(@NotNull QuestRequirement requirement) {
        requirements.add(requirement);
    }

    public void removeRequirement(@NotNull QuestRequirement requirement) {
        requirements.remove(requirement);
    }

    public List<QuestRequirement> getRequirements() {
        return requirements;
    }

    public boolean isStoryMode() {
        return storyMode;
    }

    public void setStoryMode(boolean storyMode) {
        this.storyMode = storyMode;
    }

    public SoundEvent getCompleteSound() {
        return completeSound;
    }

    public void setCompleteSound(SoundEvent completeSound) {
        this.completeSound = completeSound;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Nullable
    public QuestObjective getCurrentObjective(@NotNull PlayerEntity player) {
        if (isStoryMode()) {
            QuestObjective currentObjective = null;
            for (QuestObjective objective : getObjectives()) {
                if (currentObjective == null && !objective.hasCompleted(player)) {
                    currentObjective = objective;
                    continue;
                }

                if (currentObjective != null
                        && objective.getStoryModeKey() < currentObjective.getStoryModeKey()
                        && !objective.hasCompleted(player)) {
                    currentObjective = objective;
                }
            }
            return currentObjective;
        }
        return null;
    }

    /**
     * Checks if a player has completed this quest.
     * @param player player to check
     * @return true if all quest objectives are completed, false otherwise
     */
    public boolean isCompleted(@NotNull PlayerEntity player) {
        boolean objectivesCompleted = true;
        for (QuestObjective objective : objectives) {
            if (!objective.hasCompleted(player)) {
                objectivesCompleted = false;
                break;
            }
        }
        return objectivesCompleted;
    }

    /**
     * Attempts to complete a quest for a player.
     * @param player player to complete quest for
     * @return true if objectives are completed and player has completed quest entirely, false otherwise
     */
    public boolean tryComplete(@NotNull PlayerEntity player) {
        if (isCompleted(player)) {
            forceComplete(player);
            return true;
        }
        return false;
    }

    public void forceComplete(PlayerEntity player) {
        giveEffectsAndRewards(player);
        getPlugin().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            instance.addCompletedQuest(this, player);
            instance.removeActiveQuest(this, player);
        });
    }

    private void giveEffectsAndRewards(PlayerEntity player) {
        //player.sendTitle(Translations.QUEST_COMPLETED_TITLE.get(player), getName(), 40, 60, 40);
        player.playSound(completeSound == null ? SoundEvents.UI_TOAST_CHALLENGE_COMPLETE : completeSound, 1f, 1f);
        player.getEntityWorld().addParticle(ParticleTypes.TOTEM_OF_UNDYING, player.getX(), player.getY(), player.getZ(), 0.25, 0.25, 1);
        rewards.forEach(reward -> reward.award(player));
        Translations.QUEST_COMPLETED.send(player, getDisplayName());
    }

    /**
     * Attempts to start a quest for a player.
     * @param player player to start quest for
     * @return true if quest was started, false if player has already completed and cannot restart
     *         or does not meet required quests beforehand
     */
    public CompletableFuture<DenyReason> tryStart(@NotNull PlayerEntity player) {
        CompletableFuture<DenyReason> future = new CompletableFuture<>();
        getPlugin().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            if (isCompleted(player) && !canRestart) {
                future.complete(DenyReason.CANNOT_RESTART);
                return;
            }
    
            if (!testRequirements(player)) {
                future.complete(DenyReason.REQUIREMENTS_NOT_MET);
                return;
            }
    
            objectives.forEach(objective -> objective.setIncrement(player, 0));
            //player.sendTitle(Translations.QUEST_STARTED.g(player), getName(), 40, 60, 40);
            if (instance.getActiveQuests(player).contains(this)) instance.removeActiveQuest(this, player);
            instance.addActiveQuest(this, player);
            future.complete(DenyReason.NONE);
        });
        return future;
    }

    public boolean testRequirements(@NotNull PlayerEntity player) {
        if (!getRequirements().isEmpty()) {
            for (QuestRequirement requirement : requirements) {
                if (!requirement.meetsRequirements(player)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void forceStart(@NotNull PlayerEntity player) {
        getPlugin().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            objectives.forEach(objective -> objective.setIncrement(player, 0));
            //player.sendTitle(Translations.QUEST_STARTED.get(player), getName(), 40, 60, 40);
            instance.addActiveQuest(this, player);
        });
    }

    @NotNull
    public TaleOfKingdomsAPI getPlugin() {
        if (plugin == null) this.plugin = TaleOfKingdoms.getAPI().get();
        return plugin;
    }

    public enum DenyReason {
        REQUIREMENTS_NOT_MET,
        CANNOT_RESTART,
        NONE
    }
}
