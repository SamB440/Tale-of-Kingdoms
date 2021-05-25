package com.convallyria.taleofkingdoms.client.gui;

import com.convallyria.taleofkingdoms.client.gui.image.IImage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public abstract class ScreenTOK extends Screen {

    private final List<IImage> images;

    /**
     * Constructs a new {@link Screen}
     * @param translation the translation key
     */
    protected ScreenTOK(String translation) {
        super(new TranslatableText(translation));
        this.images = new ArrayList<>();
    }

    public void addImage(IImage image) {
        images.add(image);
    }

    public void removeImage(IImage image) {
        images.remove(image);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float delta) {
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
