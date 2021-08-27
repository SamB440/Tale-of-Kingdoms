package com.convallyria.taleofkingdoms.quest;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.quest.objective.QuestObjective;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * TODO:
 *  Quests need to be serialisable
 *  Quests need to have requirements/objectives/rewards
 *  Tracked players needs to persist!
 */
public abstract class Quest {

    private String startMessage; //todo: make this a list
    private final List<UUID> trackedPlayers;
    private final Map<Integer, QuestObjective> objectives;
    //todo here: list of requirements/objectives/rewards

    public Quest() {
        this.trackedPlayers = new ArrayList<>();
        this.objectives = new HashMap<>();
    }

    public Optional<String> getStartMessage() {
        return Optional.ofNullable(startMessage);
    }

    public void setStartMessage(String startMessage) {
        this.startMessage = startMessage;
    }

    public ImmutableList<UUID> getTrackedPlayers() {
        return ImmutableList.copyOf(trackedPlayers);
    }

    public void addTrackedPlayer(PlayerEntity player) {
        this.trackedPlayers.add(player.getUuid());
    }

    public boolean isTracked(UUID uuid) {
        return trackedPlayers.contains(uuid);
    }

    public void start(PlayerEntity playerEntity) {
        TaleOfKingdoms.getAPI().flatMap(api -> api.getConquestInstanceStorage().mostRecentInstance()).ifPresent(instance -> {
            instance.addActiveQuest(playerEntity.getUuid(), this);
            this.trackedPlayers.add(playerEntity.getUuid());
            playerEntity.sendMessage(new LiteralText(startMessage), false);
        });
    }

    public Collection<QuestObjective> getObjectives() {
        return objectives.values();
    }

    public void addObjective(QuestObjective objective) {
        objectives.put(objectives.size() + 1, objective);
    }

    public boolean isCurrentPriority(PlayerEntity player, QuestObjective objective) {
        int lowestKey = objectives.size() + 1;
        for (Integer key : objectives.keySet()) {
            QuestObjective value = objectives.get(key);
            if (key < lowestKey && !value.hasCompleted(player.getUuid())) {
                lowestKey = key;
            }
        }
        return objective.equals(objectives.get(lowestKey));
    }

    public abstract Class<? extends TOKEntity> getEntity();

    public abstract String getName();
}
