package net.islandearth.taleofkingdoms.client.command;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.TaleOfKingdomsAPI;
import net.islandearth.taleofkingdoms.client.gui.GUIStartConquest;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class TestCommand extends CommandBase {

    private final String COMMAND_NAME = "toktest";
    private final String COMMAND_HELP = "test for tok";

	@Override
	public String getName() {
		return COMMAND_NAME;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return COMMAND_HELP;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		String worldName = Minecraft.getMinecraft().getIntegratedServer().getFolderName();
		File file = new File(TaleOfKingdoms.getAPI().map(TaleOfKingdomsAPI::getDataFolder).orElseThrow(() -> new IllegalArgumentException("API not present")) + "worlds/" + worldName + ".conquestworld");
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				Minecraft.getMinecraft().displayGuiScreen(new GUIStartConquest(worldName, file));
			}
			
		}, 1000);
		
	}

}
