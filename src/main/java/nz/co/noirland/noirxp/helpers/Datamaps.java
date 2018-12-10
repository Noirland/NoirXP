package nz.co.noirland.noirxp.helpers;

import nz.co.noirland.noirxp.classes.TameBreedEntity;
import nz.co.noirland.noirxp.constants.ITEM_CONSTANTS;
import nz.co.noirland.noirxp.database.XPDatabase;
import nz.co.noirland.noirxp.struct.ItemXPData;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class Datamaps {
    public static Set<Location> torchSet = new HashSet<>();
    public static HashMap<EntityType, TameBreedEntity> tameBreedEntityMap = new HashMap<>();
    public static Map<Material, Integer> armourItems = new HashMap<>();
    public static Map<String, ItemXPData> customBlocks = new HashMap<>();

    private static Map<Chunk, Set<Location>> blockLog = new HashMap<>();
    private static Map<Chunk, Set<Location>> logAddUnsynced = new HashMap<>();
    private static Map<Chunk, Set<Location>> logRemoveUnsynced = new HashMap<>();
    private static Map<Location, UUID> playerOwnedBlocks = new HashMap<>();

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

    public static boolean isPlaced(Location location) {
        return blockLog.containsKey(location.getChunk()) && blockLog.get(location.getChunk()).contains(location);
    }

    public static void addBlock(Location location) {
        Chunk c = location.getChunk();

        if(!blockLog.containsKey(c)) blockLog.put(c, new HashSet<>());
        if(!logAddUnsynced.containsKey(c)) logAddUnsynced.put(c, new HashSet<>());

        blockLog.get(c).add(location);
        logAddUnsynced.get(c).add(location);

        if(logRemoveUnsynced.containsKey(c)) logRemoveUnsynced.get(c).remove(location);
    }

    public static void removeBlock(Location location) {
        Chunk c = location.getChunk();
        if(blockLog.containsKey(c) && blockLog.get(c).remove(location)) {
            if(!logRemoveUnsynced.containsKey(c)) logRemoveUnsynced.put(c, new HashSet<>());
            logRemoveUnsynced.get(c).add(location);
        }

        if(logAddUnsynced.containsKey(c)) logAddUnsynced.get(c).remove(location);
    }

    public static Map<Chunk, Set<Location>> clearUnsyncedAddBlock() {
        Map<Chunk, Set<Location>> ret = logAddUnsynced;
        logAddUnsynced = new HashMap<>();
        return ret;
    }

    public static Map<Chunk, Set<Location>> clearUnsyncedRemoveBlock() {
        Map<Chunk, Set<Location>> ret = logRemoveUnsynced;
        logRemoveUnsynced = new HashMap<>();
        return ret;
    }

    public static void replaceBlockLog(Map<Chunk, Set<Location>> locations) {
        blockLog = locations;
        for(Chunk chunk : logAddUnsynced.keySet()) {
            blockLog.get(chunk).addAll(logAddUnsynced.get(chunk)); // Add any unsyced after the cache was refreshed
        }
    }

    public static void loadChunk(Chunk chunk, Set<Location> locations) {
        if(locations.isEmpty()) return;
        blockLog.put(chunk, locations);
        if(!logAddUnsynced.containsKey(chunk)) return;
        blockLog.get(chunk).addAll(logAddUnsynced.get(chunk));
    }

    public static void unlockChunk(Chunk chunk) {
        // No need to do DB operations here, any changes have been recorded in logAddUnsynced or logRemoveUnsynced
        blockLog.remove(chunk);
    }

    public static Optional<UUID> getOwner(Location location) {
        return Optional.ofNullable(playerOwnedBlocks.getOrDefault(location, null));
    }

    public static void setOwner(Location location, UUID owner) {
        playerOwnedBlocks.put(location, owner);
        //TODO: Database
    }

    public static void removeOwner(Location location) {
        if(playerOwnedBlocks.remove(location) != null) {
            // TODO: Database
        }
    }


}
