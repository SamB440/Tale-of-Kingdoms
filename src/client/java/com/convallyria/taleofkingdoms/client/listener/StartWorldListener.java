package com.convallyria.taleofkingdoms.client.listener;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.TaleOfKingdomsClient;
import com.convallyria.taleofkingdoms.client.gui.generic.ScreenContinueConquest;
import com.convallyria.taleofkingdoms.client.gui.generic.ScreenStartConquest;
import com.convallyria.taleofkingdoms.client.gui.generic.owo.update.UpdateScreen;
import com.convallyria.taleofkingdoms.common.event.GameJoinCallback;
import com.convallyria.taleofkingdoms.common.event.WorldSessionStartCallback;
import com.convallyria.taleofkingdoms.common.event.WorldStopCallback;
import com.convallyria.taleofkingdoms.common.listener.Listener;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class StartWorldListener extends Listener {

    private String worldName;
    private final List<Runnable> postJoin = new ArrayList<>();
    private boolean joined;

    @Nullable
    public String getWorldName() {
        return worldName;
    }

    public StartWorldListener() {
        WorldStopCallback.EVENT.register(() -> {
            if (!joined) return;
            if (TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().isEmpty()) return;

            ConquestInstance instance = TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().get();
            instance.save(worldName);
            TaleOfKingdoms.getAPI().getConquestInstanceStorage().removeConquest(worldName);
            this.joined = false;
        });

        WorldSessionStartCallback.EVENT.register(worldName -> {
            final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
            if (api == null) return;

            this.worldName = worldName;
            boolean loaded = load(worldName, api);

            File file = new File(api.getDataFolder() + "worlds/" + worldName + ConquestInstance.FILE_TYPE);

            // Already exists
            if (loaded) {
                Gson gson = api.getMod().getGson();
                // Load from json into class
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    ConquestInstance instance = gson.fromJson(reader, ConquestInstance.class);
                    Runnable runnable = () -> api.executeOnMain(() -> {
                        // Check if file exists, but values don't. Game probably crashed?
                        if ((instance == null || instance.getName() == null) || !instance.isLoaded()) {
                            PlayerEntity entity = MinecraftClient.getInstance().player;
                            if (entity == null) return;

                            if (TaleOfKingdoms.CONFIG.mainConfig.showStartKingdomGUI) {
                                MinecraftClient.getInstance().setScreen(new ScreenStartConquest(worldName, file, entity));
                            } else {
                                Text keyName = TaleOfKingdomsClient.START_CONQUEST_KEYBIND.getBoundKeyLocalizedText();
                                entity.sendMessage(Text.translatable("menu.taleofkingdoms.startconquest.closed", keyName.getString()), false);
                            }
                        } else {
                            if (TaleOfKingdoms.CONFIG.mainConfig.alwaysShowUpdatesGUI || instance.didUpgrade()) {
                                MinecraftClient.getInstance().setScreen(new UpdateScreen());
                            } else if (TaleOfKingdoms.CONFIG.mainConfig.showContinueConquestGUI) {
                                MinecraftClient.getInstance().setScreen(new ScreenContinueConquest(instance));
                            }

                            if (api.getConquestInstanceStorage().getConquestInstance(worldName).isEmpty()) {
                                TaleOfKingdoms.LOGGER.info("Adding world: " + worldName);
                                api.getConquestInstanceStorage().addConquest(worldName, instance, true);
                            }
                        }
                    });

                    if (instance != null) {
                        TaleOfKingdoms.LOGGER.info("Adding early existing world: " + worldName);
                        api.getConquestInstanceStorage().addConquest(worldName, instance, true);
                    }

                    postJoin.add(runnable);
                } catch (JsonSyntaxException | JsonIOException | IOException e) {
                    e.printStackTrace();
                }
                return;
            }

            // New world creation
            postJoin.add(() -> api.executeOnMain(() -> {
                PlayerEntity entity = MinecraftClient.getInstance().player;
                if (entity == null) return;
                MinecraftClient.getInstance().setScreen(new ScreenStartConquest(worldName, file, entity));
            }));
        });

        GameJoinCallback.EVENT.register(() -> {
            if (!MinecraftClient.getInstance().getNetworkHandler().getConnection().isLocal()) {
                postJoin.clear();
                return;
            }

            // Check player is loaded, then check if it's them or not, and whether they've already been registered. If all conditions met, add to joined list.
            if (joined) {
                postJoin.clear();
                return;
            }
            this.joined = true;

            for (Runnable runnable : postJoin) {
                runnable.run();
            }

            postJoin.clear();
        });
    }

    private boolean load(String worldName, TaleOfKingdomsAPI api) {
        File file = new File(api.getDataFolder() + "worlds/" + worldName + ConquestInstance.FILE_TYPE);
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
