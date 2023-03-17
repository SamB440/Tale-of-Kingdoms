package com.convallyria.taleofkingdoms.common.entity.guild;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.client.gui.entity.citybuilder.CityBuilderBeginGui;
import com.convallyria.taleofkingdoms.client.gui.entity.citybuilder.CityBuilderTierOneGui;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.FollowPlayerGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.WalkToTargetGoal;
import com.convallyria.taleofkingdoms.common.kingdom.KingdomTier;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.kingdom.builds.BuildCosts;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CityBuilderEntity extends TOKEntity implements InventoryOwner {

    private static final TrackedData<Boolean> MOVING_TO_LOCATION;

    static {
        MOVING_TO_LOCATION = DataTracker.registerData(CityBuilderEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(MOVING_TO_LOCATION, false);
    }

    private final FollowPlayerGoal followPlayerGoal = new FollowPlayerGoal(this, 0.75F, 5, 50);
    private WalkToTargetGoal currentBlockTarget;

    private final SimpleInventory inventory = new SimpleInventory(10);

    public CityBuilderEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F, 100F));
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (hand == Hand.OFF_HAND) return ActionResult.FAIL;
        TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            final PlayerKingdom kingdom = instance.getKingdom(player.getUuid());
            if (kingdom != null) {
                if (!player.world.isClient()) return;
                openScreen(player, kingdom, instance);
                return;
            }

            if (this.getDataTracker().get(MOVING_TO_LOCATION)) {
                BlockPos current = this.getBlockPos();
                int distance = (int) instance.getCentre().distanceTo(new Vec3d(current.getX(), current.getY(), current.getZ()));
//                if (distance < (3000)) {
//                    Translations.CITYBUILDER_DISTANCE.send(player, distance, 3000);
//                    return;
//                }

                if (player.world.isClient()) {
                    openScreen(player, null, instance);
                }
                return;
            }

            if (instance.getWorthiness(player.getUuid()) >= 1500) {
                if (player.world.isClient()) Translations.CITYBUILDER_BUILD.send(player);
                this.followPlayer();
            } else {
                if (player.world.isClient()) Translations.CITYBUILDER_MESSAGE.send(player);
            }
        });
        return ActionResult.PASS;
    }

    public void followPlayer() {
        this.getDataTracker().set(MOVING_TO_LOCATION, true);
        this.goalSelector.add(2, followPlayerGoal);
    }

    public void stopFollowingPlayer() {
        this.getDataTracker().set(MOVING_TO_LOCATION, false);
        this.goalSelector.remove(followPlayerGoal);
    }

    public void setTarget(BlockPos pos) {
        this.goalSelector.add(3, this.currentBlockTarget = new WalkToTargetGoal(this, 0.75F, pos));
    }

    public void stopTarget() {
        this.goalSelector.remove(this.currentBlockTarget);
    }

    @Environment(EnvType.CLIENT)
    private void openScreen(PlayerEntity player, @Nullable PlayerKingdom kingdom, ConquestInstance instance) {
        if (kingdom == null) {
            MinecraftClient.getInstance().setScreen(new CityBuilderBeginGui(player, this, instance));
            return;
        }

        if (kingdom.getTier() == KingdomTier.TIER_ONE) {
            MinecraftClient.getInstance().setScreen(new CityBuilderTierOneGui(player, this, instance));
        }
    }

    public boolean canAffordBuild(BuildCosts build) {
       return inventory.count(Items.COBBLESTONE) >= build.getStone() && inventory.count(Items.OAK_LOG) >= build.getWood();
    }

    public void requireResources(BuildCosts build, Runnable runnable) {
        if (canAffordBuild(build)) {
            inventory.removeItem(Items.OAK_LOG, build.getWood());
            inventory.removeItem(Items.COBBLESTONE, build.getStone());
            runnable.run();
        }
    }

    @Override
    public boolean isStationary() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public boolean damage(DamageSource damageSource, float f) {
        return false;
    }

    @Override
    public SimpleInventory getInventory() {
        return inventory;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("Inventory", this.inventory.toNbtList());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.inventory.readNbtList(nbt.getList("Inventory", 10));
    }
}
