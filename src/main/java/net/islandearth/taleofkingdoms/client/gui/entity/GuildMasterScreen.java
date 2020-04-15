package net.islandearth.taleofkingdoms.client.gui.entity;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.client.gui.ScreenTOK;
import net.islandearth.taleofkingdoms.common.entity.guild.GuildMasterEntity;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class GuildMasterScreen extends ScreenTOK {

    private final PlayerEntity player;
	private final GuildMasterEntity entity;
	private final ConquestInstance instance;

	public GuildMasterScreen(PlayerEntity player, GuildMasterEntity entity, ConquestInstance instance) {
	    super("taleofkingdoms.menu.guildmaster.name");
		this.player = player;
		this.entity = entity;
		this.instance = instance;
		
		player.sendMessage(new StringTextComponent("Guild Master: Welcome to the order, hero."));
	}

	@Override
	public void init() {
		super.init();
		if (!instance.hasContract()) {
            this.addButton(new Button(this.width / 2 - 75, this.height / 4 + 50, 150, 20, "Sign up contract!", (button) -> {
                instance.setHasContract(true);
                player.sendMessage(new StringTextComponent("Guild Master: You are now one of us my friend. Kill monsters and you will soon be worthy of your title."));
                button.visible = false;
                button.active = false;
            }));
        } else {
            this.addButton(new Button(this.width / 2 - 75, this.height / 4 + 50, 150, 20, "Cancel Contract.", (button) -> {
                instance.setHasContract(false);
                button.visible = false;
                button.active = false;
            }));
        }

		String hunterText = instance.getCoins() >= 1500 ? "Hire Hunters " + TextFormatting.GREEN + "(1500 gold)" : "Hire Hunters " + TextFormatting.RED + "(1500 gold)";
        this.addButton(new Button(this.width / 2 - 75, this.height / 2 - 13, 150, 20, hunterText, (button) -> {
            //TODO what happens?
        }));

        this.addButton(new Button(this.width / 2 - 75, this.height / 2 + 20, 150, 20, "Exit", (button) -> this.onClose()));
	}

	@Override
	public void render(int par1, int par2, float par3) {
		super.render(par1, par2, par3);
		ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().getConquestInstance(Minecraft.getInstance().getIntegratedServer().getFolderName()).get();
		this.drawCenteredString(this.font, "The Guild Order  Total Money: " + instance.getCoins() + " Gold Coins", this.width / 2, this.height / 4 - 25, 0xFFFFFF);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return true;
	}
	
	@Override
	public void onClose() {
		super.onClose();
		player.sendMessage(new StringTextComponent("Guild Master: Good hunting."));
	}
}
