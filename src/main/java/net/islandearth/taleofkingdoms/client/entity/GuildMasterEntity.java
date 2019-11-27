package net.islandearth.taleofkingdoms.client.entity;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.client.gui.entity.GuildMasterScreen;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuildMasterEntity extends TOKEntity {

	public GuildMasterEntity(World worldIn) {
		super(EntityTypes.GUILD_MASTER, worldIn);
	}

	public GuildMasterEntity(EntityType<GuildMasterEntity> guildMasterEntityEntityType, World world) {
		super(guildMasterEntityEntityType, world);
	}

	@Override
	public boolean processInteract(PlayerEntity player, Hand hand) {
		if (hand == Hand.OFF_HAND || player.world.isRemote) return false;
		ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().getConquestInstance(Minecraft.getInstance().getIntegratedServer().getFolderName()).get();
		GuildMasterScreen screen = new GuildMasterScreen(player, this, instance);
		Minecraft.getInstance().displayGuiScreen(screen);
		return true;
	}


	@Override
	public boolean isStationary() {
		return true;
	}
}
