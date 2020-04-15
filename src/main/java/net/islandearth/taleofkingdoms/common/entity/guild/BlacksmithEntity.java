package net.islandearth.taleofkingdoms.common.entity.guild;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.client.gui.entity.BlacksmithScreen;
import net.islandearth.taleofkingdoms.common.entity.EntityTypes;
import net.islandearth.taleofkingdoms.common.entity.TOKEntity;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class BlacksmithEntity extends TOKEntity {

    public BlacksmithEntity(World worldIn) {
        super(EntityTypes.BLACKSMITH, worldIn);
    }

    public BlacksmithEntity(EntityType<BlacksmithEntity> blacksmithEntityEntityType, World world) {
        super(blacksmithEntityEntityType, world);
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
        BlacksmithScreen screen = new BlacksmithScreen(player, this, instance);
        Minecraft.getInstance().displayGuiScreen(screen);
        return true;
    }

    @Override
    public boolean isStationary() {
        return true;
    }
}
