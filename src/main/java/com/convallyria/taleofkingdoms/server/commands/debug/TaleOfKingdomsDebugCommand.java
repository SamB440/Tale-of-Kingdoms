package com.convallyria.taleofkingdoms.server.commands.debug;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.world.entity.Entity;

public class TaleOfKingdomsDebugCommand implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Entity entity = context.getSource().getEntity();
        if (entity != null) {
            String message = "{\"text\":\"List of debug commands: invoke, set\"}";
            entity.sendSystemMessage(ComponentUtils.updateForEntity(context.getSource(), TaleOfKingdoms.parse(new StringReader(message), entity.registryAccess()), entity, 0));
            return 1;
        }
        return 0;
    }
}
