package com.convallyria.taleofkingdoms.quest.objective;

import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import net.minecraft.entity.player.PlayerEntity;

public class SpeakToNPCObjective extends QuestObjective {

    private final Class<? extends TOKEntity> npc;

    public SpeakToNPCObjective(final Class<? extends TOKEntity> npc) {
        this.npc = npc;
    }

    @Override
    public void test(PlayerEntity player) {
        increment(player);
    }

    @Override
    public String getName() {
        return "Speak to NPC";
    }

    public Class<? extends TOKEntity> getNpc() {
        return npc;
    }
}
