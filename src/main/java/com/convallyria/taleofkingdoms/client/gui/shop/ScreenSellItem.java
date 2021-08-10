package com.convallyria.taleofkingdoms.client.gui.shop;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.Optional;

public class ScreenSellItem extends AbstractContainerScreen<AbstractContainerMenu> {
    //A path to the gui texture. In this example we use the texture from the dispenser
    private static final ResourceLocation TEXTURE = new ResourceLocation(TaleOfKingdoms.MODID, "textures/gui/guisell.png");

    private final Inventory playerInventory;

    public ScreenSellItem(AbstractContainerMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.playerInventory = inventory;
    }

    @Override
    protected void renderBg(PoseStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        blit(matrices, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        renderTooltip(matrices, mouseX, mouseY);
    }

    @Override
    public void renderLabels(PoseStack matrices, int mouseX, int mouseY) {
        this.font.draw(matrices, new TextComponent("Total Money:"), (float)this.inventoryLabelX + 20, (float)this.inventoryLabelY - 50, 4210752);
        TaleOfKingdoms.getAPI().ifPresent(api -> {
            Optional<ConquestInstance> instance = api.getConquestInstanceStorage().mostRecentInstance();
            int x = this.inventoryLabelX + 25;
            int y = this.inventoryLabelY - 40;
            if (instance.isPresent()) {
                int coins = instance.get().getCoins(playerInventory.player.getUUID());
                this.font.draw(matrices, new TextComponent(coins + " Gold Coins"), x, y, 4210752);
            }
        });
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }

    @Override
    public void onClose() {
        TaleOfKingdoms.getAPI().ifPresent(api -> api.getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            Level world = playerInventory.player.level;
            instance.getGuildEntity(world, EntityTypes.BLACKSMITH).ifPresent(entity -> {
                deleteBlock(api, entity);
            });
            instance.getGuildEntity(playerInventory.player.level, EntityTypes.FOODSHOP).ifPresent(entity -> {
                deleteBlock(api, entity);
            });
        }));
        super.onClose();
    }

    protected void deleteBlock(TaleOfKingdomsAPI api, Entity entity) {
        if (Minecraft.getInstance().getSingleplayerServer() == null) {
            api.getClientHandler(TaleOfKingdoms.TOGGLE_SELL_GUI_PACKET_ID)
                    .handleOutgoingPacket(TaleOfKingdoms.TOGGLE_SELL_GUI_PACKET_ID,
                            playerInventory.player,
                            null, true);
            return;
        }
        api.getScheduler().queue(server -> {
            BlockPos pos = entity.blockPosition().offset(0, 2, 0);
            server.overworld().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }, 1);
    }
}

