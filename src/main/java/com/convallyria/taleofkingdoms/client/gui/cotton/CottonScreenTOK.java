package com.convallyria.taleofkingdoms.client.gui.cotton;

import com.convallyria.taleofkingdoms.client.gui.image.IImage;
import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
@Deprecated
public abstract class CottonScreenTOK extends CottonClientScreen {

    protected final List<IImage> images;

    /**
     * Constructs a new {@link CottonClientScreen}
     */
    protected CottonScreenTOK(GuiDescription description) {
        super(description);
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
    public boolean shouldPause() {
        return true;
    }
}
