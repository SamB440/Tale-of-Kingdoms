package net.islandearth.taleofkingdoms.common.entity.guild;

import net.islandearth.taleofkingdoms.TaleOfKingdoms;
import net.islandearth.taleofkingdoms.client.gui.entity.InnkeeperScreen;
import net.islandearth.taleofkingdoms.client.translation.Translations;
import net.islandearth.taleofkingdoms.common.entity.EntityTypes;
import net.islandearth.taleofkingdoms.common.entity.TOKEntity;
import net.islandearth.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class InnkeeperEntity extends TOKEntity {

    public InnkeeperEntity(World worldIn) {
        super(EntityTypes.INNKEEPER, worldIn);
    }

    public InnkeeperEntity(EntityType<InnkeeperEntity> innkeeperEntityEntityType, World world) {
        super(innkeeperEntityEntityType, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new LookAtGoal(this, PlayerEntity.class, 15.0F));
        applyEntityAI();
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        if (hand == Hand.OFF_HAND || player.world.isRemote) return false;
        ConquestInstance instance = TaleOfKingdoms.getAPI().get().getConquestInstanceStorage().getConquestInstance(Minecraft.getInstance().getIntegratedServer().getFolderName()).get();
        if (!instance.hasContract()) {
            Translations.NEED_CONTRACT.send(player);
            return false;
        }

        InnkeeperScreen screen = new InnkeeperScreen(player, this, instance);
        Minecraft.getInstance().displayGuiScreen(screen);
        return true;
    }

    @Override
    public boolean isStationary() {
        return true;
    }
}
