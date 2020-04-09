package net.islandearth.taleofkingdoms.client.gui.entity;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.client.entity.guild.GuildMasterEntity;
import net.islandearth.taleofkingdoms.client.gui.ScreenTOK;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

public class GuildMasterScreen extends ScreenTOK {

	private final GuildMasterEntity entity;

	public GuildMasterScreen(PlayerEntity player, GuildMasterEntity entity, ConquestInstance instance) {
		this.entity = entity;

		if (!instance.isHasContract()) {
			//TODO has contract gui
			player.sendMessage(new StringTextComponent("Guild Master: Welcome to the order, hero."));
		} else {
			//TODO doesn't have contract gui
		}
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public void render(int par1, int par2, float par3) {
		super.render(par1, par2, par3);
		ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().getConquestInstance(Minecraft.getInstance().getIntegratedServer().getFolderName()).get();
		this.drawCenteredString(this.font, "The Guild Order  Total Money: " + instance.getCoins() + " Gold Coins", this.width / 2, this.height / 2 + 25, 0xFFFFFF);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return true;
	}
}
