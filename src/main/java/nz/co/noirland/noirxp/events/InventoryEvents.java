package nz.co.noirland.noirxp.events;

import nz.co.noirland.noirxp.helpers.LoreHelper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

public class InventoryEvents implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        ItemStack itemStack = event.getItem();
        LoreHelper.addLoreToItem(itemStack);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryPickupItem(InventoryPickupItemEvent event) {
        ItemStack itemStack = event.getItem().getItemStack();
        LoreHelper.addLoreToItem(itemStack);
    }
}
