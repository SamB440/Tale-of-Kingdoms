package com.convallyria.taleofkingdoms.server.commands.debug;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class TaleOfKingdomsGetCommand implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        return 1;
    }

    public static int getCoins(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().get();
        ServerPlayer player = context.getSource().getPlayerOrException();
        UUID playerUuid = player.getUUID();

        player.displayClientMessage(new TextComponent("Your balance is: " + instance.getCoins(playerUuid)), false);
        return 1;
    }

    public static int getWorthiness(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().get();
        ServerPlayer player = context.getSource().getPlayerOrException();
        UUID playerUuid = player.getUUID();

        player.displayClientMessage(new TextComponent("Your worthiness is: " + instance.getWorthiness(playerUuid)), false);
        return 1;
    }

    public static int getHasRebuilt(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().get();
        ServerPlayer player = context.getSource().getPlayerOrException();

        player.displayClientMessage(new TextComponent("Has the guild been rebuilt? " + instance.hasRebuilt()), false);
        return 1;
    }

    public static int getHasAttacked(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().get();
        ServerPlayer player = context.getSource().getPlayerOrException();

        player.displayClientMessage(new TextComponent("Has the guild been attacked? " + instance.hasAttacked(player.getUUID())), false);
        return 1;
    }
}