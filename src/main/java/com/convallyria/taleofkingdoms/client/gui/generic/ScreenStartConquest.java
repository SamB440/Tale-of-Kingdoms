package com.convallyria.taleofkingdoms.client.gui.generic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.TaleOfKingdomsClient;
import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.event.tok.KingdomStartCallback;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.google.gson.Gson;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Optional;

public class ScreenStartConquest extends ScreenTOK {

    // Buttons
    private ButtonWidget mButtonClose;

    // Text fields
    private TextFieldWidget text;

    // Other
    private final String worldName;
    private final File toSave;
    private final PlayerEntity player;
    private boolean loading;

    public ScreenStartConquest(String worldName, File toSave, PlayerEntity player) {
        super("taleofkingdoms.menu.startconquest.name");
        this.worldName = worldName;
        this.toSave = toSave;
        this.player = player;
    }

    @Override
    public void init() {
        super.init();
        this.children().clear();
        this.text = new TextFieldWidget(this.textRenderer, this.width / 2 - 150, this.height / 2 - 40, 300, 20, Text.literal("Sir Punchwood"));
        this.addDrawableChild(mButtonClose = ButtonWidget.builder(Translations.START_CONQUEST.getTranslation(), button -> {
            if (loading) return;

            button.setMessage(Translations.BUILDING_CASTLE.getTranslation());
            final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
            if (api == null) {
                button.setMessage(Text.literal("No API present"));
                return;
            }

            MinecraftServer server = MinecraftClient.getInstance().getServer();
            if (server == null) {
                button.setMessage(Text.literal("No server present"));
                return;
            }
            ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(player.getUuid());
            if (serverPlayer == null) return;

            // Load guild castle schematic
            ConquestInstance instance = new ConquestInstance(worldName, text.getText(), null, null, serverPlayer.getBlockPos().add(0, 1, 0));
            try (Writer writer = new FileWriter(toSave)) {
                Gson gson = api.getMod().getGson();
                gson.toJson(instance, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            api.getConquestInstanceStorage().addConquest(worldName, instance, true);

            BlockPos pastePos = serverPlayer.getBlockPos().subtract(new Vec3i(0, 20, 0));
            api.getSchematicHandler().pasteSchematic(Schematic.GUILD_CASTLE, serverPlayer, pastePos).thenAccept(oi -> api.executeOnServer(() -> {
                BlockPos start = new BlockPos(oi.getMaxX(), oi.getMaxY(), oi.getMaxZ());
                BlockPos end = new BlockPos(oi.getMinX(), oi.getMinY(), oi.getMinZ());
                instance.setStart(start);
                instance.setEnd(end);

                button.setMessage(Translations.SUMMONING_CITIZENS.getTranslation());

                api.executeOnMain(() -> {
                    button.setMessage(Text.literal("Reloading chunks..."));
                    MinecraftClient.getInstance().worldRenderer.reload();
                    close();
                    loading = false;
                    instance.setLoaded(true);
                    instance.setFarmerLastBread(player.getUuid(), -1); // Set to -1 in order to claim on first day
                });

                KingdomStartCallback.EVENT.invoker().kingdomStart(serverPlayer, instance); // Call kingdom start event
            }));
        }).dimensions(this.width / 2 - 100, this.height / 2 + 15, 200, 20).build());

        this.text.setMaxLength(32);
        this.text.setText("Sir Punchwood");
        this.text.setFocusUnlocked(false);
        this.text.setFocused(true);
        this.text.setVisible(true);
        this.addSelectableChild(this.text);
    }

    @Override
    public void render(MatrixStack stack, int par1, int par2, float par3) {
        this.renderBackground(stack);
        String text = Translations.DARKNESS.getFormatted();
        int currentHeight = this.height / 2 - 110;
        for (String toRender : text.split("\n")) {
            //todo change colour? 11111111 is nice
            drawCenteredTextWithShadow(stack, this.textRenderer, toRender, this.width / 2, currentHeight, 0xFFFFFF);
            currentHeight = currentHeight + 10;
        }
        drawCenteredTextWithShadow(stack, this.textRenderer, Translations.HERO.getFormatted(), this.width / 2, currentHeight + 10, 0xFFFFFF);
        this.text.render(stack, par1, par2, par3);
        super.render(stack, par1, par2, par3);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public boolean shouldPause() {
        return true;
    }

    @Override
    public void close() {
        super.close();
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        Optional<ConquestInstance> instance = api.getConquestInstanceStorage().mostRecentInstance();
        if (instance.isEmpty()) {
            Text keyName = TaleOfKingdomsClient.START_CONQUEST_KEYBIND.getBoundKeyLocalizedText();
            player.sendMessage(Text.literal("Start conquest menu was closed. Press ").append(keyName).append(" to open it again."), false);
        }
    }
}
