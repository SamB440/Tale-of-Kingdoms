package com.convallyria.taleofkingdoms.common.quest.reward;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import net.minecraft.entity.player.PlayerEntity;

public final class ExperienceReward extends QuestReward {
	
	private final int xp;

	public ExperienceReward(TaleOfKingdoms plugin) {
		super(plugin);
		this.xp = 10;
	}

	public ExperienceReward(TaleOfKingdoms plugin, int xp) {
		super(plugin);
		this.xp = xp;
	}
	
	@Override
	public void award(PlayerEntity player) {
		player.addExperience(xp);
	}

	@Override
	public String getName() {
		return "Experience";
	}
}
