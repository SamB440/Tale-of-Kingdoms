package com.convallyria.taleofkingdoms.client.gui;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.event.InventoryDrawCallback;
import com.convallyria.taleofkingdoms.common.listener.Listener;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class RenderListener extends Listener {

    public RenderListener() {
        InventoryDrawCallback.EVENT.register((gui, context, textRenderer) -> {
            TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                final UUID uuid = Minecraft.getInstance().player.getUUID();
                final GuildPlayer guildPlayer = instance.getPlayer(uuid);
                drawWithoutShadow(context, textRenderer, "Gold Coins: " + guildPlayer.getCoins(), gui.width / 2 - 50, gui.height / 2 - 100, 16763904);
            });
        });
    }

    private void drawWithoutShadow(GuiGraphics context, Font textRenderer, String text, int centerX, int y, int color) {
        context.drawString(textRenderer, text, (centerX - textRenderer.width(text) / 2), y, color, false);
    }
}
