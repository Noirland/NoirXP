package nz.co.noirland.noirxp.events;

import nz.co.noirland.noirxp.helpers.LoreHelper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PickupEvents implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onItemPickup(EntityPickupItemEvent event) {
        ItemStack stack = event.getItem().getItemStack();
        LoreHelper.addLoreToItem(stack);
    }
}
