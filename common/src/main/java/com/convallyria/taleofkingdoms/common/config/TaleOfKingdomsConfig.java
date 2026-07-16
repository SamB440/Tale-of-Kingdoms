package com.convallyria.taleofkingdoms.common.config;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.config.main.MainConfig;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

@Config(name = TaleOfKingdoms.MODID)
public class TaleOfKingdomsConfig extends PartitioningSerializer.GlobalData {

	@ConfigEntry.Category("main_configuration")
	@ConfigEntry.Gui.TransitiveObject
	public MainConfig mainConfig = new MainConfig();
}