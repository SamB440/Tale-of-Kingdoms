package com.convallyria.taleofkingdoms.server.commands.debug;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import com.convallyria.taleofkingdoms.server.world.ServerConquestInstance;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.EnvType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Texts;

import java.util.UUID;

public class TaleOfKingdomsInvokeCommand implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Entity entity = context.getSource().getEntity();
        if (entity != null) {
            String message = "{\"text\":\"List of invoke events: saveVillagers, guildAttack\"}";
            entity.sendMessage(Texts.parse(context.getSource(), TaleOfKingdoms.parse(new StringReader(message)), entity, 0));
            return 1;
        }
        return 0;
    }

    public static int invokeSaveVillagers(CommandContext<ServerCommandSource> context) {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();

            Translations.LONE_HELP.send(player);

            //BlockPos structureLocation = player.getWorld().locateStructure(TaleOfKingdoms.REFICULE_VILLAGE_STRUCTURE, player.getBlockPos(), 100, false);
            //String message = "[\"\",{\"text\":\"Do you wish to \"},{\"text\":\"teleport\",\"bold\":true,\"underlined\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tp " + structureLocation.getX() + " 100 " + structureLocation.getZ() + "\"}},{\"text\":\" to the village?\"}]";
            //player.sendSystemMessage(Texts.parse(context.getSource(), TaleOfKingdoms.parse(new StringReader(message)), player, 0), Util.NIL_UUID);

            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int invokeGuildAttack(CommandContext<ServerCommandSource> context) {
        try {
            ConquestInstance instance = TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().get();
            ServerPlayerEntity player = context.getSource().getPlayer();
            UUID playerUuid = player.getUuid();
            final GuildPlayer guildPlayer = instance.getPlayer(playerUuid);

            // Pre-requisites for attack

            // Requires at least 750 worthiness
            guildPlayer.setWorthiness(guildPlayer.getWorthiness() + 750);
            // The guild must not be currently under attack
            instance.setUnderAttack(false);

            // The guild must not be rebuilt
            guildPlayer.setHasRebuiltGuild(false);

            instance.attack(player, player.getServerWorld());

            guildPlayer.setWorthiness(guildPlayer.getWorthiness() - 750);

            if (TaleOfKingdoms.getAPI().getEnvironment() == EnvType.SERVER) {
                ServerConquestInstance.sync(player, instance);
            }

            TaleOfKingdoms.LOGGER.debug("Guild attack forcefully activated!");
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            TaleOfKingdoms.LOGGER.debug("Guild attack forceful activation failed!");
            return 0;
        }
    }
}
