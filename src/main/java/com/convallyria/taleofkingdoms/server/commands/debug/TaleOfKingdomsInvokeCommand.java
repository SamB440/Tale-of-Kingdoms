package com.convallyria.taleofkingdoms.server.commands.debug;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.server.world.ServerConquestInstance;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Texts;
import net.minecraft.util.Util;

import java.util.UUID;

public class TaleOfKingdomsInvokeCommand implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        Entity entity = context.getSource().getEntity();
        if (entity != null) {
            String message = "{\"text\":\"List of invoke events: saveVillagers, guildAttack\"}";
            entity.sendSystemMessage(Texts.parse(context.getSource(), TaleOfKingdoms.parse(new StringReader(message)), entity, 0), Util.NIL_UUID);
            return 1;
        }
        return 0;
    }

    public static int invokeSaveVillagers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            ServerPlayerEntity player = context.getSource().getPlayer();

            Translations.LONE_HELP.send(player);

            //BlockPos structureLocation = player.getWorld().locateStructure(TaleOfKingdoms.REFICULE_VILLAGE_STRUCTURE, player.getBlockPos(), 100, false);
            //String message = "[\"\",{\"text\":\"Do you wish to \"},{\"text\":\"teleport\",\"bold\":true,\"underlined\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tp " + structureLocation.getX() + " 100 " + structureLocation.getZ() + "\"}},{\"text\":\" to the village?\"}]";
            //player.sendSystemMessage(Texts.parse(context.getSource(), TaleOfKingdoms.parse(new StringReader(message)), player, 0), Util.NIL_UUID);

            return 1;
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int invokeGuildAttack(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        try {
            ConquestInstance instance = TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().get();
            ServerPlayerEntity player = context.getSource().getPlayer();
            UUID playerUuid = player.getUuid();

            // Pre-requisites for attack

            // Requires at least 750 worthiness
            instance.addWorthiness(playerUuid, 750);
            // The guild must not be currently under attack
            instance.setUnderAttack(false);

            // The guild must not be rebuilt
            instance.setRebuilt(false);

            instance.attack(player, player.getWorld());

            instance.setWorthiness(playerUuid, instance.getWorthiness(playerUuid) - 750);

            if (instance instanceof ServerConquestInstance serverConquestInstance) {
                serverConquestInstance.sync(player);
            }

            TaleOfKingdoms.LOGGER.debug("Guild attack forcefully activated!");
            return 1;
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            TaleOfKingdoms.LOGGER.debug("Guild attack forceful activation failed!");
            return 0;
        }
    }
}
