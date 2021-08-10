package com.convallyria.taleofkingdoms.mixin.client;

import com.convallyria.taleofkingdoms.common.event.InventoryDrawCallback;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class InventoryDrawEvent {

    @Shadow
    protected Font textRenderer;

    @Inject(method = "render", at = @At("HEAD"))
    private void render(PoseStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (Minecraft.getInstance().screen instanceof InventoryScreen) {
            InventoryDrawCallback.EVENT.invoker().render((InventoryScreen) Minecraft.getInstance().screen, matrices, textRenderer);
        }
    }
}
