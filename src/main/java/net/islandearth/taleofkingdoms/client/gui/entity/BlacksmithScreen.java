package net.islandearth.taleofkingdoms.client.gui.entity;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.client.gui.ScreenTOK;
import net.islandearth.taleofkingdoms.client.gui.image.IImage;
import net.islandearth.taleofkingdoms.client.gui.image.Image;
import net.islandearth.taleofkingdoms.common.entity.guild.BlacksmithEntity;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.List;

public class BlacksmithScreen extends ScreenTOK {

    private final PlayerEntity player;
    private final List<IImage> images;
    private final BlacksmithEntity entity;
    private final ConquestInstance instance;

    public BlacksmithScreen(PlayerEntity player, BlacksmithEntity entity, ConquestInstance instance) {
        super("taleofkingdoms.menu.blacksmith.name");
        this.player = player;
        this.images = Arrays.asList(new Image(this, new ResourceLocation(TaleOfKingdoms.MODID, "textures/gui/crafting.png"), 128, 5, 230));
        this.entity = entity;
        this.instance = instance;
    }

    @Override
    public void init() {
        super.init();
        //TODO
    }

    @Override
    public void render(int par1, int par2, float par3) {
        super.render(par1, par2, par3);
        images.forEach(IImage::render);
        ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().getConquestInstance(Minecraft.getInstance().getIntegratedServer().getFolderName()).get();
        this.drawCenteredString(this.font, "Shop Menu - Total Money: " + instance.getCoins() + " Gold Coins", this.width / 2, this.height / 4 - 25, 0xFFFFFF);
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
