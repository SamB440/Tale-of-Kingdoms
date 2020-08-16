package net.islandearth.taleofkingdoms.mixin;

import net.islandearth.taleofkingdoms.common.event.InventoryDrawCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public class InventoryDrawEvent {

    @Inject(method = "render", at = @At("HEAD"))
    private void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        InventoryDrawCallback.EVENT.invoker().render((InventoryScreen) MinecraftClient.getInstance().currentScreen, matrices);
    }
}
