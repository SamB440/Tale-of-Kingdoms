package net.islandearth.taleofkingdoms.client.gui.entity;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.client.gui.ScreenTOK;
import net.islandearth.taleofkingdoms.client.gui.image.IImage;
import net.islandearth.taleofkingdoms.client.gui.image.Image;
import net.islandearth.taleofkingdoms.common.entity.guild.BlacksmithEntity;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

public class BlacksmithScreen extends ScreenTOK {

    private final PlayerEntity player;
    private final List<IImage> images;
    private final BlacksmithEntity entity;
    private final ConquestInstance instance;

    public BlacksmithScreen(PlayerEntity player, BlacksmithEntity entity, ConquestInstance instance) {
        super("taleofkingdoms.menu.blacksmith.name");
        this.player = player;
        this.images = Collections.singletonList(new Image(this, new Identifier(TaleOfKingdoms.MODID, "textures/gui/crafting.png"), 128, 5, new int[]{230, 230}));
        this.entity = entity;
        this.instance = instance;
    }

    @Override
    public void init() {
        super.init();
        //TODO
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float delta) {
        super.render(stack, mouseX, mouseY, delta);
        images.forEach(image -> image.render(stack, this));
        ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().get();
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
