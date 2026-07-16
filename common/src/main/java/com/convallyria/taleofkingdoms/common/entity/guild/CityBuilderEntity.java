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
import com.convallyria.taleofkingdoms.common.world.guild.PlayerTitle;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CityBuilderEntity extends TOKEntity implements InventoryCarrier {

    private static final EntityDataAccessor<Boolean> MOVING_TO_LOCATION;
    private static final EntityDataAccessor<Integer> STONE;
    private static final EntityDataAccessor<Integer> WOOD;

    static {
        MOVING_TO_LOCATION = SynchedEntityData.defineId(CityBuilderEntity.class, EntityDataSerializers.BOOLEAN);
        STONE = SynchedEntityData.defineId(CityBuilderEntity.class, EntityDataSerializers.INT);
        WOOD = SynchedEntityData.defineId(CityBuilderEntity.class, EntityDataSerializers.INT);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(MOVING_TO_LOCATION, false);
        builder.define(STONE, 0);
        builder.define(WOOD, 0);
    }

    private final FollowPlayerGoal followPlayerGoal = new FollowPlayerGoal(this, 0.75F, 5, 50);
    private WalkToTargetGoal currentBlockTarget;

    private final SimpleContainer inventory = new SimpleContainer(10);

    public CityBuilderEntity(EntityType<? extends PathfinderMob> entityType, Level world) {
        super(entityType, world);
        inventory.addListener((changed) -> {
            this.getEntityData().set(STONE, changed.countItem(Items.COBBLESTONE));
            this.getEntityData().set(WOOD, changed.countItem(Items.OAK_LOG));
        });
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 10.0F, 100F));
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (hand == InteractionHand.OFF_HAND) return InteractionResult.FAIL;
        final boolean client = player.level().isClientSide();
        TaleOfKingdoms.getAPI().getConquestInstanceStorage().mostRecentInstance().ifPresent(instance -> {
            final GuildPlayer guildPlayer = instance.getPlayer(player);
            final PlayerKingdom kingdom = guildPlayer.getKingdom();
            if (kingdom != null) {
                if (client) return;
                openScreen(player, kingdom, instance);
                return;
            }

            if (this.getEntityData().get(MOVING_TO_LOCATION)) {
                BlockPos current = this.blockPosition();
                int distance = (int) instance.getCentre().distanceTo(new Vec3(current.getX(), current.getY(), current.getZ()));
                if (distance < 500) {
                    if (!client) Translations.CITYBUILDER_DISTANCE.send(player, guildPlayer.getTitle().getAddress(), distance, 500);
                    return;
                }

                if (!client) {
                    openScreen(player, null, instance);
                }
                return;
            }

            if (guildPlayer.getWorthiness() >= 1500) {
                if (client) Translations.CITYBUILDER_BUILD.send(player, guildPlayer.getTitle().getTranslation());
                this.followPlayer();
            } else {
                if (client) Translations.CITYBUILDER_MESSAGE.send(player, guildPlayer.getTitle().getTranslation());
            }
        });
        return InteractionResult.PASS;
    }

    public void give64wood(Player player) {
        final int playerWoodCount = InventoryUtils.count(player.getInventory(), ItemTags.LOGS);
        TaleOfKingdoms.getAPI().executeOnServerEnvironment((server) -> {
            final ItemStack stack = InventoryUtils.getStack(player.getInventory(), ItemTags.LOGS, 64);
            final ServerPlayer serverPlayer = server.getPlayerList().getPlayer(player.getUUID());
            final CityBuilderEntity serverCityBuilder = (CityBuilderEntity) serverPlayer.level().getEntity(this.getId());
            if (stack != null && playerWoodCount >= 64 && getWood() <= (320 - 64) && serverCityBuilder.getInventory().canAddItem(stack)) {
                int slot = serverPlayer.getInventory().findSlotMatchingItem(stack);
                serverPlayer.getInventory().removeItemNoUpdate(slot);
                player.getInventory().removeItemNoUpdate(slot);
                serverCityBuilder.getInventory().addItem(new ItemStack(Items.OAK_LOG, 64));
            }
        });
    }

    public void give64stone(Player player) {
        final int playerCobblestoneCount = player.getInventory().countItem(Items.COBBLESTONE);
        TaleOfKingdoms.getAPI().executeOnServerEnvironment((server) -> {
            final ItemStack stack = new ItemStack(Items.COBBLESTONE, 64);
            final ServerPlayer serverPlayer = server.getPlayerList().getPlayer(player.getUUID());
            final CityBuilderEntity serverCityBuilder = (CityBuilderEntity) serverPlayer.level().getEntity(this.getId());
            if (playerCobblestoneCount >= 64 && getStone() <= (320 - 64) && serverCityBuilder.getInventory().canAddItem(stack)) {
                int slot = serverPlayer.getInventory().findSlotMatchingItem(stack);
                serverPlayer.getInventory().removeItemNoUpdate(slot);
                player.getInventory().removeItemNoUpdate(slot);
                serverCityBuilder.getInventory().addItem(stack);
            }
        });
    }

    public void fixKingdom(Player player, PlayerKingdom kingdom) {
        if (this.getStone() != 320 || this.getWood() != 320)
            return;

        TaleOfKingdoms.getAPI().executeOnServerEnvironment(server -> {
            final ServerPlayer serverPlayer = server.getPlayerList().getPlayer(player.getUUID());
            final CityBuilderEntity serverCityBuilder = (CityBuilderEntity) serverPlayer.level().getEntity(this.getId());
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
            serverCityBuilder.getInventory().clearContent();
        });
    }

    public CompletableFuture<Void> build(Player player, BuildCosts build, PlayerKingdom kingdom) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        TaleOfKingdoms.getAPI().executeOnServerEnvironment(server -> {
            final ServerPlayer serverPlayer = server.getPlayerList().getPlayer(player.getUUID());
            final CityBuilderEntity serverCityBuilder = (CityBuilderEntity) serverPlayer.level().getEntity(this.getId());
            kingdom.addBuilt(build);
            TaleOfKingdoms.LOGGER.info("Placing {}...", build);
            TaleOfKingdoms.getAPI().getSchematicHandler().pasteSchematic(build.getSchematic(), serverPlayer, kingdom.getPOIPos(build.getKingdomPOI()), build.getSchematicRotation());

            serverCityBuilder.getInventory().removeItemType(Items.OAK_LOG, build.getWood());
            serverCityBuilder.getInventory().removeItemType(Items.COBBLESTONE, build.getStone());
            future.complete(null);
        });
        return future;
    }

    public void followPlayer() {
        this.getEntityData().set(MOVING_TO_LOCATION, true);
        this.goalSelector.addGoal(2, followPlayerGoal);
    }

    public void stopFollowingPlayer() {
        this.getEntityData().set(MOVING_TO_LOCATION, false);
        this.goalSelector.removeGoal(followPlayerGoal);
    }

    public void setTarget(BlockPos pos) {
        if (this.currentBlockTarget != null) this.goalSelector.removeGoal(this.currentBlockTarget);
        this.goalSelector.addGoal(3, this.currentBlockTarget = new WalkToTargetGoal(this, 0.75F, pos));
    }

    public void stopTarget() {
        this.goalSelector.removeGoal(this.currentBlockTarget);
    }

    private void openScreen(Player player, @Nullable PlayerKingdom kingdom, ConquestInstance instance) {
        final PacketHandler<OpenScreenPacket> open = TaleOfKingdoms.getAPI().getServerPacket(Packets.OPEN_CLIENT_SCREEN);
        if (kingdom == null) {
            open.sendPacket(player, new OpenScreenPacket(OpenScreenPacket.ScreenTypes.CITY_BUILDER_BEGIN, this.getId()));
            return;
        }

        open.sendPacket(player, new OpenScreenPacket(OpenScreenPacket.ScreenTypes.CITY_BUILDER_TIER, this.getId()));
    }

    public boolean canAffordBuild(@Nullable PlayerKingdom kingdom, BuildCosts build) {
       return build.getTier().isLowerThanOrEqual(kingdom == null ? null : kingdom.getTier()) && this.entityData.get(STONE) >= build.getStone() && this.entityData.get(WOOD) >= build.getWood();
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
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        return false;
    }

    @Override
    public SimpleContainer getInventory() {
        return inventory;
    }

    public int getStone() {
        return this.entityData.get(STONE);
    }

    public int getWood() {
        return this.entityData.get(WOOD);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.put("Inventory", this.inventory.createTag(this.registryAccess()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.inventory.fromTag(nbt.getList("Inventory", Tag.TAG_COMPOUND), this.registryAccess());
        this.getEntityData().set(STONE, inventory.countItem(Items.COBBLESTONE));
        this.getEntityData().set(WOOD, inventory.countItem(Items.OAK_LOG));
    }
}
