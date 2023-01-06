package com.convallyria.taleofkingdoms.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public abstract class ScreenTOK extends Screen {

    /**
     * Constructs a new {@link Screen}
     * @param translation the translation key
     */
    protected ScreenTOK(String translation) {
        super(Text.translatable(translation));
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
