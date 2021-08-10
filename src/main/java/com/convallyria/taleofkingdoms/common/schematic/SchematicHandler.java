package com.convallyria.taleofkingdoms.common.schematic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.generator.processor.GuildStructureProcessor;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.JigsawReplacementProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Handles schematics for TaleOfKingdoms.
 * Works on both SERVER and CLIENT.
 */
public abstract class SchematicHandler {

    /**
     * Pastes the selected schematic. Returns a {@link CompletableFuture} containing the {@link BoundingBox}
     * @param schematic schematic to paste
     * @param player the <b><i>server</i></b> player
     * @param position the {@link BlockPos} position to paste at
     * @return {@link CompletableFuture} containing the {@link BoundingBox}
     */
    public abstract CompletableFuture<BoundingBox> pasteSchematic(Schematic schematic, ServerPlayer player, BlockPos position, SchematicOptions... options);

    /**
     * Pastes the selected schematic. Returns a {@link CompletableFuture} containing the {@link BoundingBox}.
     * This defaults the position parameter to: <br>
     *     <b>x, y + 1, z</b>
     * @see #pasteSchematic(Schematic, ServerPlayer, BlockPos, SchematicOptions...)
     * @param schematic schematic to paste
     * @param player the <b><i>server</i></b> player
     * @return {@link CompletableFuture} containing the {@link BoundingBox}
     */
    @NotNull
    public CompletableFuture<BoundingBox> pasteSchematic(Schematic schematic, ServerPlayer player, SchematicOptions... options) {
	    return pasteSchematic(schematic, player, player.blockPosition().offset(0, 1, 0), options);
    }

    protected void pasteSchematic(Schematic schematic, ServerPlayer player, BlockPos position, CompletableFuture<BoundingBox> cf, SchematicOptions... options) {
        TaleOfKingdoms.LOGGER.info("Loading schematic, please wait: " + schematic.toString());
        player.getLevel().getStructureManager().get(schematic.getPath()).ifPresent(structure -> {
            SharedConstants.IS_RUNNING_IN_IDE = true; // We want to crash if something went wrong
            StructurePlaceSettings structurePlacementData = new StructurePlaceSettings();
            structurePlacementData.addProcessor(new GuildStructureProcessor(options));
            structurePlacementData.addProcessor(JigsawReplacementProcessor.INSTANCE);
            structure.placeInWorld(player.getLevel(), position, position, structurePlacementData, ThreadLocalRandom.current(), Block.UPDATE_ALL);
            BoundingBox box = structure.getBoundingBox(structurePlacementData, position);
            cf.complete(box);
            SharedConstants.IS_RUNNING_IN_IDE = false; // Put it back to what it was.
        });
    }
}
