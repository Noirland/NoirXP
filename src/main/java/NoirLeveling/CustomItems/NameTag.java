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

public class NameTag implements ICustomItem {
    private static ShapedRecipe recipe;
    public NameTag() {
        ItemStack item = new ItemStack(Material.NAME_TAG, 1);
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = new ArrayList<>();
        loreList.add(PlayerClassConverter.PlayerClassToCapitalString(PlayerClass.FARMING));
        meta.setLore(loreList);
        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Main.plugin, "nametag");
        recipe = new ShapedRecipe(key, item);
        recipe.shape("#", "L", "P");
        recipe.setIngredient('#', Material.STRING);
        recipe.setIngredient('L', Material.LEATHER);
        recipe.setIngredient('P', Material.PAPER);
    }

    @Override
    public ShapedRecipe getRecipe() {
        return recipe;
    }
}
