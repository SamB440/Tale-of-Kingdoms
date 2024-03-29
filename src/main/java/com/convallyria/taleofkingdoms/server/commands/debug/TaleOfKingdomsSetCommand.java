package com.convallyria.taleofkingdoms.server.commands.debug;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.convallyria.taleofkingdoms.server.world.ServerConquestInstance;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.EnvType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.UUID;

public class TaleOfKingdomsSetCommand implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        return 1;
    }

    public static int setCoins(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ConquestInstance instance = TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().get();
        ServerPlayerEntity player = context.getSource().getPlayer();
        UUID playerUuid = player.getUuid();
        final GuildPlayer guildPlayer = instance.getPlayer(playerUuid);

        guildPlayer.setCoins(context.getArgument("coins", Integer.class));
        if (TaleOfKingdoms.getAPI().getEnvironment() == EnvType.SERVER) {
            ServerConquestInstance.sync(player, instance);
        }

        player.sendMessage(Text.literal("Your new balance is: " + guildPlayer.getCoins()), false);
        return 1;
    }

    public static int setWorthiness(CommandContext<ServerCommandSource> context) {
        ConquestInstance instance = TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().get();
        ServerPlayerEntity player = context.getSource().getPlayer();
        UUID playerUuid = player.getUuid();
        final GuildPlayer guildPlayer = instance.getPlayer(playerUuid);

        guildPlayer.setWorthiness(context.getArgument("worthiness", Integer.class));
        if (TaleOfKingdoms.getAPI().getEnvironment() == EnvType.SERVER) {
            ServerConquestInstance.sync(player, instance);
        }

        player.sendMessage(Text.literal("Your new worthiness is: " + guildPlayer.getWorthiness()), false);
        return 1;
    }
}
