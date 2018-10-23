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

public class ChainHelmet implements ICustomItem {
    private static ShapedRecipe recipe;
    public ChainHelmet() {
        ItemStack item = new ItemStack(Material.CHAINMAIL_HELMET, 1);
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = new ArrayList<>();
        loreList.add(PlayerClassConverter.PlayerClassToCapitalString(PlayerClass.GENERAL));
        loreList.add("280/280");

        meta.setLore(loreList);
        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Main.plugin, "chainhelmet");
        recipe = new ShapedRecipe(key, item);
        recipe.shape("###", "#G#");
        recipe.setIngredient('#', Material.EMERALD);
        recipe.setIngredient('G', Material.GOLDEN_HELMET);
    }

    @Override
    public ShapedRecipe getRecipe() {
        return recipe;
    }
}
