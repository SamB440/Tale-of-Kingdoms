package com.convallyria.taleofkingdoms.server.commands.debug;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.UUID;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class TaleOfKingdomsGetCommand implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        return 1;
    }

    public static int getCoins(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ConquestInstance instance = TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().get();
        ServerPlayer player = context.getSource().getPlayer();
        UUID playerUuid = player.getUUID();
        final GuildPlayer guildPlayer = instance.getPlayer(playerUuid);

        player.displayClientMessage(Component.literal("Your balance is: " + guildPlayer.getCoins()), false);
        return 1;
    }

    public static int getWorthiness(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ConquestInstance instance = TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().get();
        ServerPlayer player = context.getSource().getPlayer();
        UUID playerUuid = player.getUUID();
        final GuildPlayer guildPlayer = instance.getPlayer(playerUuid);

        player.displayClientMessage(Component.literal("Your worthiness is: " + guildPlayer.getWorthiness()), false);
        return 1;
    }

    public static int getHasRebuilt(CommandContext<CommandSourceStack> context) {
        ConquestInstance instance = TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().get();
        ServerPlayer player = context.getSource().getPlayer();
        final GuildPlayer guildPlayer = instance.getPlayer(player);

        player.displayClientMessage(Component.literal("Has the guild been rebuilt? " + guildPlayer.hasRebuiltGuild()), false);
        return 1;
    }

    public static int getHasAttacked(CommandContext<CommandSourceStack> context) {
        ConquestInstance instance = TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().get();
        ServerPlayer player = context.getSource().getPlayer();

        player.displayClientMessage(Component.literal("Has the guild been attacked? " + instance.hasAttacked(player.getUUID())), false);
        return 1;
    }
}