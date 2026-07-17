package com.convallyria.taleofkingdoms.common.entity.kingdom;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.advancement.TOKAdvancements;
import com.convallyria.taleofkingdoms.common.entity.EntityTypes;
import com.convallyria.taleofkingdoms.common.entity.TOKEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.workers.QuarryForemanEntity;
import com.convallyria.taleofkingdoms.common.entity.kingdom.workers.WorkerEntity;
import com.convallyria.taleofkingdoms.common.kingdom.PlayerKingdom;
import com.convallyria.taleofkingdoms.common.kingdom.poi.KingdomPOI;
import com.convallyria.taleofkingdoms.common.packet.Packets;
import com.convallyria.taleofkingdoms.common.packet.s2c.OpenScreenPacket;
import com.convallyria.taleofkingdoms.common.translation.Translations;
import com.convallyria.taleofkingdoms.common.utils.EntityUtils;
import com.convallyria.taleofkingdoms.common.utils.InventoryUtils;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import com.convallyria.taleofkingdoms.common.world.guild.GuildPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public abstract class ForemanEntity extends TOKEntity implements InventoryCarrier {

    private static final EntityDataAccessor<Integer> STONE;
    private static final EntityDataAccessor<Integer> WOOD;

    static {
        STONE = SynchedEntityData.defineId(ForemanEntity.class, EntityDataSerializers.INT);
        WOOD = SynchedEntityData.defineId(ForemanEntity.class, EntityDataSerializers.INT);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(STONE, 0);
        builder.define(WOOD, 0);
    }

    private final SimpleContainer inventory = new SimpleContainer(20);

    public ForemanEntity(EntityType<? extends PathfinderMob> entityType, Level world) {
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
        final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
        if (api == null) return InteractionResult.FAIL;
        if (api.getConquestInstanceStorage().mostRecentInstance().isEmpty()) return InteractionResult.FAIL;
        if (hand == InteractionHand.OFF_HAND || player.level().isClientSide()) return InteractionResult.FAIL;
        TaleOfKingdoms.getAPI().getServerPacket(Packets.OPEN_CLIENT_SCREEN).sendPacket(player, new OpenScreenPacket(OpenScreenPacket.ScreenTypes.FOREMAN, this.getId()));
        return InteractionResult.PASS;
    }

    public void buyWorker(Player player, ConquestInstance instance) {
        final GuildPlayer guildPlayer = instance.getPlayer(player);
        final int coins = guildPlayer.getCoins();
        if (coins < 1500) return;

        final PlayerKingdom kingdom = guildPlayer.getKingdom();
        if (kingdom == null) return;

        guildPlayer.setCoins(coins - 1500);
        EntityType<? extends WorkerEntity> type = this instanceof QuarryForemanEntity ? EntityTypes.QUARRY_WORKER.get() : EntityTypes.LUMBER_WORKER.get();
        BlockPos poi = this instanceof QuarryForemanEntity ? kingdom.getPOIPos(KingdomPOI.QUARRY_WORKER_SPAWN) : kingdom.getPOIPos(KingdomPOI.LUMBER_WORKER_SPAWN);
        TaleOfKingdoms.getAPI().executeOnServerEnvironment((server) -> {
            ServerPlayer serverPlayerEntity = player instanceof ServerPlayer ? (ServerPlayer) player
                    : server.getPlayerList().getPlayer(player.getUUID());
            if (serverPlayerEntity == null) return;
            EntityUtils.spawnEntity(type, serverPlayerEntity, poi);
            TOKAdvancements.award(serverPlayerEntity, TOKAdvancements.HIRE_WORKER);
            Translations.FOREMAN_BUY_WORKER.send(player);
        });
    }

    public void collect64(Player player, Item item) {
        TaleOfKingdoms.getAPI().executeOnServerEnvironment(server -> {
            final ServerPlayer serverPlayer = server.getPlayerList().getPlayer(player.getUUID());
            final ForemanEntity serverForeman = (ForemanEntity) serverPlayer.level().getEntity(this.getId());
            final int slotWithStack = InventoryUtils.getSlotWithStack(serverForeman.getInventory(), new ItemStack(item, 64));
            if (slotWithStack == -1) {
                Translations.FOREMAN_COLLECT_RESOURCES_EMPTY.send(player);
                return;
            }

            final ItemStack itemStack = serverForeman.getInventory().removeItemNoUpdate(slotWithStack);
            serverPlayer.getInventory().add(itemStack);
            player.getInventory().add(itemStack);
        });
    }

    @Override
    public boolean isStationary() {
        return true;
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
