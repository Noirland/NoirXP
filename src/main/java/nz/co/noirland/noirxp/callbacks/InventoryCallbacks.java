package nz.co.noirland.noirxp.callbacks;

import nz.co.noirland.noirxp.helpers.LoreHelper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryCallbacks {
    /**
     * Adds noir tags to all items in a players inventory.
     * @param player The player to add the items to.
     */
    public static void addTagsToPlayerInventory(Player player) {
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            LoreHelper.addLoreToItem(contents[i]);
        }
    }
}
