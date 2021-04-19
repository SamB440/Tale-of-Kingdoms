package com.convallyria.taleofkingdoms.common.schematic.blocky.block;

import com.convallyria.taleofkingdoms.common.schematic.blocky.WrongIdException;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class NBTChestBlock extends NBTBlock {

    private final Map<Integer, ItemStack> allItems = new HashMap<>();

    public NBTChestBlock(CompoundTag nbtTag) {
        super(nbtTag);
    }

    @Override
    public void setData(ServerWorld world, BlockPos pos, BlockState block) throws WrongIdException {
        /*org.bukkit.block.Chest chest = (org.bukkit.block.Chest) state;
        for (Integer location : allItems.keySet()) {
            chest.getSnapshotInventory().setItem(location, allItems.get(location));
        }*/
        //TODO
    }

    @Override
    public boolean isEmpty() {
        try {
            return getItems().isEmpty();
        } catch (WrongIdException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * @return a map, with the key as the slot and the value as the item
     * @throws WrongIdException if it's not a chest
     */
    public Map<Integer, ItemStack> getItems() throws WrongIdException {
        if (!allItems.isEmpty()) return allItems;

        CompoundTag compound = this.getNbtTag();
        if (compound.getString("Id").equals("minecraft:chest")) {
            if (compound.get("Items") != null) {
                ListTag items = (ListTag) compound.get("Items");
                for (int i = 0; i < items.size(); i++) {
                    CompoundTag anItem = items.getCompound(i);
                    //Material mat = Material.valueOf(anItem.getString("id").replace("minecraft:", "").toUpperCase());
                    //ItemStack item = new ItemStack(mat, anItem.getInt("Count"));
                    //allItems.put(anItem.getInt("Slot"), item);
                }
            }
        } else {
            throw new WrongIdException("Id of NBT was not a chest, was instead " + compound.getString("Id"));
        }
        return allItems;
    }
}
