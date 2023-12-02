package com.convallyria.taleofkingdoms.client;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.entity.render.RenderSetup;
import com.convallyria.taleofkingdoms.client.gui.RenderListener;
import com.convallyria.taleofkingdoms.client.gui.generic.ScreenStartConquest;
import com.convallyria.taleofkingdoms.client.gui.shop.ScreenSellItem;
import com.convallyria.taleofkingdoms.client.listener.ClientGameInstanceListener;
import com.convallyria.taleofkingdoms.client.packet.ClientPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.both.BothSignContractPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.incoming.IncomingInstanceSyncPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingBankerInteractPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingBuildKingdomPacket;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingBuyItemPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingCityBuilderActionPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingFixGuildPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingForemanBuyWorkerPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingForemanCollectPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingHunterPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingInnkeeperPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingToggleSellGuiPacketHandler;
import com.convallyria.taleofkingdoms.common.listener.StartWorldListener;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.io.File;

@Environment(EnvType.CLIENT)
public class TaleOfKingdomsClient implements ClientModInitializer {

    private static TaleOfKingdomsClientAPI api;
    private StartWorldListener startWorldListener;

    public static final KeyBinding START_CONQUEST_KEYBIND = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.taleofkingdoms.startconquest", // The translation key of the keybinding's name
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "category.taleofkingdoms.keys" // The translation key of the keybinding's category.
    ));

    public static TaleOfKingdomsClientAPI getAPI() {
        return api;
    }

    @Override
    public void onInitializeClient() {
        TaleOfKingdoms.setAPI(api = new TaleOfKingdomsClientAPI(TaleOfKingdoms.getInstance()));
        new RenderSetup(api.getMod());
        registerPacketHandlers();
        registerEvents();
        HandledScreens.register(TaleOfKingdoms.SELL_SCREEN_HANDLER, ScreenSellItem::new);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (START_CONQUEST_KEYBIND.wasPressed()) {
                String worldName = startWorldListener.getWorldName();
                if (worldName == null) {
                    TaleOfKingdoms.LOGGER.info("World name was null");
                    return;
                }

                if (api.getConquestInstanceStorage().getConquestInstance(worldName).isPresent()) {
                    TaleOfKingdoms.LOGGER.info("World already loaded");
                    return;
                }

                File file = new File(api.getDataFolder() + "worlds/" + worldName + ".conquestworld");
                client.setScreen(new ScreenStartConquest(worldName, file, client.player));
            }
        });
    }

    private void registerPacketHandlers() {
        registerHandler(new BothSignContractPacketHandler());

        registerHandler(new OutgoingBankerInteractPacketHandler());
        registerHandler(new OutgoingBuildKingdomPacket());
        registerHandler(new OutgoingBuyItemPacketHandler());
        registerHandler(new OutgoingCityBuilderActionPacketHandler());
        registerHandler(new OutgoingFixGuildPacketHandler());
        registerHandler(new OutgoingForemanBuyWorkerPacketHandler());
        registerHandler(new OutgoingForemanCollectPacketHandler());
        registerHandler(new OutgoingHunterPacketHandler());
        registerHandler(new OutgoingInnkeeperPacketHandler());
        registerHandler(new OutgoingToggleSellGuiPacketHandler());

        registerHandler(new IncomingInstanceSyncPacketHandler());
    }

    protected void registerHandler(ClientPacketHandler clientPacketHandler) {
        api.registerClientHandler(clientPacketHandler);
    }

    private void registerEvents() {
        TaleOfKingdoms.LOGGER.info("Registering client events...");
        this.startWorldListener = new StartWorldListener();
        new ClientGameInstanceListener();
        new RenderListener();
    }
}
