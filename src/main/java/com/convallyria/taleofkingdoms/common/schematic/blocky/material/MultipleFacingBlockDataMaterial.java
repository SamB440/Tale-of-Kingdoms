package com.convallyria.taleofkingdoms.common.schematic.blocky.material;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;

public class MultipleFacingBlockDataMaterial extends BlockDataMaterial {

    public MultipleFacingBlockDataMaterial(BlockState state) {
        super(state);
    }

    @Override
    public BlockState update(Direction face) {
        return this.getState();
    }

    /*@Override
    public BlockState update(BlockState face) {
        BlockState state = this.getState();
        HorizontalConnectingBlock facing = (HorizontalConnectingBlock) state.getBlock();
        // ??? where's the api for setting these automatically... https://www.spigotmc.org/threads/force-updating-fence-connections.461672/
        facing.getAllowedFaces().forEach(bf -> {
            Block rel = state.getBlock().getRelative(bf);
            if (rel.getType() == Material.AIR
                    || rel.getType().toString().contains("SLAB")
                    || rel.getType().toString().contains("STAIRS")) {
                if (facing.hasFace(bf)) facing.setFace(bf, false);
            } else {
                if (!rel.getType().toString().contains("SLAB")
                        && !rel.getType().toString().contains("STAIRS")
                        && !Tag.ANVIL.getValues().contains(rel.getType())
                        && rel.getType().isSolid()
                        && rel.getType().isBlock()) {
                    if (!facing.hasFace(bf)) facing.setFace(bf, true);
                }
            }
        });

        return state;
    }*/
}
