package com.convallyria.taleofkingdoms.common.gson;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.api.EnvType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

public class ConquestInstanceAdapter implements JsonSerializer<ConquestInstance>, JsonDeserializer<ConquestInstance> {

    @Override
    public JsonElement serialize(ConquestInstance conquestInstance, Type type, JsonSerializationContext context) {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(BlockPos.class, new BlockPosAdapter())
                .create()
                .toJsonTree(conquestInstance);
    }

    @Override
    public ConquestInstance deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        final Gson gson = TaleOfKingdoms.getAPI().getMod().getGson();
        JsonObject jsonObject = json.getAsJsonObject();
        String version = jsonObject.get("version").getAsString();

        if (version.equals("1.0.0") && TaleOfKingdoms.getAPI().getEnvironment() == EnvType.CLIENT) {
            // We need to convert 1.0.0 format to this server format
            // We used to store single player worlds without uuid maps, but there was no point to that
            TaleOfKingdoms.LOGGER.warn("Converting old format version 1");

            String world = jsonObject.get("world").getAsString();
            String name = jsonObject.get("name").getAsString();
            BlockPos start = gson.fromJson(jsonObject.getAsJsonObject("start"), BlockPos.class);
            BlockPos end = gson.fromJson(jsonObject.getAsJsonObject("end"), BlockPos.class);
            BlockPos origin = gson.fromJson(jsonObject.getAsJsonObject("origin"), BlockPos.class);
            ConquestInstance serverConquestInstance = new ConquestInstance(world, name, start, end, origin);
            serverConquestInstance.setVersion(version);
            boolean hasLoaded = jsonObject.get("hasLoaded").getAsBoolean();
            boolean underAttack = jsonObject.get("underAttack").getAsBoolean();
            boolean hasRebuilt = jsonObject.get("hasRebuilt").getAsBoolean();
            final List<UUID> loneVillagersWithRooms = gson.fromJson(jsonObject.getAsJsonArray("loneVillagersWithRooms"), new TypeToken<List<UUID>>() {
            }.getType());
            final List<BlockPos> reficuleAttackLocations = gson.fromJson(jsonObject.getAsJsonArray("reficuleAttackLocations"), new TypeToken<List<BlockPos>>() {
            }.getType());
            final List<UUID> reficuleAttackers = gson.fromJson(jsonObject.getAsJsonArray("reficuleAttackers"), new TypeToken<List<UUID>>() {
            }.getType());
            final List<UUID> hunterUUIDs = gson.fromJson(jsonObject.getAsJsonArray("hunterUUIDs"), new TypeToken<List<UUID>>() {
            }.getType());

            UUID playerUuid = MinecraftClient.getInstance().player.getUuid();
            int coins = jsonObject.get("coins").getAsInt();
            int bankerCoins = jsonObject.get("bankerCoins").getAsInt();
            long farmerLastBread = jsonObject.get("farmerLastBread").getAsLong();
            boolean hasContract = jsonObject.get("hasContract").getAsBoolean();
            int worthiness = jsonObject.get("worthiness").getAsInt();

            serverConquestInstance.setLoaded(hasLoaded);
            serverConquestInstance.setUnderAttack(underAttack);
            serverConquestInstance.setRebuilt(hasRebuilt);
            serverConquestInstance.getLoneVillagersWithRooms().addAll(loneVillagersWithRooms);
            serverConquestInstance.getReficuleAttackLocations().addAll(reficuleAttackLocations);
            serverConquestInstance.getReficuleAttackers().addAll(reficuleAttackers);
            serverConquestInstance.getHunterUUIDs().put(playerUuid, hunterUUIDs);
            serverConquestInstance.setCoins(playerUuid, coins);
            serverConquestInstance.setBankerCoins(playerUuid, bankerCoins);
            serverConquestInstance.setFarmerLastBread(playerUuid, farmerLastBread);
            serverConquestInstance.setHasContract(playerUuid, hasContract);
            serverConquestInstance.setWorthiness(playerUuid, worthiness);
            TaleOfKingdoms.LOGGER.info("Successfully converted save file.");
            return serverConquestInstance;
        }

        try {
            return new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(BlockPos.class, new BlockPosAdapter())
                    .create()
                    .fromJson(json, ConquestInstance.class);
        } catch (Exception e) {
            if (version.equals("1.0.0")) TaleOfKingdoms.LOGGER.error("Unable to load conquest instance. You are trying to load the old client format '1' on a server. Please join the single player world using this save file first so it will be converted.");
            else e.printStackTrace();
            return null;
        }
    }
}
