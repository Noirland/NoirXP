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

public class SpaceVest implements ICustomItem {
    private static ShapedRecipe recipe;
    public SpaceVest() {
        ItemStack item = new ItemStack(Material.IRON_CHESTPLATE, 1);
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = new ArrayList<>();
        loreList.add(PlayerClassConverter.playerClassToCapitalString(PlayerClass.GENERAL));
        loreList.add("For spacious encounters");
        meta.setDisplayName(ChatColor.DARK_BLUE + "Space Vest");
        meta.setLore(loreList);
        item.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(NoirXP.inst(), "spacevest");
        recipe = new ShapedRecipe(key, item);
        recipe.shape("#A#", "PCP", "PNP");
        recipe.setIngredient('#', Material.DRIED_KELP_BLOCK);
        recipe.setIngredient('N', Material.NETHER_STAR);
        recipe.setIngredient('P', Material.PHANTOM_MEMBRANE);
        recipe.setIngredient('C', Material.CONDUIT);
        recipe.setIngredient('A', Material.AIR);
    }

    @Override
    public ShapedRecipe getRecipe() {
        return recipe;
    }
}
