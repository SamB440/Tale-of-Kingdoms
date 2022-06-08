package com.convallyria.taleofkingdoms.server.commands.debug;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.UUID;

public class TaleOfKingdomsGetCommand implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        return 1;
    }

    public static int getCoins(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ConquestInstance instance = TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().get();
        ServerPlayerEntity player = context.getSource().getPlayer();
        UUID playerUuid = player.getUuid();

        player.sendMessage(Text.literal("Your balance is: " + instance.getCoins(playerUuid)), false);
        return 1;
    }

    public static int getWorthiness(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ConquestInstance instance = TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().get();
        ServerPlayerEntity player = context.getSource().getPlayer();
        UUID playerUuid = player.getUuid();

        player.sendMessage(Text.literal("Your worthiness is: " + instance.getWorthiness(playerUuid)), false);
        return 1;
    }

    public static int getHasRebuilt(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ConquestInstance instance = TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().get();
        ServerPlayerEntity player = context.getSource().getPlayer();

        player.sendMessage(Text.literal("Has the guild been rebuilt? " + instance.hasRebuilt()), false);
        return 1;
    }

    public static int getHasAttacked(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ConquestInstance instance = TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().get();
        ServerPlayerEntity player = context.getSource().getPlayer();

        player.sendMessage(Text.literal("Has the guild been attacked? " + instance.hasAttacked(player.getUuid())), false);
        return 1;
    }
}