package NoirLeveling.Events;

import NoirLeveling.Classes.NoirPlayer;
import NoirLeveling.Main;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class DamageEvents implements Listener {

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        ItemStack itemStack = event.getItem();
        int damage = event.getDamage();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.hasLore()) {
            List<String> loreList = itemMeta.getLore();
            if (loreList.size() > 1) {
                if (loreList.get(loreList.size() - 1).contains("/")) {
                    String durabilityEntry = loreList.get(loreList.size() - 1);
                    int currentDurability = Integer.parseInt(durabilityEntry.substring(0, durabilityEntry.indexOf("/")));

                    if (currentDurability - damage <= 0) {
                        event.setDamage(itemStack.getType().getMaxDurability());
                        return;
                    }
                    else {
                        int newDurability = currentDurability - damage;
                        String newLore = durabilityEntry.replace(currentDurability + "/", newDurability + "/");
                        loreList.set(loreList.size() - 1, newLore);
                        itemMeta.setLore(loreList);
                        itemStack.setItemMeta(itemMeta);
                    }

                    event.setDamage(0);
                }

            }
        }
    }
}
