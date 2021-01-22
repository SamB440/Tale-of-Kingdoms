package com.convallyria.taleofkingdoms.common.gson;

import com.google.gson.*;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public class AbstractAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    private final String thePackage;

    /**
     * Constructs a new adapter.
     * If null is passed, the package will also be saved to the json file.
     * @param thePackage package class is located in
     */
    public AbstractAdapter(@Nullable String thePackage) {
        this.thePackage = thePackage;
    }

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        if (thePackage != null)
            result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
        else
            result.add("type", new JsonPrimitive(src.getClass().getPackage().getName() + "." + src.getClass().getSimpleName()));
        result.add("properties", context.serialize(src, src.getClass()));
        return result;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");

        try {
            if (thePackage != null)
                return context.deserialize(element, Class.forName(thePackage + type));
            else
                return context.deserialize(element, Class.forName(type));
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException("Unknown element type: " + type, cnfe);
        }
    }
}