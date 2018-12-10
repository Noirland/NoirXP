package nz.co.noirland.noirxp.callbacks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CraftCallbacks {

    private static Map<Material, Material> furnaceSmeltMap = new HashMap<>();

    static {
        Bukkit.recipeIterator().forEachRemaining(r -> {
            if(!(r instanceof FurnaceRecipe)) return;
            FurnaceRecipe recipe = (FurnaceRecipe) r;
            furnaceSmeltMap.put(recipe.getInput().getType(), recipe.getResult().getType());
        });
    }

    /**
     * Checks if two item stacks are equal.
     * @param stack1 The first item stack.
     * @param stack2 The second item stack.
     * @return True if the lore, display name and material are the same, false otherwise
     */
    public static boolean isItemStackEqual(ItemStack stack1, ItemStack stack2) {
        ItemMeta stack1meta = stack1.getItemMeta();
        ItemMeta stack2meta = stack2.getItemMeta();
        if (stack1.getType() != stack2.getType()) {
            return false;
        }
        if (stack1meta.hasDisplayName() != stack2meta.hasDisplayName()) {
            return false;
        }
        if (stack1meta.hasLore() != stack2meta.hasLore()) {
            return false;
        }
        if (stack1meta.hasDisplayName()) {
            if (!stack1meta.getDisplayName().equals(stack2meta.getDisplayName())) {
                return false;
            }
        }
        if (stack1meta.hasLore()) {
            return stack1meta.getLore().equals(stack2meta.getLore());
        }
        return true;

    }

    public static Optional<Material> getFurnaceResult(Material smeltable) {
        return Optional.ofNullable(furnaceSmeltMap.getOrDefault(smeltable, null));
    }
}
