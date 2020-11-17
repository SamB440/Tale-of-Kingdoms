package com.convallyria.taleofkingdoms.client.gui.generic;

import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import net.minecraft.client.util.math.MatrixStack;

public class ScreenSyncConquest extends ScreenTOK {

    private String progress;

    public ScreenSyncConquest() {
        super("taleofkingdoms.menu.startconquest.name");
        this.progress = "Please wait.";
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    @Override
    public void render(MatrixStack stack, int par1, int par2, float par3) {
        this.renderBackground(stack);
        drawCenteredString(stack, this.textRenderer, "Syncing data with TOK mod via server...", this.width / 2, this.height / 2, 0xFFFFFF);
        drawCenteredString(stack, this.textRenderer, progress, this.width / 2, this.height / 2 + 10, 0xFFFFFF);
        super.render(stack, par1, par2, par3);
    }
}
