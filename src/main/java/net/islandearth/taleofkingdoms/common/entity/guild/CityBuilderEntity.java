package net.islandearth.taleofkingdoms.common.entity.guild;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.common.entity.EntityTypes;
import net.islandearth.taleofkingdoms.common.entity.TOKEntity;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class CityBuilderEntity extends TOKEntity {

    public CityBuilderEntity(World worldIn) {
        super(EntityTypes.CITY_BUILDER, worldIn);
    }

    public CityBuilderEntity(EntityType<CityBuilderEntity> cityBuilderEntityEntityType, World world) {
        super(cityBuilderEntityEntityType, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new LookAtGoal(this, PlayerEntity.class, 10.0F));
        applyEntityAI();
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
        return true;
    }
}
