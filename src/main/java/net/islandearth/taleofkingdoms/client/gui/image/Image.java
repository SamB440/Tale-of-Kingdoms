package net.islandearth.taleofkingdoms.client.gui.image;

import net.islandearth.taleofkingdoms.client.gui.ScreenTOK;
import net.minecraft.util.ResourceLocation;

public class Image implements IImage {

	private final ScreenTOK screen;
	private final ResourceLocation image;
	private final int x;
	private final int y;
	private final int dimensions;
	
	public Image(ScreenTOK screen, ResourceLocation image, int x, int y, int dimensions) {
		this.screen = screen;
		this.image = image;
		this.x = x;
		this.y = y;
		this.dimensions = dimensions;
	}

	@Override
	public ResourceLocation getResourceLocation() {
		return image;
	}

	public int getY() {
		return y;
	}

	public int getX() {
		return x;
	}

	public int getDimensions() {
		return dimensions;
	}

	@Override
	public void render() {
		screen.getMinecraft().getTextureManager().bindTexture(image);
		screen.blit(x, y, 0, 0, dimensions, dimensions);
	}
}
