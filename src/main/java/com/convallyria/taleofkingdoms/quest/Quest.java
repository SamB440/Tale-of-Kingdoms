package com.convallyria.taleofkingdoms.quest;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.quest.objective.QuestObjective;
import com.google.common.collect.ImmutableList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;
import java.util.List;
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
    private final List<QuestObjective> objectives;
    //todo here: list of requirements/objectives/rewards

    public Quest() {
        this.trackedPlayers = new ArrayList<>();
        this.objectives = new ArrayList<>();
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

    public List<QuestObjective> getObjectives() {
        return objectives;
    }

    public void addObjective(QuestObjective objective) {
        objectives.add(objective);
    }

    public void testObjectives(PlayerEntity playerEntity) {
        objectives.forEach(objective -> objective.test(playerEntity));
    }

    public abstract Class<? extends TOKEntity> getEntity();

    public abstract String getName();
}
