package NoirLeveling.CustomItems;

import NoirLeveling.Constants.PlayerClass;
import NoirLeveling.Helpers.PlayerClassConverter;
import NoirLeveling.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Lead implements ICustomItem {
    private static ShapedRecipe recipe;
    public Lead() {
        ItemStack item = new ItemStack(Material.LEAD, 1);
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = new ArrayList<String>();
        loreList.add(PlayerClassConverter.playerClassToCapitalString(PlayerClass.FARMING));

        meta.setLore(loreList);
        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Main.plugin, "lead");
        recipe = new ShapedRecipe(key, item);
        recipe.shape("#", "#", "S");
        recipe.setIngredient('#', Material.LEATHER);
        recipe.setIngredient('S', Material.STRING);

    }


    @Override
    public ShapedRecipe getRecipe() {
        return recipe;
    }
}
