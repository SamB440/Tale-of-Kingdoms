package net.islandearth.taleofkingdoms.client.gui;

import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;

public class ScreenContinueConquest extends ScreenTOK {
	
	// Buttons
	private Button mButtonClose;
	
	// Other
	private ConquestInstance instance;
	
	public ScreenContinueConquest(ConquestInstance instance) {
		this.instance = instance;
	}
	
	@Override
	public void init() {
		super.init();
		this.buttons.clear();
		this.addButton(mButtonClose = new Button(this.width / 2 - 100, this.height - (this.height / 4) + 10, 200, 20, "Continue your Conquest.", (button) -> {
			this.onClose();
		}));
	}
	
	@Override
	public void render(int par1, int par2, float par3) {
        this.renderBackground();
		this.drawCenteredString(this.font, Minecraft.getInstance().player.getName().getString() 
				+ ", your conquest, " 
				+ instance.getName() + ", has come far.", this.width / 2, this.height / 2 + 40, 0xFFFFFF);
		this.drawCenteredString(this.font, "Now you seek to venture further, and continue your journey.", this.width / 2, this.height / 2 + 50, 0xFFFFFF);
		this.drawCenteredString(this.font, "Safe travels, and go forth!", this.width / 2, this.height / 2 + 60, 0xFFFFFF);
        super.render(par1, par2, par3);
    }
	
	@Override
	public boolean shouldCloseOnEsc() {
		return true;
	}
}
