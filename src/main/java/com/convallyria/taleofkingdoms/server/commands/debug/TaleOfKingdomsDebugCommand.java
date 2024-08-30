package com.convallyria.taleofkingdoms.server.commands.debug;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Texts;

public class TaleOfKingdomsDebugCommand implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Entity entity = context.getSource().getEntity();
        if (entity != null) {
            String message = "{\"text\":\"List of debug commands: invoke, set\"}";
            entity.sendMessage(Texts.parse(context.getSource(), TaleOfKingdoms.parse(new StringReader(message), entity.getRegistryManager()), entity, 0));
            return 1;
        }
        return 0;
    }
}
