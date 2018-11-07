package NoirLeveling.Events;

import NoirLeveling.Callbacks.PlayerCallbacks;
import NoirLeveling.Constants.PlayerClass;
import NoirLeveling.Database.Database;
import NoirLeveling.Helpers.LoreHelper;
import NoirLeveling.Helpers.PlayerClassConverter;
import NoirLeveling.Main;
import NoirLeveling.SQLProcedures.SQLProcedures;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PickupEvents implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onItemPickup(EntityPickupItemEvent event) {

        ItemStack stack = event.getItem().getItemStack();
        LoreHelper.addLoreToItem(stack);


    }
}
