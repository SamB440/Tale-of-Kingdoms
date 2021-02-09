package com.convallyria.taleofkingdoms.common.entity.guild;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.generic.LoneVillagerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class LoneEntity extends TOKEntity {

    public LoneEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F, 100F));
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (hand == Hand.OFF_HAND || !player.world.isClient()) return ActionResult.FAIL;
        TaleOfKingdoms.getAPI().flatMap(api -> api.getConquestInstanceStorage().mostRecentInstance()).ifPresent(instance -> {
            BlockPos startPos = instance.getStart();
            BlockPos endPos = instance.getEnd();
            Vec3d start = new Vec3d(startPos.getX(), startPos.getY(), startPos.getZ());
            Vec3d end = new Vec3d(endPos.getX(), endPos.getY(), endPos.getZ());
            Box region = new Box(start, end);
            List<LoneVillagerEntity> loneVillagers = player.world.getEntitiesByType(EntityTypes.LONEVILLAGER, region, predicate -> {
                return instance.isInGuild(predicate.getBlockPos());
            });

            if (!loneVillagers.isEmpty()) {
                for (LoneVillagerEntity loneVillager : loneVillagers) {
                    loneVillager.kill();
                }
                Translations.LONE_THANK.send(player);
                instance.setWorthiness(player.getUuid(), instance.getWorthiness(player.getUuid()) + loneVillagers.size() * 10);
            } else {
                Translations.LONE_HELP.send(player);
            }
        });
        return ActionResult.PASS;
    }

    @Override
    public boolean isStationary() {
        return true;
    }
}
