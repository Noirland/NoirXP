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

public class SpaceBoots implements ICustomItem {
    private static ShapedRecipe recipe;
    public SpaceBoots() {
        ItemStack item = new ItemStack(Material.IRON_BOOTS, 1);
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = new ArrayList<>();
        loreList.add(PlayerClassConverter.PlayerClassToCapitalString(PlayerClass.GENERAL));
        meta.setDisplayName(ChatColor.DARK_BLUE + "Space Boots");
        loreList.add("For spacious encounters");
        meta.setLore(loreList);
        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Main.plugin, "spaceboots");
        recipe = new ShapedRecipe(key, item);
        recipe.shape("#A#", "PAP", "IAI");
        recipe.setIngredient('#', Material.PHANTOM_MEMBRANE);
        recipe.setIngredient('P', Material.PRISMARINE_CRYSTALS);
        recipe.setIngredient('I', Material.IRON_BOOTS);
        recipe.setIngredient('A', Material.AIR);
    }

    @Override
    public ShapedRecipe getRecipe() {
        return recipe;
    }
}
