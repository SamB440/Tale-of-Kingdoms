package com.convallyria.taleofkingdoms;

import com.convallyria.taleofkingdoms.server.commands.TaleOfKingdomsCommand;
import com.convallyria.taleofkingdoms.server.commands.debug.TaleOfKingdomsAddCommand;
import com.convallyria.taleofkingdoms.server.commands.debug.TaleOfKingdomsDebugCommand;
import com.convallyria.taleofkingdoms.server.commands.debug.TaleOfKingdomsGetCommand;
import com.convallyria.taleofkingdoms.server.commands.debug.TaleOfKingdomsInvokeCommand;
import com.convallyria.taleofkingdoms.server.commands.debug.TaleOfKingdomsSetCommand;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class TaleOfKingdomsCommands {
    public TaleOfKingdomsCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, registrationEnvironment) -> { // Register commands
            // Base node /taleofkingdoms
            LiteralCommandNode<ServerCommandSource> baseNode = CommandManager
                    .literal(TaleOfKingdoms.MODID)
                    .executes(new TaleOfKingdomsCommand())
                    .build();

            // Debug node /taleofkingdoms debug

            LiteralCommandNode<ServerCommandSource> debugNode = CommandManager
                    .literal("debug")
                    .requires(p -> p.hasPermissionLevel(4))
                    .executes(new TaleOfKingdomsDebugCommand())
                    .build();

            // Add node /taleofkingdoms debug add [coins|worthiness] [integer]

            LiteralCommandNode<ServerCommandSource> addNode = CommandManager
                    .literal("add")
                    .executes(new TaleOfKingdomsAddCommand())
                    .build();

            LiteralCommandNode<ServerCommandSource> addCoinsNode = CommandManager
                    .literal("coins")
                    .build();

            ArgumentCommandNode<ServerCommandSource, Integer> addCoinsArgumentNode = CommandManager
                    .argument("coins", IntegerArgumentType.integer())
                    .executes(TaleOfKingdomsAddCommand::addCoins)
                    .build();

            LiteralCommandNode<ServerCommandSource> addWorthinessNode = CommandManager
                    .literal("worthiness")
                    .build();

            ArgumentCommandNode<ServerCommandSource, Integer> addWorthinessArgumentNode = CommandManager
                    .argument("worthiness", IntegerArgumentType.integer())
                    .executes(TaleOfKingdomsAddCommand::addWorthiness)
                    .build();

            // Get node /taleofkingdoms debug get [coins|worthiness]

            LiteralCommandNode<ServerCommandSource> getNode = CommandManager
                    .literal("get")
                    .executes(new TaleOfKingdomsGetCommand())
                    .build();

            LiteralCommandNode<ServerCommandSource> getCoinsNode = CommandManager
                    .literal("coins")
                    .executes(TaleOfKingdomsGetCommand::getCoins)
                    .build();

            LiteralCommandNode<ServerCommandSource> getWorthinessNode = CommandManager
                    .literal("worthiness")
                    .executes(TaleOfKingdomsGetCommand::getWorthiness)
                    .build();

            LiteralCommandNode<ServerCommandSource> getHasRebuiltNode = CommandManager
                    .literal("hasRebuilt")
                    .executes(TaleOfKingdomsGetCommand::getHasRebuilt)
                    .build();

            LiteralCommandNode<ServerCommandSource> getHasAttackedNode = CommandManager
                    .literal("hasAttacked")
                    .executes(TaleOfKingdomsGetCommand::getHasAttacked)
                    .build();

            // Invoke node /taleofkingdoms debug invoke [saveVillagers|guildAttack]

            LiteralCommandNode<ServerCommandSource> invokeNode = CommandManager
                    .literal("invoke")
                    .executes(new TaleOfKingdomsInvokeCommand())
                    .build();

            LiteralCommandNode<ServerCommandSource> invokeSaveVillagersNode = CommandManager
                    .literal("saveVillagers")
                    .executes(TaleOfKingdomsInvokeCommand::invokeSaveVillagers)
                    .build();

            LiteralCommandNode<ServerCommandSource> invokeGuildAttackNode = CommandManager
                    .literal("guildAttack")
                    .executes(TaleOfKingdomsInvokeCommand::invokeGuildAttack)
                    .build();

            // Set node /taleofkingdoms debug set [coins|worthiness] [integer]

            LiteralCommandNode<ServerCommandSource> setNode = CommandManager
                    .literal("set")
                    .executes(new TaleOfKingdomsSetCommand())
                    .build();

            LiteralCommandNode<ServerCommandSource> setCoinsNode = CommandManager
                    .literal("coins")
                    .build();

            ArgumentCommandNode<ServerCommandSource, Integer> setCoinsArgumentNode = CommandManager
                    .argument("coins", IntegerArgumentType.integer())
                    .executes(TaleOfKingdomsSetCommand::setCoins)
                    .build();

            LiteralCommandNode<ServerCommandSource> setWorthinessNode = CommandManager
                    .literal("worthiness")
                    .build();

            ArgumentCommandNode<ServerCommandSource, Integer> setWorthinessArgumentNode = CommandManager
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
