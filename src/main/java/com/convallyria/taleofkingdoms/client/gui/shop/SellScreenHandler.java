package com.convallyria.taleofkingdoms.client.gui.shop;

import com.convallyria.taleofkingdoms.TaleOfKingdoms;
import com.convallyria.taleofkingdoms.TaleOfKingdomsAPI;
import com.convallyria.taleofkingdoms.common.shop.ShopItem;
import com.convallyria.taleofkingdoms.common.shop.ShopParser;
import com.convallyria.taleofkingdoms.common.world.ConquestInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

import java.util.List;
import java.util.Optional;

public class SellScreenHandler extends ScreenHandler {

    private final Inventory inventory;

    //This constructor gets called on the client when the server wants it to open the screenHandler,
    //The client will call the other constructor with an empty Inventory and the screenHandler will automatically
    //sync this empty inventory with the inventory on the server.
    public SellScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(1));
    }

    //This constructor gets called from the BlockEntity on the server without calling the other constructor first, the server knows the inventory of the container
    //and can therefore directly provide it as an argument. This inventory will then be synced to the client.
    public SellScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(TaleOfKingdoms.SELL_SCREEN_HANDLER, syncId);
        checkSize(inventory, 1);
        this.inventory = inventory;
        //some inventories do custom logic when a player opens it.
        inventory.onOpen(playerInventory.player);

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
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack onSlotClick(int i, int j, SlotActionType actionType, PlayerEntity playerEntity) {
        if (i == 0) {
            ItemStack itemStack = playerEntity.inventory.getCursorStack();
            Optional<TaleOfKingdomsAPI> api = TaleOfKingdoms.getAPI();
            if (!api.isPresent()) return itemStack;
            Optional<ConquestInstance> instance = api.get().getConquestInstanceStorage().mostRecentInstance();
            if (!instance.isPresent()) return itemStack;
            playerEntity.inventory.setCursorStack(ItemStack.EMPTY);

            for (List<ShopItem> shopItems : ShopParser.guiShopItems.values()) {
                for (ShopItem shopItem : shopItems) {
                    if (itemStack.getItem() == shopItem.getItem()) {
                        // Issue #59
                        instance.get().addCoins(playerEntity.getUuid(), shopItem.getSell() * itemStack.getCount());
                        return ItemStack.EMPTY;
                    }
                }
            }
            return itemStack;
        }
        return super.onSlotClick(i, j, actionType, playerEntity);
    }

    // Shift + Player Inv Slot
    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        return ItemStack.EMPTY;
    }
}
