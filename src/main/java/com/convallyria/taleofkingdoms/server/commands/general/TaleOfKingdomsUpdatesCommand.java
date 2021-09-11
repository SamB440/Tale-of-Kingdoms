package com.convallyria.taleofkingdoms.server.commands.general;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.generic.UpdateScreen;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

public class TaleOfKingdomsUpdatesCommand implements Command<FabricClientCommandSource> {

    @Override
    public int run(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        MinecraftClient.getInstance().setScreen(new UpdateScreen(MinecraftClient.getInstance().player, (ClientConquestInstance) TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().get()));
        return 1;
    }
}