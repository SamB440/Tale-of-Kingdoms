package com.convallyria.taleofkingdoms.common.shop;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.fabricmc.api.EnvType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import java.util.Optional;

public class SellScreenHandler extends AbstractContainerMenu {

    private final Container inventory;

    //This constructor gets called on the client when the server wants it to open the screenHandler,
    //The client will call the other constructor with an empty Inventory and the screenHandler will automatically
    //sync this empty inventory with the inventory on the server.
    public SellScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(1));
    }

    //This constructor gets called from the BlockEntity on the server without calling the other constructor first, the server knows the inventory of the container
    //and can therefore directly provide it as an argument. This inventory will then be synced to the client.
    public SellScreenHandler(int syncId, Inventory playerInventory, Container inventory) {
        super(TaleOfKingdoms.SELL_SCREEN_HANDLER, syncId);
        checkContainerSize(inventory, 1);
        this.inventory = inventory;
        //some inventories do custom logic when a player opens it.
        inventory.startOpen(playerInventory.player);

        //This will place the slot in the correct locations for a 3x3 Grid. The slots exist on both server and client!
        //This will not render the background of the slots however, this is the Screens job
        int m;
        int l;
        // Our inventory
        this.addSlot(new Slot(inventory, 0, 116, 35));
        //The player inventory
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        //The player Hotbar
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }

    /**
     * @param clickData 0 = left click, 1 = right, 2 = middle, 3-x = extra mouse buttons
     */
    @Override
    public void clicked(int slotIndex, int clickData, ClickType actionType, Player playerEntity) {
        if (slotIndex == 0) {
            ItemStack itemStack = playerEntity.containerMenu.getCarried();
            final TaleOfKingdomsAPI api = TaleOfKingdoms.getAPI();
            if (api == null) return;
            Optional<ConquestInstance> instance = api.getConquestInstanceStorage().mostRecentInstance();
            if (instance.isEmpty()) return;

            for (List<ShopItem> shopItems : ShopParser.SHOP_ITEMS.values()) {
                for (ShopItem shopItem : shopItems) {
                    if (itemStack.getItem() != shopItem.getItem()) continue;
                    if (shopItem.getSell() <= 0) continue;

                    int itemStackCount = itemStack.getCount();

                    if(clickData == 0) {
                        // Only set empty once we've found the item... and that the item is a valid sell item and that they left-clicked on the slot
                        playerEntity.containerMenu.setCarried(ItemStack.EMPTY);
                    } else {
                        // Else assume it is right click (will include middle and extra mouse button clicks)
                        // and only take 1
                        itemStack.setCount(itemStackCount - 1);
                        itemStackCount = 1;
                        playerEntity.containerMenu.setCarried(itemStack);
                    }

                    // Don't run on local server if we're in a client environment
                    // Otherwise, the coins will get added twice.
                    if (playerEntity instanceof ServerPlayer && TaleOfKingdoms.getAPI().getEnvironment() == EnvType.CLIENT) {
                        return;
                    }

                    // Issue #59
                    instance.get().addCoins(playerEntity.getUUID(), shopItem.getSell() * itemStackCount);
                    return;
                }
            }
            return;
        }
        super.clicked(slotIndex, clickData, actionType, playerEntity);
    }

    // Shift + Player Inv Slot
    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        return ItemStack.EMPTY;
    }
}
