package com.convallyria.taleofkingdoms.common.config.main;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "main_configuration")
public class MainConfig implements ConfigData {
	@ConfigEntry.Gui.Tooltip
	@Comment("Whether the start kingdom GUI should be shown when joining a world")
	public boolean showStartKingdomGUI = true;

	@ConfigEntry.Gui.Tooltip
	@Comment("Whether the continue conquest GUI should be shown when joining a world")
	public boolean showContinueConquestGUI = true;

	@ConfigEntry.Gui.Tooltip
	@Comment("Percent chance that a gateway can spawn in a selected chunk")
	public int gateWaySpawnRate = 30;

	@ConfigEntry.Gui.Tooltip
	@Comment("Percent chance that a reficule village can spawn in a selected chunk")
	public int reficuleVillageSpawnRate = 20;

	@ConfigEntry.Gui.Tooltip
	@Comment("Percent chance that a bandit camp can spawn in a selected chunk")
	public int banditCampSpawnRate = 40;

	@ConfigEntry.Gui.Tooltip
	@Comment("Whether to always show the updates GUI when joining a world")
	public boolean alwaysShowUpdatesGUI = false;
}