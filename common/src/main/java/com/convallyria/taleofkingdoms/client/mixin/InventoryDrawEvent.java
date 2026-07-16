package com.convallyria.taleofkingdoms.client.mixin;

import com.convallyria.taleofkingdoms.client.event.InventoryDrawCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
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
    protected Font font;

    @Inject(method = "render", at = @At("TAIL"))
    private void render(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (Minecraft.getInstance().screen instanceof InventoryScreen) {
            InventoryDrawCallback.EVENT.invoker().render((InventoryScreen) Minecraft.getInstance().screen, context, font);
        }
    }
}
