package com.convallyria.taleofkingdoms.client.gui.generic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.TaleOfKingdomsClient;
import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.event.tok.KingdomStartCallback;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.google.gson.Gson;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Optional;

public class ScreenStartConquest extends ScreenTOK {

    // Buttons
    private Button mButtonClose;

    // Text fields
    private EditBox text;

    // Other
    private final String worldName;
    private final File toSave;
    private final Player player;
    private boolean loading;

    public ScreenStartConquest(String worldName, File toSave, Player player) {
        super("taleofkingdoms.menu.startconquest.name");
        this.worldName = worldName;
        this.toSave = toSave;
        this.player = player;
    }

    @Override
    public void init() {
        super.init();
        this.children().clear();
        this.text = new EditBox(this.font, this.width / 2 - 150, this.height / 2 - 40, 300, 20, new TextComponent("Sir Punchwood"));
        this.addRenderableWidget(mButtonClose = new Button(this.width / 2 - 100, this.height / 2 + 15, 200, 20, Translations.START_CONQUEST.getTranslation(), (button) -> {
            if (loading) return;

            button.setMessage(Translations.BUILDING_CASTLE.getTranslation());
            Optional<TaleOfKingdomsAPI> api = TaleOfKingdoms.getAPI();
            if (api.isEmpty()) {
                button.setMessage(new TextComponent("No API present"));
                return;
            }

            MinecraftServer server = Minecraft.getInstance().getSingleplayerServer();
            if (server == null) {
                button.setMessage(new TextComponent("No server present"));
                return;
            }
            ServerPlayer serverPlayer = server.getPlayerList().getPlayer(player.getUUID());
            if (serverPlayer == null) return;

            // Load guild castle schematic
            ClientConquestInstance instance = new ClientConquestInstance(worldName, text.getValue(), null, null, serverPlayer.blockPosition().offset(0, 1, 0));
            try (Writer writer = new FileWriter(toSave)) {
                Gson gson = api.get().getMod().getGson();
                gson.toJson(instance, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            api.get().getConquestInstanceStorage().addConquest(worldName, instance, true);

            BlockPos pastePos = serverPlayer.blockPosition().subtract(new Vec3i(0, 12, 0));
            api.get().getSchematicHandler().pasteSchematic(Schematic.GUILD_CASTLE, serverPlayer, pastePos).thenAccept(oi -> {
                api.get().executeOnServer(() -> {
                    BlockPos start = new BlockPos(oi.maxX(), oi.maxY(), oi.maxZ());
                    BlockPos end = new BlockPos(oi.minX(), oi.minY(), oi.minZ());
                    instance.setStart(start);
                    instance.setEnd(end);
                    
                    button.setMessage(Translations.SUMMONING_CITIZENS.getTranslation());
    
                    api.get().executeOnMain(() -> {
                        button.setMessage(new TextComponent("Reloading chunks..."));
                        Minecraft.getInstance().levelRenderer.allChanged();
                        onClose();
                        loading = false;
                        instance.setLoaded(true);
                        instance.setFarmerLastBread(-1); // Set to -1 in order to claim on first day
                    });
    
                    KingdomStartCallback.EVENT.invoker().kingdomStart(serverPlayer, instance); // Call kingdom start event
                });
            });
        }));
        this.text.setMaxLength(32);
        this.text.setValue("Sir Punchwood");
        this.text.setCanLoseFocus(false);
        this.text.changeFocus(true);
        this.text.setVisible(true);
        this.addWidget(this.text);
    }

    @Override
    public void render(PoseStack stack, int par1, int par2, float par3) {
        this.renderBackground(stack);
        String text = Translations.DARKNESS.getFormatted();
        int currentHeight = this.height / 2 - 110;
        for (String toRender : text.split("\n")) {
            drawCenteredString(stack, this.font, toRender, this.width / 2, currentHeight, 0xFFFFFF);
            currentHeight = currentHeight + 10;
        }
        drawCenteredString(stack, this.font, Translations.HERO.getFormatted(), this.width / 2, currentHeight + 10, 0xFFFFFF);
        this.text.render(stack, par1, par2, par3);
        super.render(stack, par1, par2, par3);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void onClose() {
        super.onClose();
        TaleOfKingdoms.getAPI().ifPresent(api -> {
            Optional<ConquestInstance> instance = api.getConquestInstanceStorage().mostRecentInstance();
            if (instance.isEmpty()) {
                Component keyName = TaleOfKingdomsClient.START_CONQUEST_KEYBIND.getTranslatedKeyMessage();
                player.displayClientMessage(new TextComponent("Start conquest menu was closed. Press ").append(keyName).append(" to open it again."), false);
            }
        });
    }
}
