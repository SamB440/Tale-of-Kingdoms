package com.convallyria.taleofkingdoms.client.gui.generic;

import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

/**
 * Prompts the player for their name, in the same way {@link ScreenStartConquest} does at world
 * start. Used when signing the contract with the Guild Master while the guild was generated
 * automatically (the {@code autoGenerateGuild} gamerule), where no name has been chosen yet.
 */
public class ScreenEnterName extends ScreenTOK {

    private static final String DEFAULT_NAME = "Sir Punchwood";

    private final Consumer<String> callback;
    private EditBox text;

    public ScreenEnterName(Consumer<String> callback) {
        super("menu.taleofkingdoms.entername.name");
        this.callback = callback;
    }

    @Override
    public void init() {
        super.init();
        this.children().clear();
        this.text = new EditBox(this.font, this.width / 2 - 150, this.height / 2 - 40, 300, 20, Component.literal(DEFAULT_NAME));
        this.addRenderableWidget(Button.builder(Component.translatable("menu.taleofkingdoms.entername.confirm"), button -> {
            final String name = text.getValue().isBlank() ? DEFAULT_NAME : text.getValue().trim();
            this.onClose();
            callback.accept(name);
        }).bounds(this.width / 2 - 100, this.height / 2 + 15, 200, 20).build());

        this.addRenderableWidget(Button.builder(Component.translatable("menu.taleofkingdoms.generic.cancel"), button -> {
            this.onClose();
        }).bounds(this.width / 2 - 50, this.height / 2 + 50, 100, 20).build());

        this.text.setMaxLength(32);
        this.text.setValue(DEFAULT_NAME);
        this.text.setCanLoseFocus(true);
        this.text.setFocused(true);
        this.text.setVisible(true);
        this.addWidget(this.text);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredString(this.font, Component.translatable("menu.taleofkingdoms.entername.prompt"), this.width / 2, this.height / 2 - 60, 0xFFFFFF);
        this.text.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
