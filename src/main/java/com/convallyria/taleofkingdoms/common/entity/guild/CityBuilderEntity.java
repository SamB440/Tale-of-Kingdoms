package com.convallyria.taleofkingdoms.common.entity.guild;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.FollowPlayerGoal;
import com.convallyria.taleofkingdoms.common.entity.ai.goal.WalkToTargetGoal;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.kingdom.builds.BuildCosts;
import com.convallyria.taleofkingdoms.common.kingdom.poi.KingdomPOI;
import com.convallyria.taleofkingdoms.common.packet.PacketHandler;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.s2c.OpenScreenPacket;
import com.convallyria.taleofkingdoms.common.schematic.Schematic;
import com.convallyria.taleofkingdoms.common.translation.Translations;
import com.convallyria.taleofkingdoms.common.utils.InventoryUtils;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
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
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CityBuilderEntity extends TOKEntity implements InventoryOwner {

    private static final TrackedData<Boolean> MOVING_TO_LOCATION;
    private static final TrackedData<Integer> STONE;
    private static final TrackedData<Integer> WOOD;

    static {
        MOVING_TO_LOCATION = DataTracker.registerData(CityBuilderEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
        STONE = DataTracker.registerData(CityBuilderEntity.class, TrackedDataHandlerRegistry.INTEGER);
        WOOD = DataTracker.registerData(CityBuilderEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(MOVING_TO_LOCATION, false);
        builder.add(STONE, 0);
        builder.add(WOOD, 0);
    }

    private final FollowPlayerGoal followPlayerGoal = new FollowPlayerGoal(this, 0.75F, 5, 50);
    private WalkToTargetGoal currentBlockTarget;

    private final SimpleInventory inventory = new SimpleInventory(10);

    public CityBuilderEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        inventory.addListener((changed) -> {
            this.getDataTracker().set(STONE, changed.count(Items.COBBLESTONE));
            this.getDataTracker().set(WOOD, changed.count(Items.OAK_LOG));
        });
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F, 100F));
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (hand == Hand.OFF_HAND) return ActionResult.FAIL;
        final boolean client = player.getWorld().isClient();
        TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            final GuildPlayer guildPlayer = instance.getPlayer(player);
            final PlayerKingdom kingdom = guildPlayer.getKingdom();
            if (kingdom != null) {
                if (client) return;
                openScreen(player, kingdom, instance);
                return;
            }

            if (this.getDataTracker().get(MOVING_TO_LOCATION)) {
                BlockPos current = this.getBlockPos();
                int distance = (int) instance.getCentre().distanceTo(new Vec3d(current.getX(), current.getY(), current.getZ()));
                if (distance < 500) {
                    if (!client) Translations.CITYBUILDER_DISTANCE.send(player, distance, 500);
                    return;
                }

                if (!client) {
                    openScreen(player, null, instance);
                }
                return;
            }

            if (guildPlayer.getWorthiness() >= 1500) {
                if (client) Translations.CITYBUILDER_BUILD.send(player);
                this.followPlayer();
            } else {
                if (client) Translations.CITYBUILDER_MESSAGE.send(player);
            }
        });
        return ActionResult.PASS;
    }

    public void give64wood(PlayerEntity player) {
        final int playerWoodCount = InventoryUtils.count(player.getInventory(), ItemTags.LOGS);
        TaleOfKingdoms.getAPI().executeOnServerEnvironment((server) -> {
            final ItemStack stack = InventoryUtils.getStack(player.getInventory(), ItemTags.LOGS, 64);
            final ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(player.getUuid());
            final CityBuilderEntity serverCityBuilder = (CityBuilderEntity) serverPlayer.getWorld().getEntityById(this.getId());
            if (stack != null && playerWoodCount >= 64 && getWood() <= (320 - 64) && serverCityBuilder.getInventory().canInsert(stack)) {
                int slot = serverPlayer.getInventory().getSlotWithStack(stack);
                serverPlayer.getInventory().removeStack(slot);
                player.getInventory().removeStack(slot);
                serverCityBuilder.getInventory().addStack(new ItemStack(Items.OAK_LOG, 64));
            }
        });
    }

    public void give64stone(PlayerEntity player) {
        final int playerCobblestoneCount = player.getInventory().count(Items.COBBLESTONE);
        TaleOfKingdoms.getAPI().executeOnServerEnvironment((server) -> {
            final ItemStack stack = new ItemStack(Items.COBBLESTONE, 64);
            final ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(player.getUuid());
            final CityBuilderEntity serverCityBuilder = (CityBuilderEntity) serverPlayer.getWorld().getEntityById(this.getId());
            if (playerCobblestoneCount >= 64 && getStone() <= (320 - 64) && serverCityBuilder.getInventory().canInsert(stack)) {
                int slot = serverPlayer.getInventory().getSlotWithStack(stack);
                serverPlayer.getInventory().removeStack(slot);
                player.getInventory().removeStack(slot);
                serverCityBuilder.getInventory().addStack(stack);
            }
        });
    }

    public void fixKingdom(PlayerEntity player, PlayerKingdom kingdom) {
        if (this.getStone() != 320 || this.getWood() != 320)
            return;

        TaleOfKingdoms.getAPI().executeOnServerEnvironment(server -> {
            final ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(player.getUuid());
            final CityBuilderEntity serverCityBuilder = (CityBuilderEntity) serverPlayer.getWorld().getEntityById(this.getId());
            final Schematic kingdomSchematic = kingdom.getTier().getSchematic();
            BlockPos newOrigin = kingdom.getOrigin().subtract(kingdom.getTier().getOffset());
            TaleOfKingdoms.getAPI().getSchematicHandler().pasteSchematic(kingdomSchematic, serverPlayer, newOrigin);
            for (BuildCosts buildCost : BuildCosts.values()) {
                if (kingdom.getTier() != buildCost.getTier()) continue;
                final KingdomPOI kingdomPOI = buildCost.getKingdomPOI();
                final Schematic schematic = buildCost.getSchematic();
                if (kingdom.hasBuilt(buildCost)) {
                    TaleOfKingdoms.getAPI().getSchematicHandler().pasteSchematic(schematic, serverPlayer, kingdom.getPOIPos(kingdomPOI), buildCost.getSchematicRotation());
                }
            }
            serverCityBuilder.getInventory().clear();
        });
    }

    public CompletableFuture<Void> build(PlayerEntity player, BuildCosts build, PlayerKingdom kingdom) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        TaleOfKingdoms.getAPI().executeOnServerEnvironment(server -> {
            final ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(player.getUuid());
            final CityBuilderEntity serverCityBuilder = (CityBuilderEntity) serverPlayer.getWorld().getEntityById(this.getId());
            kingdom.addBuilt(build);
            TaleOfKingdoms.LOGGER.info("Placing {}...", build);
            TaleOfKingdoms.getAPI().getSchematicHandler().pasteSchematic(build.getSchematic(), serverPlayer, kingdom.getPOIPos(build.getKingdomPOI()), build.getSchematicRotation());

            serverCityBuilder.getInventory().removeItem(Items.OAK_LOG, build.getWood());
            serverCityBuilder.getInventory().removeItem(Items.COBBLESTONE, build.getStone());
            future.complete(null);
        });
        return future;
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
        if (this.currentBlockTarget != null) this.goalSelector.remove(this.currentBlockTarget);
        this.goalSelector.add(3, this.currentBlockTarget = new WalkToTargetGoal(this, 0.75F, pos));
    }

    public void stopTarget() {
        this.goalSelector.remove(this.currentBlockTarget);
    }

    private void openScreen(PlayerEntity player, @Nullable PlayerKingdom kingdom, ConquestInstance instance) {
        final PacketHandler<OpenScreenPacket> open = TaleOfKingdoms.getAPI().getServerPacket(Packets.OPEN_CLIENT_SCREEN);
        if (kingdom == null) {
            open.sendPacket(player, new OpenScreenPacket(OpenScreenPacket.ScreenTypes.CITY_BUILDER_BEGIN, this.getId()));
            return;
        }

        open.sendPacket(player, new OpenScreenPacket(OpenScreenPacket.ScreenTypes.CITY_BUILDER_TIER, this.getId()));
    }

    public boolean canAffordBuild(@Nullable PlayerKingdom kingdom, BuildCosts build) {
       return build.getTier().isLowerThanOrEqual(kingdom == null ? null : kingdom.getTier()) && this.dataTracker.get(STONE) >= build.getStone() && this.dataTracker.get(WOOD) >= build.getWood();
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

    public int getStone() {
        return this.dataTracker.get(STONE);
    }

    public int getWood() {
        return this.dataTracker.get(WOOD);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("Inventory", this.inventory.toNbtList(this.getRegistryManager()));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.inventory.readNbtList(nbt.getList("Inventory", NbtElement.COMPOUND_TYPE), this.getRegistryManager());
        this.getDataTracker().set(STONE, inventory.count(Items.COBBLESTONE));
        this.getDataTracker().set(WOOD, inventory.count(Items.OAK_LOG));
    }
}
