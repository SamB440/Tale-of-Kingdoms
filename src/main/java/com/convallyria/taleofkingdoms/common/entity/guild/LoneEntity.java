package com.convallyria.taleofkingdoms.common.entity.guild;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.generic.LoneVillagerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class LoneEntity extends TOKEntity {

    public LoneEntity(EntityType<? extends PathfinderMob> entityType, Level world) {
        super(entityType, world);
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 10.0F, 100F));
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (hand == InteractionHand.OFF_HAND || player.level.isClientSide()) return InteractionResult.FAIL;
        TaleOfKingdoms.getAPI().flatMap(api -> api.getConquestInstanceStorage().mostRecentInstance()).ifPresent(instance -> {
            BlockPos startPos = instance.getStart();
            BlockPos endPos = instance.getEnd();
            Vec3 start = new Vec3(startPos.getX(), startPos.getY(), startPos.getZ());
            Vec3 end = new Vec3(endPos.getX(), endPos.getY(), endPos.getZ());
            AABB region = new AABB(start, end);
            List<LoneVillagerEntity> loneVillagers = player.level.getEntities(EntityTypes.LONEVILLAGER, region, predicate -> {
                return instance.isInGuild(predicate.blockPosition())
                        && !instance.getLoneVillagersWithRooms().contains(predicate.getUUID());
            });

            if (!loneVillagers.isEmpty()) {
                List<BlockPos> sleepLocations = instance.getSleepLocations(player);
                for (LoneVillagerEntity loneVillager : loneVillagers) {
                    instance.addLoneVillagerWithRoom(loneVillager);
                    loneVillager.setMovementEnabled(false);
                    Random random = ThreadLocalRandom.current();
                    BlockPos sleepLocation = sleepLocations.get(random.nextInt(sleepLocations.size()));
                    loneVillager.moveTo(sleepLocation.getX() + 0.5, sleepLocation.getY(), sleepLocation.getZ() + 0.5);
                }

                Translations.LONE_THANK.send(player);
                instance.setWorthiness(player.getUUID(), instance.getWorthiness(player.getUUID()) + loneVillagers.size() * 6);
                player.displayClientMessage(new TextComponent("+" + loneVillagers.size() * 6 + " worthiness"), true);
            } else {
                Translations.LONE_HELP.send(player);
            }
        });
        return InteractionResult.PASS;
    }

    @Override
    public boolean isStationary() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }
}
