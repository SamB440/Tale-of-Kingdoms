package com.convallyria.taleofkingdoms.client.gui.generic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.TaleOfKingdomsClient;
import com.convallyria.taleofkingdoms.client.gui.ScreenTOK;
import com.convallyria.taleofkingdoms.common.translation.Translations;
import com.convallyria.taleofkingdoms.common.event.tok.KingdomStartCallback;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
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

    public ScreenStartConquest(@NotNull String worldName, File toSave, Player player) {
        super("menu.taleofkingdoms.startconquest.name");
        this.worldName = worldName;
        this.toSave = toSave;
        this.player = player;
    }

    @Override
    public void init() {
        super.init();
        this.children().clear();
        this.text = new EditBox(this.font, this.width / 2 - 150, this.height / 2 - 40, 300, 20, Component.literal("Sir Punchwood"));
        this.addRenderableWidget(mButtonClose = Button.builder(Translations.START_CONQUEST.getTranslation(), button -> {
            if (loading) return;

            button.setMessage(Translations.BUILDING_CASTLE.getTranslation());
            final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
            if (api == null) {
                button.setMessage(Component.literal("No API present"));
                return;
            }

            MinecraftServer server = Minecraft.getInstance().getSingleplayerServer();
            if (server == null) {
                button.setMessage(Component.literal("No server present"));
                return;
            }
            ServerPlayer serverPlayer = server.getPlayerList().getPlayer(player.getUUID());
            if (serverPlayer == null) return;

            // Load guild castle schematic
            ConquestInstance instance = new ConquestInstance(text.getValue(), null, null, serverPlayer.blockPosition().offset(0, 1, 0));
            instance.reset(player);
            api.getConquestInstanceStorage().addConquest(worldName, instance, true);

            BlockPos pastePos = serverPlayer.blockPosition().subtract(new Vec3i(0, 20, 0));
            api.getSchematicHandler().pasteSchematic(Schematic.GUILD_CASTLE, serverPlayer, pastePos).thenAccept(oi -> api.executeOnServerEnvironment((s) -> {
                BlockPos start = new BlockPos(oi.maxX(), oi.maxY(), oi.maxZ());
                BlockPos end = new BlockPos(oi.minX(), oi.minY(), oi.minZ());
                instance.setStart(start);
                instance.setEnd(end);

                button.setMessage(Translations.SUMMONING_CITIZENS.getTranslation());

                api.executeOnMain(() -> {
                    button.setMessage(Component.literal("Reloading chunks..."));
                    Minecraft.getInstance().levelRenderer.allChanged();
                    onClose();
                    loading = false;
                    instance.setLoaded(true);
                    final GuildPlayer guildPlayer = instance.getPlayer(player.getUUID());
                    guildPlayer.setFarmerLastBread(-1); // Set to -1 in order to claim on first day
                    instance.save(worldName);
                });

                KingdomStartCallback.EVENT.invoker().kingdomStart(serverPlayer, instance); // Call kingdom start event
            }));
        }).bounds(this.width / 2 - 100, this.height / 2 + 15, 200, 20).build());

        this.addRenderableWidget(Button.builder(Component.translatable("menu.taleofkingdoms.startconquest.delay").withStyle(ChatFormatting.RED), (b) -> {
            this.onClose();
        }).bounds(this.width / 2 - 50, this.height / 2 + 50, 100, 20).build());

        this.text.setMaxLength(32);
        this.text.setValue("Sir Punchwood");
        this.text.setCanLoseFocus(true);
        this.text.setFocused(true);
        this.text.setVisible(true);
        this.addWidget(this.text);
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        String text = Translations.DARKNESS.getFormatted();
        int currentHeight = this.height / 2 - 110;
        for (String toRender : text.split("\n")) {
            //todo change colour? 11111111 is nice
            context.drawCenteredString(this.font, toRender, this.width / 2, currentHeight, 0xFFFFFF);
            currentHeight = currentHeight + 10;
        }
        context.drawCenteredString(this.font, Translations.HERO.getFormatted(), this.width / 2, currentHeight + 10, 0xFFFFFF);
        context.drawCenteredString(this.font, Component.translatable("menu.taleofkingdoms.startconquest.exit"), this.width / 2, currentHeight + 65, 0xFFFFFF);
        this.text.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    @Override
    public void onClose() {
        super.onClose();
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        Optional<ConquestInstance> instance = api.getConquestInstanceStorage().mostRecentInstance();
        if (instance.isEmpty()) {
            Component keyName = TaleOfKingdomsClient.START_CONQUEST_KEYBIND.getTranslatedKeyMessage();
            player.displayClientMessage(Component.translatable("menu.taleofkingdoms.startconquest.closed", keyName.getString()), false);
        }
    }
}
