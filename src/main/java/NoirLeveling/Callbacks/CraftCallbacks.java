package NoirLeveling.Callbacks;

import NoirLeveling.Database.Database;
import NoirLeveling.SQLProcedures.SQLProcedures;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class CraftCallbacks {

    public static boolean isItemStackEqual(ItemStack stack1, ItemStack stack2) {
        ItemMeta stack1meta = stack1.getItemMeta();
        ItemMeta stack2meta = stack2.getItemMeta();

        if (stack1meta.hasDisplayName() && stack2meta.hasDisplayName()) {
            if (stack1meta.getDisplayName().equals(stack2meta.getDisplayName())) {
                if (stack1meta.getLore().equals(stack2meta.getLore())) {
                    return true;
                }
            }
        }
        return false;

    }

}
