package com.convallyria.taleofkingdoms.client.gui.entity;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import com.convallyria.taleofkingdoms.client.gui.image.IImage;
import com.convallyria.taleofkingdoms.client.gui.image.Image;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.guild.BlacksmithEntity;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

public class BlacksmithScreen extends ScreenTOK {

    private final PlayerEntity player;
    private final List<IImage> images;
    private final BlacksmithEntity entity;
    private final ClientConquestInstance instance;

    public BlacksmithScreen(PlayerEntity player, BlacksmithEntity entity, ClientConquestInstance instance) {
        super("menu.taleofkingdoms.blacksmith.name");
        this.player = player;
        this.images = Collections.singletonList(new Image(this, new Identifier(TaleOfKingdoms.MODID, "textures/gui/crafting.png"), 360, 100, new int[]{230, 230}));
        this.entity = entity;
        this.instance = instance;
    }

    @Override
    public void init() {
        super.init();
        this.addButton(new ButtonWidget(this.width / 2 - 40 , this.height / 2 + 35 , 75, 20, new LiteralText("Buy Item"), (button) -> this.onClose()));
        this.addButton(new ButtonWidget(this.width / 2 + 120, this.height / 2 + 15 , 75, 20, new LiteralText("Sell Item"), (button) -> this.onClose()));
        this.addButton(new ButtonWidget(this.width / 2 - 200, this.height / 2 - 100 , 75, 20, new LiteralText("Back"), (button) -> this.onClose()));
        this.addButton(new ButtonWidget(this.width / 2 + 120, this.height / 2 - 100 , 75, 20, new LiteralText("Next"), (button) -> this.onClose()));

        this.addButton(new ButtonWidget(this.width / 2 - 200 , this.height / 2 + 15 , 75, 20, new LiteralText("Exit"), (button) -> {
            Translations.SHOP_CLOSE.send(player);
            this.onClose();
        }));
    }


    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float delta) {
        images.forEach(image -> image.render(stack, this));
        super.render(stack, mouseX, mouseY, delta);
        drawCenteredString(stack, this.textRenderer, "Shop Menu - Total Money: " + instance.getCoins() + " Gold Coins", this.width / 2, this.height / 4 - 25, 0xFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
