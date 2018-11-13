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

public class ChainLeggings implements ICustomItem {
    private static ShapedRecipe recipe;
    public ChainLeggings() {
        ItemStack item = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = new ArrayList<>();
        loreList.add(PlayerClassConverter.playerClassToCapitalString(PlayerClass.GENERAL));
        loreList.add("382/382");

        meta.setLore(loreList);
        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Main.plugin, "chainleggings");
        recipe = new ShapedRecipe(key, item);
        recipe.shape("###", "#G#", "#A#");
        recipe.setIngredient('#', Material.EMERALD);
        recipe.setIngredient('G', Material.GOLDEN_LEGGINGS);
        recipe.setIngredient('A', Material.AIR);
    }

    @Override
    public ShapedRecipe getRecipe() {
        return recipe;
    }
}
