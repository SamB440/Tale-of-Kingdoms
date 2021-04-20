package com.convallyria.taleofkingdoms.client.gui.generic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.event.tok.KingdomStartCallback;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import com.convallyria.taleofkingdoms.common.world.ClientConquestInstance;
import com.google.gson.Gson;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
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
        this.buttons.clear();
        this.text = new TextFieldWidget(this.textRenderer, this.width / 2 - 150, this.height / 2 - 40, 300, 20, new LiteralText("Sir Punchwood"));
        this.addButton(mButtonClose = new ButtonWidget(this.width / 2 - 100, this.height / 2 + 30, 200, 20, Translations.START_CONQUEST.getTranslation(), (button) -> {
            if (loading) return;

            button.setMessage(Translations.BUILDING_CASTLE.getTranslation());
            Optional<TaleOfKingdomsAPI> api = TaleOfKingdoms.getAPI();
            if (!api.isPresent()) {
                button.setMessage(new LiteralText("No API present"));
                return;
            }

            MinecraftServer server = MinecraftClient.getInstance().getServer();
            if (server == null) {
                button.setMessage(new LiteralText("No server present"));
                return;
            }
            ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(player.getUuid());
            if (serverPlayer == null) return;

            // Load guild castle schematic
            ClientConquestInstance instance = new ClientConquestInstance(worldName, text.getText(), null, null, serverPlayer.getBlockPos().add(0, 1, 0));
            try (Writer writer = new FileWriter(toSave)) {
                Gson gson = api.get().getMod().getGson();
                gson.toJson(instance, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            api.get().getConquestInstanceStorage().addConquest(worldName, instance, true);

            BlockPos pastePos = serverPlayer.getBlockPos().subtract(new Vec3i(0, 12, 0));
            api.get().getSchematicHandler().pasteSchematic(Schematic.GUILD_CASTLE, serverPlayer, pastePos).thenAccept(oi -> {
                api.get().executeOnServer(() -> {
                    BlockPos start = new BlockPos(oi.maxX, oi.maxY, oi.maxZ);
                    BlockPos end = new BlockPos(oi.minX, oi.minY, oi.minZ);
                    instance.setStart(start);
                    instance.setEnd(end);
                    
                    button.setMessage(Translations.SUMMONING_CITIZENS.getTranslation());
    
                    api.get().executeOnMain(() -> {
                        button.setMessage(new LiteralText("Reloading chunks..."));
                        MinecraftClient.getInstance().worldRenderer.reload();
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
        this.text.setText("Sir Punchwood");
        this.text.setFocusUnlocked(false);
        this.text.changeFocus(true);
        this.text.setVisible(true);
        this.children.add(this.text);
    }

    @Override
    public void render(MatrixStack stack, int par1, int par2, float par3) {
        this.renderBackground(stack);
        drawCenteredString(stack, this.textRenderer, Translations.DARKNESS.getFormatted(), this.width / 2, this.height / 2, 0xFFFFFF);
        drawCenteredString(stack, this.textRenderer, Translations.HERO.getFormatted(), this.width / 2, this.height / 2 + 10, 0xFFFFFF);
        this.text.render(stack, par1, par2, par3);
        super.render(stack, par1, par2, par3);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
