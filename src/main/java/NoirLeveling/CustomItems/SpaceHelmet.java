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

public class SpaceHelmet implements ICustomItem {
    private static ShapedRecipe recipe;
    public SpaceHelmet() {
        ItemStack item = new ItemStack(Material.IRON_HELMET, 1);
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = new ArrayList<>();
        loreList.add(PlayerClassConverter.PlayerClassToCapitalString(PlayerClass.GENERAL));
        loreList.add("For spacious encounters");
        meta.setDisplayName(ChatColor.DARK_BLUE + "Space Helmet");
        meta.setLore(loreList);
        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Main.plugin, "spacehelmet");
        recipe = new ShapedRecipe(key, item);
        recipe.shape("#N#", "PCP", "PPP");
        recipe.setIngredient('#', Material.GHAST_TEAR);
        recipe.setIngredient('N', Material.NETHER_STAR);
        recipe.setIngredient('P', Material.PHANTOM_MEMBRANE);
        recipe.setIngredient('C', Material.CONDUIT);
    }

    @Override
    public ShapedRecipe getRecipe() {
        return recipe;
    }
}
