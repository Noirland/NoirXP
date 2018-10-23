package NoirLeveling.Helpers;

import NoirLeveling.Classes.TameBreedEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Datamaps {
    public static Set<Location> torchSet = new HashSet<>();
    public static HashMap<EntityType, TameBreedEntity> tameBreedEntityMap = new HashMap<>();
    public static Set<Material> armourItems = new HashSet<>();

    static {
        armourItems.add(Material.GOLDEN_HELMET);
        armourItems.add(Material.GOLDEN_CHESTPLATE);
        armourItems.add(Material.GOLDEN_LEGGINGS);
        armourItems.add(Material.GOLDEN_BOOTS);
        armourItems.add(Material.GOLDEN_SWORD);
        armourItems.add(Material.GOLDEN_SHOVEL);
        armourItems.add(Material.GOLDEN_PICKAXE);
        armourItems.add(Material.GOLDEN_HOE);
        armourItems.add(Material.GOLDEN_AXE);
    }
}
