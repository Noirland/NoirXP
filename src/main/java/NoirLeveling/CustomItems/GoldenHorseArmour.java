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

public class GoldenHorseArmour implements ICustomItem {
    private static ShapedRecipe recipe;
    public GoldenHorseArmour() {
        ItemStack item = new ItemStack(Material.GOLDEN_HORSE_ARMOR, 1);
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = new ArrayList<>();
        loreList.add(PlayerClassConverter.PlayerClassToCapitalString(PlayerClass.FARMING));
        meta.setLore(loreList);
        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Main.plugin, "goldenhorsearmour");
        recipe = new ShapedRecipe(key, item);
        recipe.shape("ggg", "gSg");
        recipe.setIngredient('g', Material.GOLD_INGOT);
        recipe.setIngredient('S', Material.SADDLE);
    }

    @Override
    public ShapedRecipe getRecipe() {
        return recipe;
    }
}
