package com.convallyria.taleofkingdoms.client.gui.image;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

public interface IImage {

    ResourceLocation getResourceLocation();

    void render(PoseStack matrices, Screen gui);

}
