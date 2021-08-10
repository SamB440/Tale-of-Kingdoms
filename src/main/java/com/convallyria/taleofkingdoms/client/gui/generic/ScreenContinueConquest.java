package com.convallyria.taleofkingdoms.client.gui.generic;

import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;

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
        this.addRenderableWidget(mButtonClose = new Button(this.width / 2 - 100, this.height - (this.height / 4) + 10, 200, 20, new TextComponent("Continue your Conquest."), (button) -> this.onClose()));
    }

    @Override
    public void render(PoseStack stack, int par1, int par2, float par3) {
        this.renderBackground(stack);
        drawCenteredString(stack, this.font, Minecraft.getInstance().player.getName().getString()
                + ", your conquest, "
                + instance.getName() + ", has come far.", this.width / 2, this.height / 2 + 40, 0xFFFFFF);
        drawCenteredString(stack, this.font, "Now you seek to venture further, and continue your journey.", this.width / 2, this.height / 2 + 50, 0xFFFFFF);
        drawCenteredString(stack, this.font, "Safe travels, and go forth!", this.width / 2, this.height / 2 + 60, 0xFFFFFF);
        super.render(stack, par1, par2, par3);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
