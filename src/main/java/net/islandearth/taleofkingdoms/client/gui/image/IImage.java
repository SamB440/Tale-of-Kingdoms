package net.islandearth.taleofkingdoms.client.gui.image;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public interface IImage {

	Identifier getResourceLocation();

	void render(MatrixStack matrices, Screen gui);

}
