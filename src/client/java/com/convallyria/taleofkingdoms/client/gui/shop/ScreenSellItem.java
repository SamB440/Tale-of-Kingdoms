package com.convallyria.taleofkingdoms.client.gui.shop;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.ShopEntity;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.c2s.ToggleSellGuiPacket;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class ScreenSellItem extends AbstractContainerScreen<AbstractContainerMenu> {
    //A path to the gui texture. In this example we use the texture from the dispenser
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TaleOfKingdoms.MODID, "textures/gui/guisell.png");

    private final Inventory playerInventory;

    public ScreenSellItem(AbstractContainerMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.playerInventory = inventory;
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        context.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        renderTooltip(context, mouseX, mouseY);
    }

    @Override
    public void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
        context.drawString(font, Component.literal("Total Money:"), this.inventoryLabelX + 20, this.inventoryLabelY - 50, 4210752, false);
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        Optional<ConquestInstance> instance = api.getConquestInstanceStorage().mostRecentInstance();
        int x = this.inventoryLabelX + 25;
        int y = this.inventoryLabelY - 40;
        if (instance.isPresent()) {
            final GuildPlayer guildPlayer = instance.get().getPlayer(playerInventory.player);
            int coins = guildPlayer.getCoins();
            context.drawString(font, Component.literal(coins + " Gold Coins"), x, y, 4210752, false);
        }
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }

    @Override
    public void onClose() {
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        api.getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            Level world = playerInventory.player.level();
            for (EntityType<? extends ShopEntity> shopEntity : EntityTypes.SHOP_ENTITIES) {
                instance.search(playerInventory.player, world, shopEntity).ifPresent(entity -> deleteBlock(api, entity));
            }
        });
        super.onClose();
    }

    protected void deleteBlock(TaleOfKingdomsAPI api, ShopEntity entity) {
        if (Minecraft.getInstance().getSingleplayerServer() == null) {
            api.getClientPacket(Packets.TOGGLE_SELL_GUI)
                    .sendPacket(playerInventory.player, new ToggleSellGuiPacket(true, entity.getGUIType()));
            return;
        }

        api.getScheduler().queue(server -> {
            BlockPos pos = entity.blockPosition().offset(0, 2, 0);
            server.overworld().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }, 1);
    }
}

