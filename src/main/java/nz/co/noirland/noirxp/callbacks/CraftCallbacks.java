package nz.co.noirland.noirxp.callbacks;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CraftCallbacks {

    /**
     * Checks if two item stacks are equal.
     * @param stack1 The first item stack.
     * @param stack2 The second item stack.
     * @return True if the lore, display name and material are the same, false otherwise
     */
    public static boolean isItemStackEqual(ItemStack stack1, ItemStack stack2) {
        ItemMeta stack1meta = stack1.getItemMeta();
        ItemMeta stack2meta = stack2.getItemMeta();
        if (stack1.getType() != stack2.getType()) {
            return false;
        }
        if (stack1meta.hasDisplayName() != stack2meta.hasDisplayName()) {
            return false;
        }
        if (stack1meta.hasLore() != stack2meta.hasLore()) {
            return false;
        }
        if (stack1meta.hasDisplayName()) {
            if (!stack1meta.getDisplayName().equals(stack2meta.getDisplayName())) {
                return false;
            }
        }
        if (stack1meta.hasLore()) {
            if (!stack1meta.getLore().equals(stack2meta.getLore())) {
                return false;
            }
        }
        return true;

    }

}
