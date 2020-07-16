package net.islandearth.taleofkingdoms.common.world;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.TaleOfKingdomsAPI;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConquestInstance {
	
	private String world;
	private String name;
	private int coins;
	private boolean hasLoaded;
	private long farmerLastBread;
	private boolean hasContract;
	private int worthiness;
	private BlockPos start;
	private BlockPos end;
	
	public ConquestInstance(String world, String name, BlockPos start, BlockPos end) {
		Optional<ConquestInstance> instance = TaleOfKingdoms.getAPI()
			.map(TaleOfKingdomsAPI::getConquestInstanceStorage)
			.orElseThrow(() -> new IllegalArgumentException("API not present"))
		.getConquestInstance(world);
		if (instance.isPresent() && instance.get().isLoaded()) throw new IllegalArgumentException("World already registered");
		this.world = world;
		this.name = name;
		this.start = start;
		this.end = end;
	}

	public String getWorld() {
		return world;
	}
	
	public String getName() {
		return name;
	}
	
	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public void addCoins(int coins) {
		this.coins = this.coins + coins;
	}
	
	public boolean isLoaded() {
		return hasLoaded;
	}
	
	public void setLoaded(boolean loaded) {
		this.hasLoaded = loaded;
	}

	public long getFarmerLastBread() {
		return farmerLastBread;
	}
	
	public void setFarmerLastBread(long day) {
		this.farmerLastBread = day;
	}

	public boolean hasContract() {
		return hasContract;
	}

	public void setHasContract(boolean hasContract) {
		this.hasContract = hasContract;
	}

	public int getWorthiness() {
		return worthiness;
	}

	public void setWorthiness(int worthiness) {
		this.worthiness = worthiness;
	}

	public void addWorthiness(int worthiness) {
		this.worthiness = this.worthiness + worthiness;
	}

	public BlockPos getStart() {
		return start;
	}

	public BlockPos getEnd() {
		return end;
	}

	private List<BlockPos> validRest;

	/**
	 * Gets valid sleep area locations. This gets the sign, not the bed head.
	 * @param player the player
	 * @return list of signs where sleeping is allowed
	 */
	@NotNull
	public List<BlockPos> getSleepLocations(PlayerEntity player) {
		if (validRest == null) validRest = new ArrayList<>();
		if (validRest.isEmpty()) { // Find a valid resting place. This will only run if validRest is empty, which is also saved to file.
			int topBlockX = (Math.max(start.getX(), end.getX()));
			int bottomBlockX = (Math.min(start.getX(), end.getX()));

			int topBlockY = (Math.max(start.getY(), end.getY()));
			int bottomBlockY = (Math.min(start.getY(), end.getY()));

			int topBlockZ = (Math.max(start.getZ(), end.getZ()));
			int bottomBlockZ = (Math.min(start.getZ(), end.getZ()));

			for (int x = bottomBlockX; x <= topBlockX; x++) {
				for (int z = bottomBlockZ; z <= topBlockZ; z++) {
					for (int y = bottomBlockY; y <= topBlockY; y++) {
						BlockPos blockPos = new BlockPos(x, y, z);
						TileEntity tileEntity = player.getEntityWorld().getChunkAt(blockPos).getTileEntity(blockPos);
						if (tileEntity instanceof SignTileEntity) {
							SignTileEntity signTileEntity = (SignTileEntity) tileEntity;
							if (signTileEntity.getText(0).getFormattedText().equals("[Rest]")) {
								validRest.add(blockPos);
							}
						}
					}
				}
			}
		}

		return validRest;
	}

	/**
	 * Checks if a player is in the guild.
	 * @param entity the entity
	 * @return true if player is in guild, false if not
	 */
	public boolean isInGuild(LivingEntity entity) {
		BlockVector3 firstPos = BlockVector3.at(start.getX(), start.getY(), start.getZ());
		BlockVector3 secondPos = BlockVector3.at(end.getX(), end.getY(), end.getZ());
		Region region = new CuboidRegion(firstPos, secondPos);
		BlockVector3 playerLoc = BlockVector3.at(entity.getPosX(), entity.getPosY(), entity.getPosZ());
		return region.contains(playerLoc);
	}
}
