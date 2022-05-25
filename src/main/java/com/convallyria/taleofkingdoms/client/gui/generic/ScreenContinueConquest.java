package com.convallyria.taleofkingdoms.client.gui.generic;

import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class ScreenContinueConquest extends ScreenTOK {

    // Buttons
    private ButtonWidget mButtonClose;

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
        this.addDrawableChild(mButtonClose = new ButtonWidget(this.width / 2 - 100, this.height - (this.height / 4) + 10, 200, 20, new LiteralText("Continue your Conquest."), (button) -> this.close()));
    }

    @Override
    public void render(MatrixStack stack, int par1, int par2, float par3) {
        this.renderBackground(stack);
        drawCenteredText(stack, this.textRenderer, MinecraftClient.getInstance().player.getName().getString()
                + ", your conquest, "
                + instance.getName() + ", has come far.", this.width / 2, this.height / 2 + 40, 0xFFFFFF);
        drawCenteredText(stack, this.textRenderer, "Now you seek to venture further, and continue your journey.", this.width / 2, this.height / 2 + 50, 0xFFFFFF);
        drawCenteredText(stack, this.textRenderer, "Safe travels, and go forth!", this.width / 2, this.height / 2 + 60, 0xFFFFFF);
        super.render(stack, par1, par2, par3);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
