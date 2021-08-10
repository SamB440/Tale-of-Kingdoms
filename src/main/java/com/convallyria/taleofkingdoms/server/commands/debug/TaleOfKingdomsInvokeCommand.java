package com.convallyria.taleofkingdoms.server.commands.debug;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.ServerConquestInstance;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.UUID;

public class TaleOfKingdomsInvokeCommand implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Entity entity = context.getSource().getEntity();
        if (entity != null) {
            String message = "{\"text\":\"List of invoke events: saveVillagers, guildAttack\"}";
            entity.sendMessage(ComponentUtils.updateForEntity(context.getSource(), TaleOfKingdoms.parse(new StringReader(message)), entity, 0), Util.NIL_UUID);
            return 1;
        }
        return 0;
    }

    public static int invokeSaveVillagers(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();

            Translations.LONE_HELP.send(player);

            BlockPos structureLocation = player.getLevel().findNearestMapFeature(TaleOfKingdoms.REFICULE_VILLAGE_STRUCTURE, player.blockPosition(), 100, false);
            String message = "[\"\",{\"text\":\"Do you wish to \"},{\"text\":\"teleport\",\"bold\":true,\"underlined\":true,\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tp " + structureLocation.getX() + " 100 " + structureLocation.getZ() + "\"}},{\"text\":\" to the village?\"}]";
            player.sendMessage(ComponentUtils.updateForEntity(context.getSource(), TaleOfKingdoms.parse(new StringReader(message)), player, 0), Util.NIL_UUID);

            return 1;
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int invokeGuildAttack(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        try {
            ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().get();
            ServerPlayerEntity player = context.getSource().getPlayer();
            UUID playerUuid = player.getUuid();

            // Pre-requisites for attack

            // Requires at least 750 worthiness
            instance.addWorthiness(playerUuid, 750);
            // The guild must not be currently under attack
            instance.setUnderAttack(false);

            // The guild must not be rebuilt
            instance.setRebuilt(false);

            instance.attack(player, player.getServerWorld());

            instance.setWorthiness(playerUuid, instance.getWorthiness(playerUuid) - 750);

            if (instance instanceof ServerConquestInstance serverConquestInstance) {
                serverConquestInstance.sync(player, null);
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
