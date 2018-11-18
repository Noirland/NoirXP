package nz.co.noirland.noirxp.customitems;

import nz.co.noirland.noirxp.constants.PlayerClass;
import nz.co.noirland.noirxp.helpers.PlayerClassConverter;
import nz.co.noirland.noirxp.NoirXP;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ChainChest implements ICustomItem {
    private static ShapedRecipe recipe;
    public ChainChest() {
        ItemStack item = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1);
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = new ArrayList<>();
        loreList.add(PlayerClassConverter.playerClassToCapitalString(PlayerClass.GENERAL));
        loreList.add("408/408");

        meta.setLore(loreList);
        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(NoirXP.plugin, "chainchest");
        recipe = new ShapedRecipe(key, item);
        recipe.shape("#G#", "###", "###");
        recipe.setIngredient('#', Material.EMERALD);
        recipe.setIngredient('G', Material.GOLDEN_CHESTPLATE);
    }

    @Override
    public ShapedRecipe getRecipe() {
        return recipe;
    }
}
