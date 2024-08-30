package com.convallyria.taleofkingdoms.client.mixin;

import com.convallyria.taleofkingdoms.client.event.InventoryDrawCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class InventoryDrawEvent {

    @Shadow
    protected TextRenderer textRenderer;

    @Inject(method = "render", at = @At("TAIL"))
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (MinecraftClient.getInstance().currentScreen instanceof InventoryScreen) {
            InventoryDrawCallback.EVENT.invoker().render((InventoryScreen) MinecraftClient.getInstance().currentScreen, context, textRenderer);
        }
    }
}
