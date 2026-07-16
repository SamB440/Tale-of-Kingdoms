package com.convallyria.taleofkingdoms.server.commands.debug;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.convallyria.taleofkingdoms.server.world.ServerConquestInstance;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.EnvType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import java.util.UUID;

public class TaleOfKingdomsSetCommand implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        return 1;
    }

    public static int setCoins(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ConquestInstance instance = TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().get();
        ServerPlayer player = context.getSource().getPlayer();
        UUID playerUuid = player.getUUID();
        final GuildPlayer guildPlayer = instance.getPlayer(playerUuid);

        guildPlayer.setCoins(context.getArgument("coins", Integer.class));
        if (TaleOfKingdoms.getAPI().getEnvironment() == EnvType.SERVER) {
            ServerConquestInstance.sync(player, instance);
        }

        player.displayClientMessage(Component.literal("Your new balance is: " + guildPlayer.getCoins()), false);
        return 1;
    }

    public static int setWorthiness(CommandContext<CommandSourceStack> context) {
        ConquestInstance instance = TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().get();
        ServerPlayer player = context.getSource().getPlayer();
        UUID playerUuid = player.getUUID();
        final GuildPlayer guildPlayer = instance.getPlayer(playerUuid);

        guildPlayer.setWorthiness(context.getArgument("worthiness", Integer.class));
        if (TaleOfKingdoms.getAPI().getEnvironment() == EnvType.SERVER) {
            ServerConquestInstance.sync(player, instance);
        }

        player.displayClientMessage(Component.literal("Your new worthiness is: " + guildPlayer.getWorthiness()), false);
        return 1;
    }
}
