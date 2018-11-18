package nz.co.noirland.noirxp.customitems;

import nz.co.noirland.noirxp.constants.PlayerClass;
import nz.co.noirland.noirxp.helpers.PlayerClassConverter;
import nz.co.noirland.noirxp.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DiamondHorseArmour implements ICustomItem {
    private static ShapedRecipe recipe;
    public DiamondHorseArmour() {
        ItemStack item = new ItemStack(Material.DIAMOND_HORSE_ARMOR, 1);
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = new ArrayList<>();
        loreList.add(PlayerClassConverter.playerClassToCapitalString(PlayerClass.FARMING));
        meta.setLore(loreList);
        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(Main.plugin, "diamondhorsearmour");
        recipe = new ShapedRecipe(key, item);
        recipe.shape("###", "#S#");
        recipe.setIngredient('#', Material.DIAMOND);
        recipe.setIngredient('S', Material.SADDLE);
    }

    @Override
    public ShapedRecipe getRecipe() {
        return recipe;
    }
}
