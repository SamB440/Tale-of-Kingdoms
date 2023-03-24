package com.convallyria.taleofkingdoms.common.entity.kingdom;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.client.gui.entity.kingdom.ForemanScreen;
import com.convallyria.taleofkingdoms.client.translation.Translations;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.kingdom.poi.KingdomPOI;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import com.convallyria.taleofkingdoms.common.utils.InventoryUtils;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class ForemanEntity extends TOKEntity implements InventoryOwner {

    private static final TrackedData<Integer> STONE;
    private static final TrackedData<Integer> WOOD;

    static {
        STONE = DataTracker.registerData(ForemanEntity.class, TrackedDataHandlerRegistry.INTEGER);
        WOOD = DataTracker.registerData(ForemanEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(STONE, 0);
        this.dataTracker.startTracking(WOOD, 0);
    }

    private final SimpleInventory inventory = new SimpleInventory(10);

    public ForemanEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
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
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        if (api == null) return ActionResult.FAIL;
        if (api.getConquestInstanceStorage().mostRecentInstance().isEmpty()) return ActionResult.FAIL;

        ConquestInstance instance = api.getConquestInstanceStorage().mostRecentInstance().get();
        if (hand == Hand.OFF_HAND || !player.world.isClient()) return ActionResult.FAIL;
        this.openScreen(player, instance);
        return ActionResult.PASS;
    }

    @Environment(EnvType.CLIENT)
    public void openScreen(PlayerEntity player, ConquestInstance instance) {
        Translations.FOREMAN_NEED_RESOURCES.send(player);
        MinecraftClient.getInstance().setScreen(new ForemanScreen(player, this, instance));
    }

    public void buyWorker(PlayerEntity player, ConquestInstance instance) {
        final GuildPlayer guildPlayer = instance.getPlayer(player);
        final int coins = guildPlayer.getCoins();
        if (coins < 1500) return;

        final PlayerKingdom kingdom = guildPlayer.getKingdom();
        if (kingdom == null) return;

        guildPlayer.setCoins(coins - 1500);
        EntityType<? extends WorkerEntity> type = this instanceof QuarryForemanEntity ? EntityTypes.QUARRY_WORKER : EntityTypes.LUMBER_WORKER;
        BlockPos poi = this instanceof QuarryForemanEntity ? kingdom.getPOIPos(KingdomPOI.QUARRY_WORKER_SPAWN) : kingdom.getPOIPos(KingdomPOI.LUMBER_WORKER_SPAWN);
        TaleOfKingdoms.getAPI().executeOnServerEnvironment((server) -> {
            ServerPlayerEntity serverPlayerEntity = player instanceof ServerPlayerEntity ? (ServerPlayerEntity) player
                    : server.getPlayerManager().getPlayer(player.getUuid());
            if (serverPlayerEntity == null) return;
            EntityUtils.spawnEntity(type, serverPlayerEntity, poi);
            Translations.FOREMAN_BUY_WORKER.send(player);
        });
    }

    public void collect64(PlayerEntity player, Item item) {
        TaleOfKingdoms.getAPI().executeOnServerEnvironment(server -> {
            final ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(player.getUuid());
            final ForemanEntity serverForeman = (ForemanEntity) serverPlayer.getWorld().getEntityById(this.getId());
            final int slotWithStack = InventoryUtils.getSlotWithStack(serverForeman.getInventory(), new ItemStack(item, 64));
            if (slotWithStack == -1) {
                Translations.FOREMAN_COLLECT_RESOURCES_EMPTY.send(player);
                return;
            }

            final ItemStack itemStack = serverForeman.getInventory().removeStack(slotWithStack);
            serverPlayer.getInventory().insertStack(itemStack);
            player.getInventory().insertStack(itemStack);
        });
    }

    @Override
    public boolean isStationary() {
        return true;
    }

    @Override
    public boolean isPushable() {
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
        nbt.put("Inventory", this.inventory.toNbtList());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.inventory.readNbtList(nbt.getList("Inventory", 10));
        this.getDataTracker().set(STONE, inventory.count(Items.COBBLESTONE));
        this.getDataTracker().set(WOOD, inventory.count(Items.OAK_LOG));
    }
}
