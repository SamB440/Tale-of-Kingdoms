package net.islandearth.taleofkingdoms.common.entity.generic;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.entity.EntityTypes;
import net.islandearth.taleofkingdoms.common.entity.TOKEntity;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class KnightEntity extends TOKEntity {

    public KnightEntity(World worldIn) {
        super(EntityTypes.KNIGHT, worldIn);
        this.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
    }

    public KnightEntity(EntityType<KnightEntity> knightEntityEntityType, World world) {
        super(knightEntityEntityType, world);
        this.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, MobEntity.class, true, true));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new LookAtGoal(this, PlayerEntity.class, 10.0F));
        applyEntityAI();
    }

    @Override
    public void registerAttributes() {
        super.registerAttributes();
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(15.0D);
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        if (hand == Hand.OFF_HAND || player.world.isRemote) return false;
        ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().getConquestInstance(Minecraft.getInstance().getIntegratedServer().getFolderName()).get();
        //TODO
        return true;
    }

    @Override
    public boolean isStationary() {
        return false;
    }
}
