package nz.co.noirland.noirxp.helpers;

import nz.co.noirland.noirxp.classes.TameBreedEntity;
import nz.co.noirland.noirxp.constants.ITEM_CONSTANTS;
import nz.co.noirland.noirxp.database.XPDatabase;
import nz.co.noirland.noirxp.struct.ItemXPData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.*;

public class Datamaps {
    public static Set<Location> torchSet = new HashSet<>();
    public static HashMap<EntityType, TameBreedEntity> tameBreedEntityMap = new HashMap<>();
    public static Map<Material, Integer> armourItems = new HashMap<>();
    public static Map<String, ItemXPData> customBlocks = new HashMap<>();

    static {
        armourItems.put(Material.GOLDEN_HELMET, (int)(Material.IRON_HELMET.getMaxDurability() * ITEM_CONSTANTS.GOLDEN_DURABILITY_MODIFIER));
        armourItems.put(Material.GOLDEN_CHESTPLATE, (int) (Material.IRON_CHESTPLATE.getMaxDurability() * ITEM_CONSTANTS.GOLDEN_DURABILITY_MODIFIER));
        armourItems.put(Material.GOLDEN_LEGGINGS, (int) (Material.IRON_LEGGINGS.getMaxDurability() * ITEM_CONSTANTS.GOLDEN_DURABILITY_MODIFIER));
        armourItems.put(Material.GOLDEN_BOOTS, (int) (Material.IRON_BOOTS.getMaxDurability() * ITEM_CONSTANTS.GOLDEN_DURABILITY_MODIFIER));
        armourItems.put(Material.GOLDEN_SWORD, (int) (Material.IRON_SWORD.getMaxDurability() * ITEM_CONSTANTS.GOLDEN_DURABILITY_MODIFIER));
        armourItems.put(Material.GOLDEN_SHOVEL, (int) (Material.IRON_SHOVEL.getMaxDurability() * ITEM_CONSTANTS.GOLDEN_DURABILITY_MODIFIER));
        armourItems.put(Material.GOLDEN_PICKAXE, (int) (Material.IRON_PICKAXE.getMaxDurability() * ITEM_CONSTANTS.GOLDEN_DURABILITY_MODIFIER));
        armourItems.put(Material.GOLDEN_HOE, (int) (Material.IRON_HOE.getMaxDurability() * ITEM_CONSTANTS.GOLDEN_DURABILITY_MODIFIER));
        armourItems.put(Material.GOLDEN_AXE, (int) (Material.IRON_AXE.getMaxDurability() * ITEM_CONSTANTS.GOLDEN_DURABILITY_MODIFIER));
    }

    public static void removeTorch(Location location) {
        if(Datamaps.torchSet.remove(location)) XPDatabase.inst().removeTorch(location);
    }

    public static void addTorch(Location location) {
        if(Datamaps.torchSet.add(location)) XPDatabase.inst().addTorch(location);
    }

    public static Optional<ItemXPData> getCustomBlock(String material) {
        return Optional.ofNullable(customBlocks.get(material));
    }

    public static Optional<ItemXPData> getCustomBlock(Material material) {
        return getCustomBlock(material.toString());
    }
}
