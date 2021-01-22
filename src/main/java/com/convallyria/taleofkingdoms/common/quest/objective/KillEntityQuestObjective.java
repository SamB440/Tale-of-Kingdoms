package com.convallyria.taleofkingdoms.common.quest.objective;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.event.EntityDeathCallback;
import com.convallyria.taleofkingdoms.common.quest.Quest;
import net.minecraft.entity.player.PlayerEntity;

public final class KillEntityQuestObjective extends QuestObjective {

    public KillEntityQuestObjective(TaleOfKingdoms plugin, Quest quest) {
        super(plugin, quest);
    }
    
    @Override
    public void registerListeners() {
        EntityDeathCallback.EVENT.register((source, entity) -> {
            if (source.getSource() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) source.getSource();
                if (this.hasCompleted(player)) return;
                this.increment(player);
            }
        });
    }
    
    @Override
    public String getName() {
        return "Kill Entity";
    }
}
