package net.islandearth.taleofkingdoms.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.client.entity.render.RenderSetup;
import net.islandearth.taleofkingdoms.client.gui.RenderListener;
import net.islandearth.taleofkingdoms.client.gui.generic.ScreenSyncConquest;
import net.islandearth.taleofkingdoms.common.listener.StartWorldListener;
import net.islandearth.taleofkingdoms.common.world.ClientConquestInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class TaleOfKingdomsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientSidePacketRegistry.INSTANCE.register(TaleOfKingdoms.PLAY_INSTANCE_PACKET_ID,
                (packetContext, attachedData) -> {
                    String name = attachedData.readString();
                    String world = attachedData.readString();
                    int bankerCoins = attachedData.readInt();
                    int coins = attachedData.readInt();
                    int worthiness = attachedData.readInt();
                    long farmerLastBread = attachedData.readLong();
                    boolean hasContract = attachedData.readBoolean();
                    boolean isLoaded = attachedData.readBoolean();
                    BlockPos start = attachedData.readBlockPos();
                    BlockPos end = attachedData.readBlockPos();
                    packetContext.getTaskQueue().execute(() -> {
                        ScreenSyncConquest screenSyncConquest = new ScreenSyncConquest();
                        MinecraftClient.getInstance().openScreen(screenSyncConquest);
                        screenSyncConquest.setProgress("Loading API");
                        TaleOfKingdoms.getAPI().ifPresent(api -> {
                            screenSyncConquest.setProgress("API loaded");
                            ClientConquestInstance instance = new ClientConquestInstance(world, name, start, end);
                            screenSyncConquest.setProgress("Created new instance");
                            instance.setBankerCoins(bankerCoins);
                            instance.setCoins(coins);
                            instance.setWorthiness(worthiness);
                            instance.setFarmerLastBread(farmerLastBread);
                            instance.setHasContract(hasContract);
                            instance.setLoaded(isLoaded);
                            screenSyncConquest.setProgress("Packet read, adding instance " + world + ", " + name);
                            api.getConquestInstanceStorage().addConquest(world, instance, true);
                            screenSyncConquest.setProgress("Complete.");
                            screenSyncConquest.onClose();
                        });
                    });
                });
        new RenderSetup(TaleOfKingdoms.getAPI().get().getMod());
        registerEvents();
    }

    private void registerEvents() {
        TaleOfKingdoms.LOGGER.info("Registering client events...");
        new RenderListener();
        new StartWorldListener();
    }
}
