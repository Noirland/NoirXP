package nz.co.noirland.noirxp.customitems;

import nz.co.noirland.noirxp.constants.PlayerClass;
import nz.co.noirland.noirxp.helpers.PlayerClassConverter;
import nz.co.noirland.noirxp.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SpacePants implements ICustomItem {
    private static ShapedRecipe recipe;
    public SpacePants() {
        ItemStack item = new ItemStack(Material.IRON_LEGGINGS, 1);
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = new ArrayList<>();
        loreList.add(PlayerClassConverter.playerClassToCapitalString(PlayerClass.GENERAL));
        loreList.add("and space shorts");
        meta.setDisplayName(ChatColor.DARK_BLUE + "SPACE PANTS");
        meta.setLore(loreList);
        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Main.plugin, "spacepants");
        recipe = new ShapedRecipe(key, item);
        recipe.shape("#C#", "#N#", "#A#");
        recipe.setIngredient('#', Material.PHANTOM_MEMBRANE);
        recipe.setIngredient('C', Material.CONDUIT);
        recipe.setIngredient('N', Material.NETHER_STAR);
        recipe.setIngredient('A', Material.AIR);
    }

    @Override
    public ShapedRecipe getRecipe() {
        return recipe;
    }
}
