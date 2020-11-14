package net.islandearth.taleofkingdoms.common.schematic;

import com.sk89q.worldedit.math.BlockVector3;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * Handles schematics for TaleOfKingdoms.
 * Works on both SERVER and CLIENT.
 */
public abstract class SchematicHandler {

    /**
     * Pastes the selected schematic. Returns a {@link CompletableFuture} containing the {@link OperationInstance}
     * @param schematic schematic to paste
     * @param player the <b><i>server</i></b> player
     * @param position the {@link BlockVector3} position to paste at
     * @return {@link CompletableFuture} containing the {@link OperationInstance}
     */
    @Nullable
    public abstract CompletableFuture<OperationInstance> pasteSchematic(Schematic schematic, ServerPlayerEntity player, BlockVector3 position);

    /**
     * Pastes the selected schematic. Returns a {@link CompletableFuture} containing the {@link OperationInstance}.
     * This defaults the position parameter to: <br>
     *     <b>x, y + 1, z</b>
     * @see #pasteSchematic(Schematic, ServerPlayerEntity, BlockVector3) 
     * @param schematic schematic to paste
     * @param player the <b><i>server</i></b> player
     * @return {@link CompletableFuture} containing the {@link OperationInstance}
     */
    public CompletableFuture<OperationInstance> pasteSchematic(Schematic schematic, ServerPlayerEntity player) {
        BlockVector3 position = BlockVector3.at(player.getBlockPos().getX(), player.getBlockPos().getY() + 1, player.getBlockPos().getZ());
	    return pasteSchematic(schematic, player, position);
    }
}
