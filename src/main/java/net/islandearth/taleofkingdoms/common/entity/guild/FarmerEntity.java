package net.islandearth.taleofkingdoms.common.entity.guild;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.entity.EntityTypes;
import net.islandearth.taleofkingdoms.common.entity.TOKEntity;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.concurrent.ThreadLocalRandom;

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
			player.sendMessage(new TranslationTextComponent("taleofkingdoms.entity.farmer.got_bread"));
			return false;
		}
		
		// Set the current day and add bread to inventory
		instance.setFarmerLastBread(day);
		player.sendMessage(new TranslationTextComponent("taleofkingdoms.entity.farmer.take_bread"));
		int amount = ThreadLocalRandom.current().nextInt(1, 4);
		player.inventory.addItemStackToInventory(new ItemStack(Items.BREAD, amount));
		return true;
	}
}
