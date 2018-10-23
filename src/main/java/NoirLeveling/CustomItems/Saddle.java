package NoirLeveling.CustomItems;

import NoirLeveling.Constants.PlayerClass;
import NoirLeveling.Helpers.PlayerClassConverter;
import NoirLeveling.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Saddle implements ICustomItem {
    private static ShapedRecipe recipe;
    public Saddle() {
        ItemStack item = new ItemStack(Material.SADDLE, 1);
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = new ArrayList<>();
        loreList.add(PlayerClassConverter.PlayerClassToCapitalString(PlayerClass.FARMING));
        meta.setLore(loreList);
        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Main.plugin, "leather");
        recipe = new ShapedRecipe(key, item);
        recipe.shape("###", "SAS");
        recipe.setIngredient('#', Material.LEATHER);
        recipe.setIngredient('S', Material.STRING);
        recipe.setIngredient('A', Material.AIR);
    }

    @Override
    public ShapedRecipe getRecipe() {
        return recipe;
    }
}
