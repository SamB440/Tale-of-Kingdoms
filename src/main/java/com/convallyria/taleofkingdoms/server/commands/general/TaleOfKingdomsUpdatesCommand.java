package com.convallyria.taleofkingdoms.server.commands.general;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class TaleOfKingdomsUpdatesCommand implements Command<FabricClientCommandSource> {

    @Override
    public int run(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        //MinecraftClient.getInstance().setScreen(new UpdateScreen(MinecraftClient.getInstance().player, (ClientConquestInstance) TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().get()));
        return 1;
    }
}