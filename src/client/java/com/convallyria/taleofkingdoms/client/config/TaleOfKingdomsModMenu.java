package com.convallyria.taleofkingdoms.client.config;

import com.convallyria.taleofkingdoms.common.config.TaleOfKingdomsConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;

public class TaleOfKingdomsModMenu implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> AutoConfig.getConfigScreen(TaleOfKingdomsConfig.class, parent).get();
	}
}