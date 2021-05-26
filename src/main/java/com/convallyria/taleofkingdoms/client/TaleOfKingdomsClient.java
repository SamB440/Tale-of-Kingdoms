package com.convallyria.taleofkingdoms.client;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.entity.render.RenderSetup;
import com.convallyria.taleofkingdoms.client.gui.RenderListener;
import com.convallyria.taleofkingdoms.client.gui.generic.ScreenStartConquest;
import com.convallyria.taleofkingdoms.client.gui.shop.ScreenSellItem;
import com.convallyria.taleofkingdoms.client.packet.ClientPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.both.BothSignContractPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.incoming.IncomingInstanceSyncPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingBankerInteractPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingBuyItemPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingFixGuildPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingToggleSellGuiPacketHandler;
import com.convallyria.taleofkingdoms.common.listener.GameInstanceListener;
import com.convallyria.taleofkingdoms.common.listener.StartWorldListener;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.io.File;

@Environment(EnvType.CLIENT)
public class TaleOfKingdomsClient implements ClientModInitializer {

    private StartWorldListener startWorldListener;

    private static final KeyBinding START_CONQUEST_KEYBIND = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.taleofkingdoms.startconquest", // The translation key of the keybinding's name
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "category.taleofkingdoms.keys" // The translation key of the keybinding's category.
    ));

    @Override
    public void onInitializeClient() {
        TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI().get();
        new RenderSetup(api.getMod());
        registerPacketHandlers();
        registerEvents();
        ScreenRegistry.register(TaleOfKingdoms.SELL_SCREEN_HANDLER, ScreenSellItem::new);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (START_CONQUEST_KEYBIND.wasPressed()) {
                String worldName = startWorldListener.getWorldName();
                if (api.getConquestInstanceStorage().getConquestInstance(worldName).isPresent()) return;
                File file = new File(api.getDataFolder() + "worlds/" + worldName + ".conquestworld");
                client.openScreen(new ScreenStartConquest(worldName, file, client.player));
            }
        });
    }

    private void registerPacketHandlers() {
        registerHandler(new IncomingInstanceSyncPacketHandler());
        registerHandler(new BothSignContractPacketHandler());
        registerHandler(new OutgoingFixGuildPacketHandler());
        registerHandler(new OutgoingToggleSellGuiPacketHandler());
        registerHandler(new OutgoingBuyItemPacketHandler());
        registerHandler(new OutgoingBankerInteractPacketHandler());
    }

    private void registerListeners() {
        new GameInstanceListener();
    }

    protected void registerHandler(ClientPacketHandler clientPacketHandler) {
        TaleOfKingdoms.getAPI().ifPresent(api -> api.registerClientHandler(clientPacketHandler));
    }

    private void registerEvents() {
        TaleOfKingdoms.LOGGER.info("Registering client events...");
        new RenderListener();
        this.startWorldListener = new StartWorldListener();
    }
}
