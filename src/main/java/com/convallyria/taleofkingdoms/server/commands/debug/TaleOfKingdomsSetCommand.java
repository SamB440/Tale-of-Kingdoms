package com.convallyria.taleofkingdoms.server.commands.debug;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.server.world.ServerConquestInstance;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.UUID;

public class TaleOfKingdomsSetCommand implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        return 1;
    }

    public static int setCoins(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().get();
        ServerPlayerEntity player = context.getSource().getPlayer();
        UUID playerUuid = player.getUuid();

        instance.setCoins(playerUuid, context.getArgument("coins", Integer.class));
        if (instance instanceof ServerConquestInstance serverConquestInstance) {
            serverConquestInstance.sync(player);
        }

        player.sendMessage(new LiteralText("Your new balance is: " + instance.getCoins(playerUuid)), false);
        return 1;
    }

    public static int setWorthiness(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().get();
        ServerPlayerEntity player = context.getSource().getPlayer();
        UUID playerUuid = player.getUuid();

        instance.setWorthiness(playerUuid, context.getArgument("worthiness", Integer.class));
        if (instance instanceof ServerConquestInstance serverConquestInstance) {
            serverConquestInstance.sync(player);
        }

        player.sendMessage(new LiteralText("Your new worthiness is: " + instance.getWorthiness(playerUuid)), false);
        return 1;
    }
}
