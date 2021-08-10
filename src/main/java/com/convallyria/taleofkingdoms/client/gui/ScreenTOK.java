package com.convallyria.taleofkingdoms.client.gui;

import com.convallyria.taleofkingdoms.client.gui.image.IImage;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class ScreenTOK extends Screen {

    private final List<IImage> images;

    /**
     * Constructs a new {@link Screen}
     * @param translation the translation key
     */
    protected ScreenTOK(String translation) {
        super(new TranslatableComponent(translation));
        this.images = new ArrayList<>();
    }

    public void addImage(IImage image) {
        images.add(image);
    }

    public void removeImage(IImage image) {
        images.remove(image);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float delta) {
        images.forEach(image -> image.render(stack, this));
        super.render(stack, mouseX, mouseY, delta);
    }

    /**
     * Whether this {@link Screen} is closeable via the escape key.
     * @return true if escapable
     */
    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    /**
     * Whether this {@link Screen} pauses the game.
     * @return true if it pauses the game
     */
    @Override
    public boolean isPauseScreen() {
        return true;
    }
}
