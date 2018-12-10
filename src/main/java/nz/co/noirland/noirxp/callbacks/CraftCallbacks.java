package nz.co.noirland.noirxp.callbacks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;

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

    public static Optional<Material> getFurnaceResult(Material smeltable) {
        return Optional.ofNullable(furnaceSmeltMap.getOrDefault(smeltable, null));
    }
}
