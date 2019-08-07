package net.islandearth.taleofkingdoms.client.command;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.TaleOfKingdomsAPI;
import net.islandearth.taleofkingdoms.client.gui.ScreenContinueConquest;
import net.islandearth.taleofkingdoms.client.gui.ScreenStartConquest;
import net.islandearth.taleofkingdoms.common.entity.EntityFarmer;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class TestCommand {
    
    public TestCommand(CommandDispatcher<CommandSource> commandDispatcher) {
    	commandDispatcher.register(Commands.literal("toktest").requires(source -> source.hasPermissionLevel(2)).executes(context -> execute(context.getSource()))
    			.then(Commands.argument("2", IntegerArgumentType.integer())
    			.executes(context -> execute_2(context.getSource()))));
	}

	private static int execute(CommandSource source) {
		String worldName = Minecraft.getInstance().getIntegratedServer().getFolderName();
		File file = new File(TaleOfKingdoms.getAPI().map(TaleOfKingdomsAPI::getDataFolder).orElseThrow(() -> new IllegalArgumentException("API not present")) + "worlds/" + worldName + ".conquestworld");
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				Minecraft.getInstance().displayGuiScreen(new ScreenContinueConquest(TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().getConquestInstance(worldName).get()));
			}
			
		}, 1000);
		
		return 0;
	}
	private static int execute_2(CommandSource source) {
		/*String worldName = Minecraft.getInstance().getIntegratedServer().getFolderName();
		File file = new File(TaleOfKingdoms.getAPI().map(TaleOfKingdomsAPI::getDataFolder).orElseThrow(() -> new IllegalArgumentException("API not present")) + "worlds/" + worldName + ".conquestworld");
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					Minecraft.getInstance().displayGuiScreen(new ScreenStartConquest(worldName, file, source.asPlayer()));
				} catch (CommandSyntaxException e) {
					e.printStackTrace();
				}
			}
			
		}, 1000);
		
		return 0;*/
		
		source.getEntity().world.addEntity(new EntityFarmer(source.getEntity().world));
		return 0;
	}
}
