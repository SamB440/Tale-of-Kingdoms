package com.convallyria.taleofkingdoms.common.schematic.blocky.block;

import com.convallyria.taleofkingdoms.common.schematic.blocky.WrongIdException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NBTSignBlock extends NBTBlock {

    private final Map<Position, String> lines;

    public NBTSignBlock(CompoundTag nbtTag) {
        super(nbtTag);
        this.lines = new HashMap<>();
    }

    @Override
    public void setData(ServerWorld world, BlockPos pos, BlockState block) throws WrongIdException {
        BlockEntity tileEntity = world.getChunk(pos).getBlockEntity(pos);
        SignBlockEntity signTileEntity = (SignBlockEntity) tileEntity;
        int current = 1;
        for (String line : this.getLines()) {
            signTileEntity.toInitialChunkDataTag().putString("Text" + current, line);
            current++;
        }
    }

    @Override
    public boolean isEmpty() {
        try {
            return getLines().isEmpty();
        } catch (WrongIdException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * @param position - position of text to read from
     * @return text at the specified position on the sign
     * @throws WrongIdException
     */
    public String getLine(Position position) throws WrongIdException {
        if (lines.containsKey(position)) {
            return lines.get(position);
        }

        CompoundTag compound = this.getNbtTag();
        if (compound.getString("Id").equals("minecraft:sign")) {
            String s1 = compound.getString(position.getId());
            JsonObject jsonObject = new Gson().fromJson(s1, JsonObject.class);
            if (jsonObject.get("extra") != null) {
                JsonArray array = jsonObject.get("extra").getAsJsonArray();
                return array.get(0).getAsJsonObject().get("text").getAsString();
            }
        } else {
            throw new WrongIdException("Id of NBT was not a sign, was instead " + compound.getString("Id"));
        }
        return null;
    }

    public List<String> getLines() throws WrongIdException {
        List<String> lines = new ArrayList<>();
        for (Position position : Position.values()) {
            lines.add(getLine(position));
        }
        return lines;
    }

    /**
     * Utility class for NBT sign positions
     * @author SamB440
     */
    public enum Position {
        TEXT_ONE("Text1"),
        TEXT_TWO("Text2"),
        TEXT_THREE("Text3"),
        TEXT_FOUR("Text4");
        
        public String getId() {
            return id;
        }
        
        private final String id;
        
        Position(String id) {
            this.id = id;
        }
    }
}
