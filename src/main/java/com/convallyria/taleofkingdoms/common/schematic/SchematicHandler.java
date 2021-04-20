package com.convallyria.taleofkingdoms.common.schematic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.generator.processor.GuildStructureProcessor;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.JigsawReplacementStructureProcessor;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Handles schematics for TaleOfKingdoms.
 * Works on both SERVER and CLIENT.
 */
public abstract class SchematicHandler {

    /**
     * Pastes the selected schematic. Returns a {@link CompletableFuture} containing the {@link BlockBox}
     * @param schematic schematic to paste
     * @param player the <b><i>server</i></b> player
     * @param position the {@link BlockPos} position to paste at
     * @return {@link CompletableFuture} containing the {@link BlockBox}
     */
    public abstract CompletableFuture<BlockBox> pasteSchematic(Schematic schematic, ServerPlayerEntity player, BlockPos position);

    /**
     * Pastes the selected schematic. Returns a {@link CompletableFuture} containing the {@link BlockBox}.
     * This defaults the position parameter to: <br>
     *     <b>x, y + 1, z</b>
     * @see #pasteSchematic(Schematic, ServerPlayerEntity, BlockPos)
     * @param schematic schematic to paste
     * @param player the <b><i>server</i></b> player
     * @return {@link CompletableFuture} containing the {@link BlockBox}
     */
    @NotNull
    public CompletableFuture<BlockBox> pasteSchematic(Schematic schematic, ServerPlayerEntity player) {
	    return pasteSchematic(schematic, player, player.getBlockPos().add(0, 1, 0));
    }

    protected void pasteSchematic(Schematic schematic, ServerPlayerEntity player, BlockPos position, CompletableFuture<BlockBox> cf) {
        TaleOfKingdoms.LOGGER.info("Loading schematic, please wait: " + schematic.toString());
        Structure structure = player.getServerWorld().getStructureManager().getStructure(schematic.getPath());
        StructurePlacementData structurePlacementData = new StructurePlacementData();
        structurePlacementData.addProcessor(GuildStructureProcessor.INSTANCE);
        structurePlacementData.addProcessor(JigsawReplacementStructureProcessor.INSTANCE);
        structure.place(player.getServerWorld(), position, structurePlacementData, ThreadLocalRandom.current());
        BlockBox box = structure.calculateBoundingBox(structurePlacementData, position);
        cf.complete(box);
    }
}
