package net.islandearth.taleofkingdoms.client.entity.guild;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.client.entity.EntityTypes;
import net.islandearth.taleofkingdoms.client.entity.TOKEntity;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FarmerEntity extends TOKEntity {
	
	public FarmerEntity(World worldIn) {
		super(EntityTypes.FARMER, worldIn);
		this.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.IRON_HOE));
	}
	
	public FarmerEntity(EntityType<FarmerEntity> farmerEntityEntityType, World world) {
		super(farmerEntityEntityType, world);
		this.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.IRON_HOE));
	}
	
	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new LookAtGoal(this, PlayerEntity.class, 10.0F));
		applyEntityAI();
	}

	@Override
	public boolean isStationary() {
		return true;
	}

	@Override
	public boolean processInteract(PlayerEntity player, Hand hand) {
		if (hand == Hand.OFF_HAND || player.world.isRemote) return false;
		
		// Check if there is at least 1 Minecraft day difference
		ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().getConquestInstance(Minecraft.getInstance().getIntegratedServer().getFolderName()).get();
		long day = player.world.getDayTime() / 24000L;
		if (instance.getFarmerLastBread() >= day) {
			player.sendMessage(new StringTextComponent("Farmer: You got your bread for now!"));
			return false;
		}
		
		// Set the current day and add bread to inventory
		instance.setFarmerLastBread(day);
		player.sendMessage(new StringTextComponent("Farmer: Here, take a bread!"));
		player.inventory.addItemStackToInventory(new ItemStack(Items.BREAD));
		return true;
	}
}
