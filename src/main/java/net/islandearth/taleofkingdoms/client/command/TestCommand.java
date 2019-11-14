package net.islandearth.taleofkingdoms.client.command;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.TaleOfKingdomsAPI;
import net.islandearth.taleofkingdoms.client.gui.ScreenContinueConquest;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

/**
 * An entirely useless class that is just here for testing.
 */
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

		System.out.println(ServerLifecycleHooks.getCurrentServer().getWorld(DimensionType.OVERWORLD));
		source.getEntity().world.addEntity(FakePlayerFactory.get(ServerLifecycleHooks.getCurrentServer().getWorld(DimensionType.OVERWORLD), new GameProfile(UUID.randomUUID(), "Farmer")));
		return 0;
	}
}
