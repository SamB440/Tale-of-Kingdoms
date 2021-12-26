package com.convallyria.taleofkingdoms.server.commands.general;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.generic.cotton.update.UpdateGui;
import com.convallyria.taleofkingdoms.client.gui.generic.cotton.update.UpdateScreen;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

public class TaleOfKingdomsUpdatesCommand implements Command<FabricClientCommandSource> {

    @Override
    public int run(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        TaleOfKingdoms.getAPI().ifPresent(api -> {
            api.getScheduler().queue(server -> {
                api.executeOnMain(() -> MinecraftClient.getInstance().setScreen(new UpdateScreen(new UpdateGui())));
            }, 1);
        });
        return 1;
    }
}