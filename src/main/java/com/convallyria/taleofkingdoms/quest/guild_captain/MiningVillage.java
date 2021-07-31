package com.convallyria.taleofkingdoms.quest.guild_captain;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildCaptainEntity;
import com.convallyria.taleofkingdoms.quest.Quest;
import com.convallyria.taleofkingdoms.quest.objective.LocateStructureObjective;

public class MiningVillage extends Quest {

    public MiningVillage() {
        super();
        this.setStartMessage("Guild Captain: My King, one of our mining towns is running wild with bandits! The guild cannot spare any more men to locate and save the mining town. Please, find the mining town and save them!");
        this.addObjective(new LocateStructureObjective(TaleOfKingdoms.MINING_TOWN_STRUCTURE));
    }

    @Override
    public Class<? extends TOKEntity> getEntity() {
        return GuildCaptainEntity.class;
    }

    @Override
    public String getName() {
        return "Overrun Mining Village";
    }
}
