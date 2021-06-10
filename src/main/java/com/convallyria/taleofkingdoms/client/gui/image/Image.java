package com.convallyria.taleofkingdoms.client.gui.image;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public record Image(Identifier resourceLocation, int x, int y, int[] dimensions) implements IImage {

    public int getWidth() {
        return dimensions[0];
    }

    public int getHeight() {
        return dimensions[1];
    }

    @Override
    public Identifier getResourceLocation() {
        return resourceLocation;
    }

    @Override
    public void render(MatrixStack matrices, Screen gui) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, resourceLocation);
        //client.getTextureManager().bindTexture(resourceLocation);
        DrawableHelper.drawTexture(matrices, x, y, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
    }
}
