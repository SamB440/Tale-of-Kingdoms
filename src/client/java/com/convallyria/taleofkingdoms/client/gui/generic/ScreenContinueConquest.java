package com.convallyria.taleofkingdoms.client.gui.generic;

import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class ScreenContinueConquest extends ScreenTOK {

    // Buttons
    private Button mButtonClose;

    // Other
    private final ConquestInstance instance;

    public ScreenContinueConquest(ConquestInstance instance) {
        super("taleofkingdoms.menu.continueconquest.name");
        this.instance = instance;
    }

    @Override
    public void init() {
        super.init();
        this.children().clear();
        this.addRenderableWidget(Button.builder(Component.literal("Continue your Conquest."), widget -> {
            this.onClose();
        }).bounds(this.width / 2 - 100, this.height - (this.height / 4) + 10, 200, 20).build());
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredString(this.font, Minecraft.getInstance().player.getName().getString()
                + ", your conquest, "
                + instance.getName() + ", has come far.", this.width / 2, this.height / 2 + 40, 0xFFFFFF);
        context.drawCenteredString(this.font, "Now you seek to venture further, and continue your journey.", this.width / 2, this.height / 2 + 50, 0xFFFFFF);
        context.drawCenteredString(this.font, "Safe travels, and go forth!", this.width / 2, this.height / 2 + 60, 0xFFFFFF);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
