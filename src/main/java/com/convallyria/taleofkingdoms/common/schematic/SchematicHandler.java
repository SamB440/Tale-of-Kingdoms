package com.convallyria.taleofkingdoms.common.schematic;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.generator.processor.GatewayStructureProcessor;
import com.convallyria.taleofkingdoms.common.generator.processor.GuildStructureProcessor;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.structure.processor.JigsawReplacementStructureProcessor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

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
    public abstract CompletableFuture<BlockBox> pasteSchematic(Schematic schematic, ServerPlayerEntity player, BlockPos position);

    /**
     * Pastes the selected schematic. Returns a {@link CompletableFuture} containing the {@link OperationInstance}.
     * This defaults the position parameter to: <br>
     *     <b>x, y + 1, z</b>
     * @see #pasteSchematic(Schematic, ServerPlayerEntity, BlockPos)
     * @param schematic schematic to paste
     * @param player the <b><i>server</i></b> player
     * @return {@link CompletableFuture} containing the {@link OperationInstance}
     */
    @NotNull
    public CompletableFuture<BlockBox> pasteSchematic(Schematic schematic, ServerPlayerEntity player) {
	    return pasteSchematic(schematic, player, player.getBlockPos().add(0, 1, 0));
    }

    protected void pasteSchematic(Schematic schematic, ServerPlayerEntity player, BlockPos position, CompletableFuture<BlockBox> cf) {
        TaleOfKingdoms.LOGGER.info("Loading schematic, please wait: " + schematic.toString());
        Identifier guild = new Identifier(TaleOfKingdoms.MODID, "guild/guild");
        Structure structure = player.getServerWorld().getStructureManager().getStructure(guild);
        StructurePlacementData structurePlacementData = new StructurePlacementData();
        structurePlacementData.addProcessor(GuildStructureProcessor.INSTANCE);
        structurePlacementData.addProcessor(JigsawReplacementStructureProcessor.INSTANCE);
        cf.complete(structurePlacementData.getBoundingBox());
        structure.place(player.getServerWorld(), position, structurePlacementData, ThreadLocalRandom.current());
        
        /*World adaptedWorld = FabricAdapter.adapt(player.getServerWorld());
        ClipboardFormat format = ClipboardFormats.findByFile(schematic.getFile());
        try {
            Clipboard clipboard = format.getReader(new FileInputStream(schematic.getFile())).read();
            EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder()
                    .world(adaptedWorld)
                    .maxBlocks(-1)
                    .build();

            ClipboardHolder clipboardHolder = new ClipboardHolder(clipboard);
            Operation operation = clipboardHolder.createPaste(editSession)
                    .to(position)
                    .ignoreAirBlocks(false)
                    .build();
            final UUID uuid = UUID.randomUUID();
            Operations.complete(operation);
            editSession.close();

            Region region = clipboard.getRegion();
            BlockVector3 clipboardOffset = clipboard.getRegion().getMinimumPoint().subtract(clipboard.getOrigin());
            Vector3 realTo = position.toVector3().add(clipboardHolder.getTransform().apply(clipboardOffset.toVector3()));
            Vector3 max = realTo.add(clipboardHolder.getTransform().apply(region.getMaximumPoint().subtract(region.getMinimumPoint()).toVector3()));
            RegionSelector selector = new CuboidRegionSelector(adaptedWorld, realTo.toBlockPoint(), max.toBlockPoint());

            BlockVector3 centerY = selector.getRegion().getCenter().toBlockPoint();
            TaleOfKingdoms.LOGGER.info(centerY); // Mainly debug, can be used to find the schematic in the world

            cf.complete(new OperationInstance(uuid, selector.getRegion()));
        } catch (WorldEditException | IOException e) {
            e.printStackTrace();
        }*/
    }
}
