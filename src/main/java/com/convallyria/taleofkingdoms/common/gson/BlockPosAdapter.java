package com.convallyria.taleofkingdoms.common.gson;

import com.google.gson.*;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Type;

public class BlockPosAdapter implements JsonSerializer<BlockPos>, JsonDeserializer<BlockPos> {

    private final Gson gson;

    public BlockPosAdapter() {
        this.gson = new GsonBuilder().create();
    }

    @Override
    public JsonElement serialize(BlockPos blockPos, Type type, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("x", new JsonPrimitive(blockPos.getX()));
        result.add("y", new JsonPrimitive(blockPos.getY()));
        result.add("z", new JsonPrimitive(blockPos.getZ()));
        return result;
    }

    @Override
    public BlockPos deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        int x = jsonObject.get("x").getAsInt();
        int y = jsonObject.get("y").getAsInt();
        int z = jsonObject.get("z").getAsInt();
        return new BlockPos(x, y, z);
    }
}
