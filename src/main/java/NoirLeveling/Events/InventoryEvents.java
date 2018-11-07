package NoirLeveling.Events;

import NoirLeveling.Classes.LevelRequirementStats;
import NoirLeveling.Classes.NoirPlayer;
import NoirLeveling.Constants.POTION_TYPES;
import NoirLeveling.Constants.PlayerPermissionTypes;
import NoirLeveling.Helpers.LoreHelper;
import NoirLeveling.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import javax.lang.model.element.ElementVisitor;

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
