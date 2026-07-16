package com.convallyria.taleofkingdoms.client;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.entity.render.RenderSetup;
import com.convallyria.taleofkingdoms.client.gui.RenderListener;
import com.convallyria.taleofkingdoms.client.gui.generic.ScreenStartConquest;
import com.convallyria.taleofkingdoms.client.gui.shop.ScreenSellItem;
import com.convallyria.taleofkingdoms.client.listener.ClientGameInstanceListener;
import com.convallyria.taleofkingdoms.client.listener.StartWorldListener;
import com.convallyria.taleofkingdoms.client.packet.incoming.IncomingInstanceSyncPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.incoming.IncomingOpenScreenPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.incoming.IncomingSignContractResponsePacketHandler;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingBankerInteractPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingBuildKingdomPacket;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingBuyItemPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingChangeTitlePacketHandler;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingCityBuilderActionPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingFixGuildPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingForemanBuyWorkerPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingForemanCollectPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingHunterPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingInnkeeperPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingSignContractPacketHandler;
import com.convallyria.taleofkingdoms.client.packet.outgoing.OutgoingToggleSellGuiPacketHandler;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.server.packet.outgoing.OutgoingOpenScreenPacketHandler;
import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import java.io.File;

@Environment(EnvType.CLIENT)
public class TaleOfKingdomsClient {

    private static TaleOfKingdomsClientAPI api;
    private StartWorldListener startWorldListener;

    public static final KeyMapping START_CONQUEST_KEYBIND = new KeyMapping(
            "key.taleofkingdoms.startconquest", // The translation key of the keybinding's name
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "category.taleofkingdoms.keys" // The translation key of the keybinding's category.
    );

    public static TaleOfKingdomsClientAPI getAPI() {
        return api;
    }

    public void init() {
        TaleOfKingdoms.setAPI(api = new TaleOfKingdomsClientAPI(TaleOfKingdoms.getInstance()));
        KeyMappingRegistry.register(START_CONQUEST_KEYBIND);
        new RenderSetup(api.getMod());
        registerPacketHandlers();
        registerEvents();
        registerTasks();
        // listen() defers until the menu type commits: this init runs during mod construction on
        // NeoForge, before the deferred registration, so SELL_SCREEN_HANDLER.get() would throw here.
        // Note CLIENT_SETUP is too late for this on both loaders: architectury fires it before our
        // Fabric entrypoint runs, and on NeoForge it maps to FMLClientSetupEvent, which fires after
        // RegisterMenuScreensEvent (where architectury actually registers the factory).
        TaleOfKingdoms.SELL_SCREEN_HANDLER.listen(menuType ->
                MenuRegistry.registerScreenFactory(menuType, ScreenSellItem::new));

        ClientTickEvent.CLIENT_POST.register(client -> {
            while (START_CONQUEST_KEYBIND.consumeClick()) {
                String worldName = startWorldListener.getWorldName();
                if (worldName == null) {
                    TaleOfKingdoms.LOGGER.info("World name was null");
                    return;
                }

                final var existing = api.getConquestInstanceStorage().getConquestInstance(worldName);
                if (existing.isPresent()
                        && (StartWorldListener.shouldAutoGenerateGuild() || existing.get().getStart() != null)) {
                    // Block manual placement unless the guild neither exists nor will generate on its own.
                    TaleOfKingdoms.LOGGER.info("World already loaded");
                    return;
                }

                File file = new File(api.getDataFolder() + "worlds/" + worldName + ConquestInstance.FILE_TYPE);
                client.setScreen(new ScreenStartConquest(worldName, file, client.player));
            }
        });
    }

    private void registerPacketHandlers() {
        registerHandler(new OutgoingSignContractPacketHandler());
        registerHandler(new IncomingSignContractResponsePacketHandler());

        registerHandler(new OutgoingBankerInteractPacketHandler());
        registerHandler(new OutgoingBuildKingdomPacket());
        registerHandler(new OutgoingBuyItemPacketHandler());
        registerHandler(new OutgoingChangeTitlePacketHandler());
        registerHandler(new OutgoingCityBuilderActionPacketHandler());
        registerHandler(new OutgoingFixGuildPacketHandler());
        registerHandler(new OutgoingForemanBuyWorkerPacketHandler());
        registerHandler(new OutgoingForemanCollectPacketHandler());
        registerHandler(new OutgoingHunterPacketHandler());
        registerHandler(new OutgoingInnkeeperPacketHandler());
        registerHandler(new OutgoingToggleSellGuiPacketHandler());

        registerHandler(new IncomingInstanceSyncPacketHandler());
        registerHandler(new IncomingOpenScreenPacketHandler());

        api.registerPacketHandler(EnvType.SERVER, new OutgoingOpenScreenPacketHandler());
    }

    protected void registerHandler(PacketHandler<?> clientPacketHandler) {
        api.registerPacketHandler(EnvType.CLIENT, clientPacketHandler);
    }

    private void registerEvents() {
        TaleOfKingdoms.LOGGER.info("Registering client events...");
        this.startWorldListener = new StartWorldListener();
        new ClientGameInstanceListener();
        new RenderListener();
    }

    private void registerTasks() {
        api.getScheduler().repeating(server -> {
            api.getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                instance.getGuildPlayers().forEach((id, guildPlayer) -> {
                    final PlayerKingdom kingdom = guildPlayer.getKingdom();
                    if (kingdom == null) return;
                    kingdom.tryTaxCollection(guildPlayer);
                });
            });
        }, 20, 1000);
    }
}
