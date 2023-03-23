package com.convallyria.taleofkingdoms.common.serialization.gson;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.datafixer.TaleOfKingdomsDataFixes;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;

import java.lang.reflect.Type;
import java.util.Optional;

public class ConquestInstanceAdapter implements JsonSerializer<ConquestInstance>, JsonDeserializer<ConquestInstance> {

    @Override
    public JsonElement serialize(ConquestInstance instance, Type typeOfSrc, JsonSerializationContext context) {
        final Optional<JsonElement> result = ConquestInstance.CODEC.encodeStart(JsonOps.INSTANCE, instance).resultOrPartial(TaleOfKingdoms.LOGGER::error);
        return result.map(TaleOfKingdomsDataFixes::updateFixerVersion).orElse(null);
    }

    @Override
    public ConquestInstance deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject object = json.getAsJsonObject();
        Dynamic<JsonElement> dynamic = new Dynamic<>(JsonOps.INSTANCE, object);
        final int fixerVersion = TaleOfKingdomsDataFixes.getFixerVersion(object);
        // Nothing to DFU... yet!
        final Optional<ConquestInstance> result = ConquestInstance.CODEC.parse(JsonOps.INSTANCE, dynamic.getValue()).resultOrPartial(TaleOfKingdoms.LOGGER::error);
        if (result.isPresent()) {
            final ConquestInstance conquestInstance = result.get();
            conquestInstance.setDidUpgrade(fixerVersion != TaleOfKingdoms.DATA_FORMAT_VERSION);
            return conquestInstance;
        }
        return null;
    }
}
