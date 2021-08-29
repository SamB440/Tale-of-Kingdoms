package com.convallyria.taleofkingdoms.quest.objective;

import com.convallyria.taleofkingdoms.quest.Quest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class SlayEntityObjective extends QuestObjective {

    private final Class<? extends Entity> entity;

    public SlayEntityObjective(final Quest quest, Class<? extends Entity> entity, int count) {
        super(quest);
        this.entity = entity;
        this.setCompletionAmount(count);
    }

    public Class<? extends Entity> getEntity() {
        return entity;
    }

    @Override
    public void test(PlayerEntity player) {
        increment(player);
    }

    @Override
    public String getName() {
        return "Slay Entity";
    }

    public enum DetectMode {
        GLOBAL
    }
}
