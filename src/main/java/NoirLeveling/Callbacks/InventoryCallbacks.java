package NoirLeveling.Callbacks;

import NoirLeveling.Helpers.LoreHelper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryCallbacks {
    public static void addTagsToPlayerInventory(Player player) {
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length; i++) {
            LoreHelper.addLoreToItem(contents[i]);
        }
    }
}
