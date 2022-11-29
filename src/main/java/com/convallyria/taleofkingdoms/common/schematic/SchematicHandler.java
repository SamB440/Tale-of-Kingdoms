package com.convallyria.taleofkingdoms.common.schematic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.generator.processor.GuildStructureProcessor;
import com.convallyria.taleofkingdoms.common.generator.processor.PlayerKingdomStructureProcessor;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import net.minecraft.SharedConstants;
import net.minecraft.block.Block;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.JigsawReplacementStructureProcessor;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

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
    public abstract CompletableFuture<BlockBox> pasteSchematic(Schematic schematic, ServerPlayerEntity player, BlockPos position, SchematicOptions... options);

    /**
     * Pastes the selected schematic. Returns a {@link CompletableFuture} containing the {@link BlockBox}.
     * This defaults the position parameter to: <br>
     *     <b>x, y + 1, z</b>
     * @see #pasteSchematic(Schematic, ServerPlayerEntity, BlockPos, SchematicOptions...)
     * @param schematic schematic to paste
     * @param player the <b><i>server</i></b> player
     * @return {@link CompletableFuture} containing the {@link BlockBox}
     */
    @NotNull
    public CompletableFuture<BlockBox> pasteSchematic(Schematic schematic, ServerPlayerEntity player, SchematicOptions... options) {
	    return pasteSchematic(schematic, player, player.getBlockPos().add(0, 1, 0), options);
    }

    protected void pasteSchematic(Schematic schematic, ServerPlayerEntity player, BlockPos position, CompletableFuture<BlockBox> cf, SchematicOptions... options) {
        TaleOfKingdoms.LOGGER.info("Loading schematic, please wait: " + schematic.toString());
        player.getWorld().getStructureTemplateManager().getTemplate(schematic.getPath()).ifPresent(structure -> {
            final boolean old = SharedConstants.isDevelopment;
            SharedConstants.isDevelopment = true; // We want to crash if something went wrong
            StructurePlacementData structurePlacementData = new StructurePlacementData();
            //todo: rotation options
            TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
                final PlayerKingdom kingdom = instance.getKingdom(player.getUuid());
                if (kingdom == null) return;
                structurePlacementData.addProcessor(new PlayerKingdomStructureProcessor(kingdom, player));
            });
            structurePlacementData.addProcessor(new GuildStructureProcessor(options));
            structurePlacementData.addProcessor(JigsawReplacementStructureProcessor.INSTANCE);
            structure.place(player.getWorld(), position, position, structurePlacementData, Random.create(), Block.NOTIFY_ALL);
            BlockBox box = structure.calculateBoundingBox(structurePlacementData, position);
            cf.complete(box);
            SharedConstants.isDevelopment = old; // Put it back to what it was.
        });
    }
}
