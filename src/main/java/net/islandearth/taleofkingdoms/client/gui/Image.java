package net.islandearth.taleofkingdoms.client.gui;

import net.minecraft.util.ResourceLocation;

public class Image implements IImage {
	
	private final ResourceLocation image;
	
	public Image(ResourceLocation image) {
		this.image = image;
	}

	@Override
	public ResourceLocation getResourceLocation() {
		return image;
	}
}
