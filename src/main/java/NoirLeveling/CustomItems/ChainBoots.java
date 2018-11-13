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

public class ChainBoots implements ICustomItem {
    private static ShapedRecipe recipe;
    public ChainBoots() {
        ItemStack item = new ItemStack(Material.CHAINMAIL_BOOTS, 1);
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = new ArrayList<>();
        loreList.add(PlayerClassConverter.playerClassToCapitalString(PlayerClass.GENERAL));
        loreList.add("331/331");

        meta.setLore(loreList);
        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Main.plugin, "chainboots");
        recipe = new ShapedRecipe(key, item);
        recipe.shape("#G#", "#A#");
        recipe.setIngredient('#', Material.EMERALD);
        recipe.setIngredient('G', Material.GOLDEN_BOOTS);
        recipe.setIngredient('A', Material.AIR);
    }

    @Override
    public ShapedRecipe getRecipe() {
        return recipe;
    }
}
