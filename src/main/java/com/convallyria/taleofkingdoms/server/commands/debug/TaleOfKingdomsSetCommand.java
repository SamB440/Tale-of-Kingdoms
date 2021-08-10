package com.convallyria.taleofkingdoms.server.commands.debug;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.ServerConquestInstance;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class TaleOfKingdomsSetCommand implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        return 1;
    }

    public static int setCoins(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().get();
        ServerPlayer player = context.getSource().getPlayerOrException();
        UUID playerUuid = player.getUUID();

        instance.setCoins(playerUuid, context.getArgument("coins", Integer.class));
        if (instance instanceof ServerConquestInstance serverConquestInstance) {
            serverConquestInstance.sync(player, null);
        }

        player.sendMessage(new TextComponent("Your new balance is: " + instance.getCoins(playerUuid)), null);
        return 1;
    }

    public static int setWorthiness(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().get();
        ServerPlayer player = context.getSource().getPlayerOrException();
        UUID playerUuid = player.getUUID();

        instance.setWorthiness(playerUuid, context.getArgument("worthiness", Integer.class));
        if (instance instanceof ServerConquestInstance serverConquestInstance) {
            serverConquestInstance.sync(player, null);
        }

        player.sendMessage(new TextComponent("Your new worthiness is: " + instance.getWorthiness(playerUuid)), null);
        return 1;
    }
}
