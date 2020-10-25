package net.islandearth.taleofkingdoms.common.listener;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.TaleOfKingdomsAPI;
import net.islandearth.taleofkingdoms.client.gui.generic.ScreenContinueConquest;
import net.islandearth.taleofkingdoms.client.gui.generic.ScreenStartConquest;
import net.islandearth.taleofkingdoms.common.event.RecipesUpdatedCallback;
import net.islandearth.taleofkingdoms.common.event.WorldSessionStartCallback;
import net.islandearth.taleofkingdoms.common.event.WorldStopCallback;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class StartWorldListener extends Listener {

    private String worldName;
    private boolean joined;

    public StartWorldListener() {
        WorldStopCallback.EVENT.register(() -> {
            if (!joined) return;
            if (!TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().isPresent()) return;

            ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().mostRecentInstance().get();
            File file = new File(TaleOfKingdoms.getAPI().map(TaleOfKingdomsAPI::getDataFolder).orElseThrow(() -> new IllegalArgumentException("API not present")) + "worlds/" + instance.getWorld() + ".conquestworld");
            try (Writer writer = new FileWriter(file)) {
                Gson gson = TaleOfKingdoms.getAPI().get().getMod().getGson();
                gson.toJson(instance, writer);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().removeConquest(instance.getWorld());
            this.joined = false;
        });

        WorldSessionStartCallback.EVENT.register(worldName -> this.worldName = worldName);

        RecipesUpdatedCallback.EVENT.register(() -> {
            PlayerEntity entity = MinecraftClient.getInstance().player;
            if (!entity.getEntityWorld().isClient()) return;

            // Check player is loaded, then check if it's them or not, and whether they've already been registered. If all conditions met, add to joined list.
            if (joined) return;

            this.joined = true;
            //TODO support for server
            if (!TaleOfKingdoms.getAPI().isPresent()) return;
            TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI().get();
            boolean loaded = load(worldName, api);
            File file = new File(api.getDataFolder() + "worlds/" + worldName + ".conquestworld");
            if (loaded) {
                // Already exists
                Gson gson = TaleOfKingdoms.getAPI().get().getMod().getGson();
                try {
                    // Load from json into class
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    ConquestInstance instance = gson.fromJson(reader, ConquestInstance.class);
                    api.executeOnMain(() -> {
                        // Check if file exists, but values don't. Game probably crashed?
                        if ((instance == null || instance.getName() == null) || !instance.isLoaded())
                            MinecraftClient.getInstance().openScreen(new ScreenStartConquest(worldName, file, entity));
                        else
                            MinecraftClient.getInstance().openScreen(new ScreenContinueConquest(instance));
                        TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().addConquest(worldName, instance, true);
                    });
                } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
                    e.printStackTrace();
                }
                return;
            }

            // New world creation
            api.executeOnMain(() -> MinecraftClient.getInstance().openScreen(new ScreenStartConquest(worldName, file, entity)));
        });
    }

    private boolean load(String worldName, TaleOfKingdomsAPI api) {
        File file = new File(api.getDataFolder() + "worlds/" + worldName + ".conquestworld");
        // Check if this world has been loaded or not
        if (!file.exists()) {
            try {
                // If not, create new file
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            // It already exists.
            return true;
        }
    }
}
