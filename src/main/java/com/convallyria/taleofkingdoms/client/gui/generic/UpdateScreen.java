package com.convallyria.taleofkingdoms.client.gui.generic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import com.convallyria.taleofkingdoms.client.gui.image.Image;
import com.convallyria.taleofkingdoms.client.gui.image.ScaleSize;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class UpdateScreen extends ScreenTOK {

    // Buttons
    private final PlayerEntity player;
    private final ClientConquestInstance instance;

    private final List<TextRender> textRenders;

    private static final ImmutableList<ScaleSize> SCALE_SIZES = ImmutableList.of(
            new ScaleSize(1, 810, 265),
            new ScaleSize(2, 310, 113),
            new ScaleSize(3, 170, 55),
            new ScaleSize(4, 100, 27));

    private static final ImmutableList<ScaleSize> SCALE_SIZES_TWO = ImmutableList.of(
            new ScaleSize(1, 1000, 265),
            new ScaleSize(2, 540, 113),
            new ScaleSize(3, 360, 55),
            new ScaleSize(4, 275, 27));

    public UpdateScreen(PlayerEntity player, ClientConquestInstance instance) {
        super("menu.taleofkingdoms.update.name");
        this.player = player;
        this.instance = instance;
        this.textRenders = new ArrayList<>();
    }

    @Override
    public void init() {
        super.init();
        textRenders.clear();

        this.addDrawableChild(new ButtonWidget(this.width / 2, this.height / 2 + 40, 45, 20, new LiteralText("Exit"), button -> this.onClose()));
        int currentY = this.height / 4;
        int currentX = this.width / 2 - 100;

        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("shop.json");
        try (BufferedReader input = new BufferedReader(new InputStreamReader(in))) {
            String update;
            while ((update = input.readLine()) != null) {
                TextRender textRender = new TextRender(currentX, currentY, update);
                textRenders.add(textRender);

                currentY = currentY + 20;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!images.isEmpty()) images.clear();
        addImage(new Image(new Identifier(TaleOfKingdoms.MODID, "textures/gui/menu.png"), this.width / 2 - 200, this.height / 6, new int[]{400, 256}));
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float delta) {
        super.render(stack, mouseX, mouseY, delta);
        drawCenteredText(stack, this.textRenderer, "Tale of Kingdoms Updates", this.width / 2, this.height / 4 - 25, 0xFFFFFF);
        drawCenteredText(stack, this.textRenderer, new LiteralText("Repair the guild to see it update!").formatted(Formatting.ITALIC), this.width / 2, this.height / 2, 0xFFFFFF);
        textRenders.forEach(textRender -> drawStringWithShadow(stack, this.textRenderer, textRender.text, textRender.x, textRender.y, 0xFFFFFF));
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    public record TextRender(int x, int y, String text) { }
}
