package com.convallyria.taleofkingdoms;

import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.generic.HunterEntity;
import com.convallyria.taleofkingdoms.common.entity.generic.KnightEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.BankerEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.BlacksmithEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.CityBuilderEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.FarmerEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.FoodShopEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildGuardEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.GuildMasterEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.InnkeeperEntity;
import com.convallyria.taleofkingdoms.common.entity.guild.LoneEntity;
import com.convallyria.taleofkingdoms.common.gson.BlockPosAdapter;
import com.convallyria.taleofkingdoms.common.item.ItemRegistry;
import com.convallyria.taleofkingdoms.common.listener.BlockListener;
import com.convallyria.taleofkingdoms.common.listener.CoinListener;
import com.convallyria.taleofkingdoms.common.listener.DeleteWorldListener;
import com.convallyria.taleofkingdoms.common.listener.KingdomListener;
import com.convallyria.taleofkingdoms.common.listener.MobSpawnListener;
import com.convallyria.taleofkingdoms.common.listener.SleepListener;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Optional;

public class TaleOfKingdoms implements ModInitializer {

    public static final String MODID = "taleofkingdoms";
    public static final String NAME = "Tale of Kingdoms";
    public static final String VERSION = "1.0.0";

    public static final Logger LOGGER = LogManager.getLogger();

    private static TaleOfKingdomsAPI api;

    public static final Identifier INSTANCE_PACKET_ID = new Identifier(TaleOfKingdoms.MODID, "instance");
    public static final Identifier SIGN_CONTRACT_PACKET_ID = new Identifier(TaleOfKingdoms.MODID, "sign_contract");

    private final CommandManager commandManager = new CommandManager(CommandManager.RegistrationEnvironment.ALL);

    @Override
    public void onInitialize() {
        ItemRegistry.init();

        File file = new File(this.getDataFolder() + "worlds/");
        if (!file.exists()) file.mkdirs();
        registerEvents();
        TaleOfKingdoms.api = new TaleOfKingdomsAPI(this);
        try {
            Schematic.saveAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FabricDefaultAttributeRegistry.register(EntityTypes.INNKEEPER, InnkeeperEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.FARMER, FarmerEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.GUILDMASTER, GuildMasterEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.BLACKSMITH, BlacksmithEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.CITYBUILDER, CityBuilderEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.KNIGHT, KnightEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.HUNTER, HunterEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.GUILDGUARD, GuildGuardEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.BANKER, BankerEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.LONE, LoneEntity.createMobAttributes());
        FabricDefaultAttributeRegistry.register(EntityTypes.FOODSHOP, FoodShopEntity.createMobAttributes());
    }

    /**
     * Gets the "data folder" of the mod. This is always the modid as a folder in the mods folder.
     * You may get the file using this.
     * @return data folder name
     */
    @NotNull
    public String getDataFolder() {
        return new File(".").getAbsolutePath() + "/mods/" + TaleOfKingdoms.MODID + "/";
    }

    /**
     * Gets the API. This will only be present after the mod has finished loading.
     * @return api of {@link TaleOfKingdoms}
     */
    public static Optional<TaleOfKingdomsAPI> getAPI() {
        return Optional.ofNullable(api);
    }

    private void registerEvents() {
        TaleOfKingdoms.LOGGER.info("Registering events...");
        new CoinListener();
        new SleepListener();
        new MobSpawnListener();
        new BlockListener();
        new KingdomListener();
        new DeleteWorldListener();
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> { // Register commands
            dispatcher.register(CommandManager.literal("taleofkingdoms").executes(context -> {
                Entity entity = context.getSource().getEntity();
                if (entity != null) {
                    String taleOfKingdoms = "[\"\",{\"text\":\"Tale of Kingdoms: A new Conquest\",\"bold\":true,\"underlined\":true,\"color\":\"blue\"},{\"text\":\"\\n\"},{\"text\":\"By Cotander/SamB440 & others. (hover)\",\"color\":\"blue\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://gitlab.com/SamB440/tale-of-kingdoms/-/blob/master/src/main/resources/fabric.mod.json\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"Marackai, Aksel0206, PyroPyxel, Sheepguard, michaelb229, The_KingCobra200, Krol05, BeingAmazing(Ben)#6423. Click to view full list.\"}}},{\"text\":\"\\n\"},{\"text\":\" Take a look at our website: \",\"color\":\"gold\"},{\"text\":\"https://www.convallyria.com\",\"underlined\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://www.convallyria.com\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"Click to view our website.\"}}},{\"text\":\"\\n\"},{\"text\":\" Join our Discord: \",\"color\":\"gold\"},{\"text\":\"https://discord.gg/fh62mxU\",\"underlined\":true,\"color\":\"gold\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://discord.gg/fh62mxU\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":{\"text\":\"Click to join our Discord.\"}}}]";
                    entity.sendSystemMessage(Texts.parse(context.getSource(), parse(new StringReader(taleOfKingdoms)), entity, 0), Util.NIL_UUID);
                    return 1;
                }
                return 0;
            }));
        });
    }

    private Text parse(StringReader stringReader) throws CommandSyntaxException {
        try {
            Text text = Text.Serializer.fromJson(stringReader);
            if (text == null) {
                throw TextArgumentType.INVALID_COMPONENT_EXCEPTION.createWithContext(stringReader, "empty");
            } else {
                return text;
            }
        } catch (JsonParseException var4) {
            String string = var4.getCause() != null ? var4.getCause().getMessage() : var4.getMessage();
            throw TextArgumentType.INVALID_COMPONENT_EXCEPTION.createWithContext(stringReader, string);
        }
    }

    public Gson getGson() {
        return new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(BlockPos.class, new BlockPosAdapter())
                .create();
    }
}

