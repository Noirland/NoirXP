package nz.co.noirland.noirxp.customitems;

import nz.co.noirland.noirxp.constants.PlayerClass;
import nz.co.noirland.noirxp.helpers.PlayerClassConverter;
import nz.co.noirland.noirxp.NoirXP;
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
        loreList.add(PlayerClassConverter.playerClassToCapitalString(PlayerClass.GENERAL));
        meta.setDisplayName(ChatColor.DARK_BLUE + "Space Boots");
        loreList.add("For spacious encounters");
        meta.setLore(loreList);
        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(NoirXP.inst(), "spaceboots");
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
