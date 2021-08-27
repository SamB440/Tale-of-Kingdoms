package com.convallyria.taleofkingdoms.quest.guild_captain;

import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.generic.BanditEntity;
import com.convallyria.taleofkingdoms.common.entity.generic.ForemanEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildCaptainEntity;
import com.convallyria.taleofkingdoms.common.generator.feature.MiningTownFeature;
import com.convallyria.taleofkingdoms.quest.objective.SlayEntityObjective;
import com.convallyria.taleofkingdoms.quest.objective.SpeakToNPCObjective;
import com.convallyria.taleofkingdoms.quest.Quest;
import com.convallyria.taleofkingdoms.quest.objective.LocateStructureObjective;

public final class MiningVillage extends Quest {

    public MiningVillage() {
        super();
        this.setStartMessage("Guild Captain: My King, one of our mining towns is running wild with bandits! The guild cannot spare any more men to locate and save the mining town. Please, find the mining town and save them!");
        LocateStructureObjective locateObjective = new LocateStructureObjective(MiningTownFeature.MINING_TOWN_STRUCTURE);
        locateObjective.setCompletionText("New Objective: Locate the Foreman.");
        this.addObjective(locateObjective);

        SpeakToNPCObjective speakToNPCObjective = new SpeakToNPCObjective(ForemanEntity.class);
        speakToNPCObjective.setCompletionText("New Objective: Kill all bandits in the mining outpost.");
        this.addObjective(speakToNPCObjective);

        SlayEntityObjective slayEntityObjective = new SlayEntityObjective(BanditEntity.class,10);
        slayEntityObjective.setCompletionText("Foreman: Thank you my king! Please tell the guild captain he has my thanks!");
        this.addObjective(slayEntityObjective);
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
