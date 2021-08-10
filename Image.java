import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class Image implements IImage {

    private final ResourceLocation resourceLocation;
    private final int x;
    private final int y;
    private final int[] dimensions;

    public Image(ResourceLocation resourceLocation, int x, int y, int[] dimensions) {
        this.resourceLocation = resourceLocation;
        this.x = x;
        this.y = y;
        this.dimensions = dimensions;
    }

    public int getWidth() {
        return dimensions[0];
    }

    public int getHeight() {
        return dimensions[1];
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    @Override
    public void render(PoseStack matrices, Screen gui) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, resourceLocation);
        //client.getTextureManager().bindTexture(resourceLocation);
        GuiComponent.blit(matrices, x, y, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
    }
}