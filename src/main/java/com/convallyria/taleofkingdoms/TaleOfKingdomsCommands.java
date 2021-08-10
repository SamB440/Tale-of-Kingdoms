package com.convallyria.taleofkingdoms;

import com.convallyria.taleofkingdoms.client.commands.TaleOfKingdomsCommand;
import com.convallyria.taleofkingdoms.server.commands.debug.TaleOfKingdomsAddCommand;
import com.convallyria.taleofkingdoms.server.commands.debug.TaleOfKingdomsDebugCommand;
import com.convallyria.taleofkingdoms.server.commands.debug.TaleOfKingdomsGetCommand;
import com.convallyria.taleofkingdoms.server.commands.debug.TaleOfKingdomsInvokeCommand;
import com.convallyria.taleofkingdoms.server.commands.debug.TaleOfKingdomsSetCommand;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class TaleOfKingdomsCommands {
    public TaleOfKingdomsCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> { // Register commands
            // Base node /taleofkingdoms
            LiteralCommandNode<CommandSourceStack> baseNode = Commands
                    .literal(TaleOfKingdoms.MODID)
                    .executes(new TaleOfKingdomsCommand())
                    .build();

            // Debug node /taleofkingdoms debug

            LiteralCommandNode<CommandSourceStack> debugNode = Commands
                    .literal("debug")
                    .requires(p -> p.hasPermission(4))
                    .executes(new TaleOfKingdomsDebugCommand())
                    .build();

            // Add node /taleofkingdoms debug add [coins|worthiness] [integer]

            LiteralCommandNode<CommandSourceStack> addNode = Commands
                    .literal("add")
                    .executes(new TaleOfKingdomsAddCommand())
                    .build();

            LiteralCommandNode<CommandSourceStack> addCoinsNode = Commands
                    .literal("coins")
                    .build();

            ArgumentCommandNode<CommandSourceStack, Integer> addCoinsArgumentNode = Commands
                    .argument("coins", IntegerArgumentType.integer())
                    .executes(TaleOfKingdomsAddCommand::addCoins)
                    .build();

            LiteralCommandNode<CommandSourceStack> addWorthinessNode = Commands
                    .literal("worthiness")
                    .build();

            ArgumentCommandNode<CommandSourceStack, Integer> addWorthinessArgumentNode = Commands
                    .argument("worthiness", IntegerArgumentType.integer())
                    .executes(TaleOfKingdomsAddCommand::addWorthiness)
                    .build();

            // Get node /taleofkingdoms debug get [coins|worthiness]

            LiteralCommandNode<CommandSourceStack> getNode = Commands
                    .literal("get")
                    .executes(new TaleOfKingdomsGetCommand())
                    .build();

            LiteralCommandNode<CommandSourceStack> getCoinsNode = Commands
                    .literal("coins")
                    .executes(TaleOfKingdomsGetCommand::getCoins)
                    .build();

            LiteralCommandNode<CommandSourceStack> getWorthinessNode = Commands
                    .literal("worthiness")
                    .executes(TaleOfKingdomsGetCommand::getWorthiness)
                    .build();

            LiteralCommandNode<CommandSourceStack> getHasRebuiltNode = Commands
                    .literal("hasRebuilt")
                    .executes(TaleOfKingdomsGetCommand::getHasRebuilt)
                    .build();

            LiteralCommandNode<CommandSourceStack> getHasAttackedNode = Commands
                    .literal("hasAttacked")
                    .executes(TaleOfKingdomsGetCommand::getHasAttacked)
                    .build();

            // Invoke node /taleofkingdoms debug invoke [saveVillagers|guildAttack]

            LiteralCommandNode<CommandSourceStack> invokeNode = Commands
                    .literal("invoke")
                    .executes(new TaleOfKingdomsInvokeCommand())
                    .build();

            LiteralCommandNode<CommandSourceStack> invokeSaveVillagersNode = Commands
                    .literal("saveVillagers")
                    .executes(TaleOfKingdomsInvokeCommand::invokeSaveVillagers)
                    .build();

            LiteralCommandNode<CommandSourceStack> invokeGuildAttackNode = Commands
                    .literal("guildAttack")
                    .executes(TaleOfKingdomsInvokeCommand::invokeGuildAttack)
                    .build();

            // Set node /taleofkingdoms debug set [coins|worthiness] [integer]

            LiteralCommandNode<CommandSourceStack> setNode = Commands
                    .literal("set")
                    .executes(new TaleOfKingdomsSetCommand())
                    .build();

            LiteralCommandNode<CommandSourceStack> setCoinsNode = Commands
                    .literal("coins")
                    .build();

            ArgumentCommandNode<CommandSourceStack, Integer> setCoinsArgumentNode = Commands
                    .argument("coins", IntegerArgumentType.integer())
                    .executes(TaleOfKingdomsSetCommand::setCoins)
                    .build();

            LiteralCommandNode<CommandSourceStack> setWorthinessNode = Commands
                    .literal("worthiness")
                    .build();

            ArgumentCommandNode<CommandSourceStack, Integer> setWorthinessArgumentNode = Commands
                    .argument("worthiness", IntegerArgumentType.integer())
                    .executes(TaleOfKingdomsSetCommand::setWorthiness)
                    .build();

            //Now stitch them together
            dispatcher.getRoot().addChild(baseNode);
            baseNode.addChild(debugNode);

            // Add node
            debugNode.addChild(addNode);
            addNode.addChild(addCoinsNode);
            addCoinsNode.addChild(addCoinsArgumentNode);
            addNode.addChild(addWorthinessNode);
            addWorthinessNode.addChild(addWorthinessArgumentNode);

            // Get node
            debugNode.addChild(getNode);
            getNode.addChild(getCoinsNode);
            getNode.addChild(getWorthinessNode);
            getNode.addChild(getHasRebuiltNode);
            getNode.addChild(getHasAttackedNode);

            // Invoke node
            debugNode.addChild(invokeNode);
            invokeNode.addChild(invokeSaveVillagersNode);
            invokeNode.addChild(invokeGuildAttackNode);

            // Set node
            debugNode.addChild(setNode);
            setNode.addChild(setCoinsNode);
            setCoinsNode.addChild(setCoinsArgumentNode);
            setNode.addChild(setWorthinessNode);
            setWorthinessNode.addChild(setWorthinessArgumentNode);
        });
    }
}
