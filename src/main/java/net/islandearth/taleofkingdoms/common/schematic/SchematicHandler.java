package net.islandearth.taleofkingdoms.common.schematic;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.forge.ForgeAdapter;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Handles schematics for TaleOfKingdoms.
 * Works on both SERVER and CLIENT.
 */
public class SchematicHandler {

	public static OperationInstance pasteSchematic(Schematic schematic, PlayerEntity player) {
		TaleOfKingdoms.LOGGER.info("Loading schematic, please wait: " + schematic.toString());
		com.sk89q.worldedit.world.World adaptedWorld = ForgeAdapter.adapt(player.getEntityWorld());
		ClipboardFormat format = ClipboardFormats.findByFile(schematic.getFile());

		try (ClipboardReader reader = format.getReader(new FileInputStream(schematic.getFile()))) {
			Clipboard clipboard = reader.read();
			try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1)) {
				BlockVector3 centerY = clipboard.getRegion().getCenter().toBlockPoint();
				BlockPos position = player.getPosition();
				Chunk chunk = player.getEntityWorld().getChunkAt(position);
				int topY = chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE, centerY.getBlockX(), centerY.getBlockZ());
				Operation operation = new ClipboardHolder(clipboard).createPaste(editSession)
                        .to(BlockVector3.at(position.getX(), position.getY() + 1, position.getZ()))
                        .ignoreAirBlocks(false)
                        .copyBiomes(false)
                        .build();
				final UUID uuid = UUID.randomUUID();

				// This will have different things in the future...
				if (player.getEntityWorld().isRemote()) {
					// Server
					Operations.completeBlindly(uuid, operation);
				} else {
					// Client
					Operations.completeBlindly(uuid, operation);
				}
				
				return new OperationInstance(uuid, clipboard.getRegion().getArea());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
