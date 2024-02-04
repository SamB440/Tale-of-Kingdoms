package com.convallyria.taleofkingdoms.server.commands.general;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;

public class TaleOfKingdomsUpdatesCommand implements Command<CommandSource> {

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        //MinecraftClient.getInstance().setScreen(new UpdateScreen(MinecraftClient.getInstance().player, (ClientConquestInstance) TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().get()));
        return 1;
    }
}