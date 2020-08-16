package net.islandearth.taleofkingdoms.client.gui.image;

import net.islandearth.taleofkingdoms.client.gui.ScreenTOK;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class Image implements IImage {

	private final ScreenTOK screen;
	private final Identifier image;
	private final int x;
	private final int y;
	private final int z;
	private final int dimensions;
	
	public Image(ScreenTOK screen, Identifier image, int x, int y, int z, int dimensions) {
		this.screen = screen;
		this.image = image;
		this.x = x;
		this.y = y;
		this.z = z;
		this.dimensions = dimensions;
	}

	@Override
	public Identifier getResourceLocation() {
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
		MinecraftClient.getInstance().getTextureManager().bindTexture(image);
		MatrixStack stack = new MatrixStack();
		stack.translate(x, y, z);
		screen.renderBackground(stack);
		stack.pop();
		//TODO need to test if this works.
	}
}
