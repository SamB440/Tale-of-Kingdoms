package net.islandearth.taleofkingdoms.schematic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.forge.ForgeAdapter;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.minecraft.entity.player.PlayerEntity;

public class SchematicHandler {

	public static void pasteSchematic(Schematic schematic, PlayerEntity player) {
		TaleOfKingdoms.LOGGER.info("Loading schematic, please wait: " + schematic.toString());
		com.sk89q.worldedit.world.World adaptedWorld = ForgeAdapter.adapt(player.getEntityWorld());
		ClipboardFormat format = ClipboardFormats.findByFile(schematic.getFile());

		try (ClipboardReader reader = format.getReader(new FileInputStream(schematic.getFile()))) {
			Clipboard clipboard = reader.read();
			try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(adaptedWorld, -1)) {
				Operation operation = new ClipboardHolder(clipboard).createPaste(editSession)
                        .to(BlockVector3.at(player.posX, player.posY, player.posZ))
                        .ignoreAirBlocks(false)
                        .copyBiomes(false)
                        .build();
				try {
					Operations.complete(operation);
					editSession.flushSession();

				} catch (WorldEditException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
