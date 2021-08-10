package com.convallyria.taleofkingdoms.client.gui.entity.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PageTurnWidget extends Button {
    private final boolean isNextPageButton;
    private final boolean playPageTurnSound;

    public PageTurnWidget(int x, int y, boolean isNextPageButton, OnPress action, boolean playPageTurnSound) {
        super(x, y, 23, 13, TextComponent.EMPTY, action);
        this.isNextPageButton = isNextPageButton;
        this.playPageTurnSound = playPageTurnSound;
    }

    @Override
    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BookViewScreen.BOOK_LOCATION);
        int i = 0;
        int j = 192;
        if (this.isHovered()) {
            i += 23;
        }

        if (!this.isNextPageButton) {
            j += 13;
        }

        this.blit(matrices, this.x, this.y, i, j, 23, 13);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        if (this.playPageTurnSound) {
            soundManager.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
        }
    }
}
