package com.convallyria.taleofkingdoms.server.commands;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.translation.Translations;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.convallyria.taleofkingdoms.common.world.guild.PlayerTitle;
import com.convallyria.taleofkingdoms.server.world.ServerConquestInstance;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.EnvType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class TaleOfKingdomsTitleCommand {

    public static int setTitle(CommandContext<CommandSourceStack> context, PlayerTitle title) {
        ConquestInstance instance = TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().orElse(null);
        ServerPlayer player = context.getSource().getPlayer();
        if (instance == null || player == null) return 0;
        final GuildPlayer guildPlayer = instance.getPlayer(player.getUUID());
        if (guildPlayer == null) return 0;

        guildPlayer.setTitle(title);
        if (TaleOfKingdoms.getAPI().getEnvironment() == EnvType.SERVER) {
            ServerConquestInstance.sync(player, instance);
        }

        Translations.TITLE_SET.send(player, title.getTranslation());
        return 1;
    }
}
