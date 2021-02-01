package com.convallyria.taleofkingdoms.common.listener;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.gui.generic.ScreenContinueConquest;
import com.convallyria.taleofkingdoms.client.gui.generic.ScreenStartConquest;
import com.convallyria.taleofkingdoms.common.event.RecipesUpdatedCallback;
import com.convallyria.taleofkingdoms.common.event.WorldSessionStartCallback;
import com.convallyria.taleofkingdoms.common.event.WorldStopCallback;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
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
            if (entity == null) return;
            if (!MinecraftClient.getInstance().getNetworkHandler().getConnection().isLocal()) return;
            // Check player is loaded, then check if it's them or not, and whether they've already been registered. If all conditions met, add to joined list.
            if (joined) return;
            this.joined = true;

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
                    ConquestInstance instance = gson.fromJson(reader, ClientConquestInstance.class);
                    api.executeOnMain(() -> {
                        // Check if file exists, but values don't. Game probably crashed?
                        if ((instance == null || instance.getName() == null) || !instance.isLoaded()) {
                            MinecraftClient.getInstance().openScreen(new ScreenStartConquest(worldName, file, entity));
                        } else {
                            MinecraftClient.getInstance().openScreen(new ScreenContinueConquest(instance));
                            System.out.println("Adding world: " + worldName);
                            TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().addConquest(worldName, instance, true);
                        }
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
                return file.createNewFile();
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
