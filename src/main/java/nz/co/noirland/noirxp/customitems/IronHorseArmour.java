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

public class IronHorseArmour implements ICustomItem {
    private static ShapedRecipe recipe;
    public IronHorseArmour() {
        ItemStack item = new ItemStack(Material.IRON_HORSE_ARMOR, 1);
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = new ArrayList<>();
        loreList.add(PlayerClassConverter.playerClassToCapitalString(PlayerClass.FARMING));
        meta.setLore(loreList);
        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(NoirXP.inst(), "ironhorsearmour");
        recipe = new ShapedRecipe(key, item);
        recipe.shape("iii", "iSi");
        recipe.setIngredient('i', Material.IRON_INGOT);
        recipe.setIngredient('S', Material.SADDLE);
    }

    @Override
    public ShapedRecipe getRecipe() {
        return recipe;
    }
}
