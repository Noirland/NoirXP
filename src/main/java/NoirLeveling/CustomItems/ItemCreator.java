package NoirLeveling.CustomItems;

import NoirLeveling.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class ItemCreator {

    public static void CreateCustomItems(Server server, List<ICustomItem> customItemList) {
        for (ICustomItem customItem : customItemList) {
            server.addRecipe(customItem.getRecipe());
        }
    }
}
