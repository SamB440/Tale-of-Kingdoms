package net.islandearth.taleofkingdoms.client.gui;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public class ScreenContinueConquest extends ScreenTOK {
	
	// Buttons
	private Button mButtonClose;
	
	// Other
	private final ConquestInstance instance;
	
	public ScreenContinueConquest(ConquestInstance instance) {
		this.instance = instance;
	}
	
	@Override
	public void init() {
		super.init();
		this.buttons.clear();
		Image image = new Image(new ResourceLocation(TaleOfKingdoms.MODID, "textures/gui/title.png"));
		this.getMinecraft().getTextureManager().bindTexture(image.getResourceLocation());
		this.addButton(mButtonClose = new Button(this.width / 2 - 100, this.height - (this.height / 4) + 10, 200, 20, "Continue your Conquest.", (button) -> this.onClose()));
	}
	
	@Override
	public void render(int par1, int par2, float par3) {
        this.renderBackground();
        this.renderBackground();
		Image image = new Image(new ResourceLocation(TaleOfKingdoms.MODID, "textures/gui/title.png"));
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		/*this.minecraft.getTextureManager().bindTexture(image.getResourceLocation());
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		bufferbuilder.pos(0.0D, (double)this.height, 0.0D).tex(0.0D, (double)((float)this.height / 32.0F + (float)0)).color(64, 64, 64, 255).endVertex();
		tessellator.draw();*/
		
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
