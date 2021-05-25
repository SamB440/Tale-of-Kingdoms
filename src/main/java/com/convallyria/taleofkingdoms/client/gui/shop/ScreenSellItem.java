package com.convallyria.taleofkingdoms.client.gui.shop;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class ScreenSellItem extends HandledScreen<ScreenHandler> {
    //A path to the gui texture. In this example we use the texture from the dispenser
    private static final Identifier TEXTURE = new Identifier(TaleOfKingdoms.MODID, "textures/gui/guisell.png");

    public ScreenSellItem(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        client.getTextureManager().bindTexture(TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    public void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        this.textRenderer.draw(matrices, new LiteralText("Total Money:"), (float)this.playerInventoryTitleX + 20, (float)this.playerInventoryTitleY - 50, 4210752);
        TaleOfKingdoms.getAPI().ifPresent(api -> {
            Optional<ConquestInstance> instance = api.getConquestInstanceStorage().mostRecentInstance();
            int x = this.playerInventoryTitleX + 25;
            int y = this.playerInventoryTitleY - 40;
            if (instance.isPresent()) {
                int coins = instance.get().getCoins(playerInventory.player.getUuid());
                this.textRenderer.draw(matrices, new LiteralText(coins + " Gold Coins"), x, y, 4210752);
            }
        });
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }

    @Override
    public void onClose() {
        TaleOfKingdoms.getAPI().ifPresent(api -> api.getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            World world = playerInventory.player.world;
            instance.getGuildEntity(world, EntityTypes.BLACKSMITH).ifPresent(entity -> {
                deleteBlock(api, entity);
            });
            instance.getGuildEntity(playerInventory.player.world, EntityTypes.FOODSHOP).ifPresent(entity -> {
                deleteBlock(api, entity);
            });
        }));
        super.onClose();
    }

    protected void deleteBlock(TaleOfKingdomsAPI api, Entity entity) {
        if (MinecraftClient.getInstance().getServer() == null) {
            api.getClientHandler(TaleOfKingdoms.TOGGLE_SELL_GUI_PACKET_ID)
                    .handleOutgoingPacket(TaleOfKingdoms.TOGGLE_SELL_GUI_PACKET_ID,
                            playerInventory.player,
                            null, true);
            return;
        }
        api.getScheduler().queue(server -> {
            BlockPos pos = entity.getBlockPos().add(0, 2, 0);
            server.getOverworld().setBlockState(pos, Blocks.AIR.getDefaultState());
        }, 1);
    }
}

