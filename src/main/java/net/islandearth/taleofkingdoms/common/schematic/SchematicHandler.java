package net.islandearth.taleofkingdoms.common.schematic;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Handles schematics for TaleOfKingdoms.
 * Works on both SERVER and CLIENT.
 */
public class SchematicHandler {

	public static CompletableFuture<OperationInstance> pasteSchematic(Schematic schematic, PlayerEntity player) {
        CompletableFuture<OperationInstance> cf = new CompletableFuture<>();
	    MinecraftClient.getInstance().getServer().execute(() -> {
            TaleOfKingdoms.LOGGER.info("Loading schematic, please wait: " + schematic.toString());
            com.sk89q.worldedit.world.World adaptedWorld = FabricAdapter.adapt(player.getEntityWorld());
            ClipboardFormat format = ClipboardFormats.findByFile(schematic.getFile());
    
            try (ClipboardReader reader = format.getReader(new FileInputStream(schematic.getFile()))) {
                Clipboard clipboard = reader.read();
                try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1)) {
                    BlockPos position = player.getBlockPos();
                    clipboard.setOrigin(BlockVector3.at(position.getX(), position.getY(), position.getZ()));
                    BlockVector3 centerY = clipboard.getRegion().getCenter().toBlockPoint();
                    System.out.println(centerY);
                    Operation operation = new ClipboardHolder(clipboard).createPaste(editSession)
                            .to(BlockVector3.at(position.getX(), position.getY() + 1, position.getZ()))
                            .ignoreAirBlocks(false)
                            .build();
                    final UUID uuid = UUID.randomUUID();
                    try {
                        com.sk89q.worldedit.function.operation.Operations.complete(operation);
                        editSession.flushSession();
                    } catch (WorldEditException e) {
                        e.printStackTrace();
                    }
                    System.out.println("finished");
                    cf.complete(new OperationInstance(uuid, clipboard.getRegion()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
		return cf;
	}
}
