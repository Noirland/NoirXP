package NoirLeveling.Events;

import NoirLeveling.Callbacks.PlayerCallbacks;
import NoirLeveling.Classes.NoirPlayer;
import NoirLeveling.Constants.PlayerClass;
import NoirLeveling.Main;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.InventoryView;

import java.util.Arrays;
import java.util.Collections;

public class EnchantEvents implements Listener {

    @EventHandler
    public void onPrepareEnchant(PrepareItemEnchantEvent event) {
        NoirPlayer noirPlayer = Main.players.get(event.getEnchanter().getUniqueId().toString());
        if (noirPlayer.alchemy.getLevel() < 20) {
            event.setCancelled(true);
            return;
        }
        if (noirPlayer.alchemy.getLevel() < 30) {
            if (event.getOffers()[0].getEnchantmentLevel() > 1) {
                event.getOffers()[0] = null;
            }
            if (event.getOffers()[1].getEnchantmentLevel() > 1) {
                event.getOffers()[1] = null;
            }
            if (event.getOffers()[2].getEnchantmentLevel() > 1) {
                event.getOffers()[2] = null;
            }
            return;
        }
        if (noirPlayer.alchemy.getLevel() < 40) {
            if (event.getOffers()[0].getEnchantmentLevel() > 2) {
                event.getOffers()[0] = null;
            }
            if (event.getOffers()[1].getEnchantmentLevel() > 2) {
                event.getOffers()[1] = null;
            }
            if (event.getOffers()[2].getEnchantmentLevel() > 2) {
                event.getOffers()[2] = null;
            }
            return;
        }

    }

    @EventHandler
    public void onItemEnchant(EnchantItemEvent event) {
        int highestLevel = Collections.max(event.getEnchantsToAdd().values());
        NoirPlayer noirPlayer = Main.players.get(event.getEnchanter().getUniqueId().toString());
        if (highestLevel == 1) {
            PlayerCallbacks.xpGained(noirPlayer.getUniqueId(), PlayerClass.ALCHEMY, 50);
        }
        else if (highestLevel == 2) {
            PlayerCallbacks.xpGained(noirPlayer.getUniqueId(), PlayerClass.ALCHEMY, 70);
        }
        else if (highestLevel == 3) {
            PlayerCallbacks.xpGained(noirPlayer.getUniqueId(), PlayerClass.ALCHEMY, 85);
        }


    }


}
