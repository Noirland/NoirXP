package NoirLeveling.Events;

import NoirLeveling.Callbacks.BlockCallbacks;
import NoirLeveling.Callbacks.PlayerCallbacks;
import NoirLeveling.Constants.PlayerClass;
import NoirLeveling.Database.Database;
import NoirLeveling.Helpers.Datamaps;
import NoirLeveling.Helpers.PlayerClassConverter;
import NoirLeveling.Main;
import NoirLeveling.SQLProcedures.SQLProcedures;
import org.bukkit.ChatColor;
import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Crops;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.*;

public class BlockEvents implements Listener {
    HashSet<Biome> snowBiomes = new HashSet<Biome>();

    public BlockEvents() {
        snowBiomes.add(Biome.SNOWY_BEACH);
        snowBiomes.add(Biome.SNOWY_MOUNTAINS);
        snowBiomes.add(Biome.SNOWY_TAIGA);
        snowBiomes.add(Biome.SNOWY_TAIGA_HILLS);
        snowBiomes.add(Biome.SNOWY_TAIGA_MOUNTAINS);
        snowBiomes.add(Biome.SNOWY_TUNDRA);
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void OnBlockBreak(BlockBreakEvent event) {

        if (!PlayerCallbacks.isPlayerLevelingEnabled(event.getPlayer())) {
            return;
        }

        Location location = event.getBlock().getLocation();

        Datamaps.torchSet.remove(location);

        Player player = event.getPlayer();
        String blockName = event.getBlock().getType().toString();
        String sql = SQLProcedures.getCustomBlock(blockName);


        List<HashMap> resultSet = Database.executeSQLGet(sql);
        if (resultSet.size() == 0) {
            return;
        }
        int reqLevel = (int) resultSet.get(0).get("levelToBreak");
        PlayerClass playerClass;
        int playerXp;
        int playerLevel;
        try {
            playerClass = PlayerClass.valueOf((String) resultSet.get(0).get("playerClass"));
            if (playerClass == PlayerClass.GENERAL) {
                playerXp = PlayerCallbacks.getPlayerTotalXp(player.getUniqueId().toString());
            }
            else {
                playerXp = PlayerCallbacks.getPlayerXpForClass(player.getUniqueId().toString(), playerClass);
            }

            playerLevel = PlayerCallbacks.getLevelFromXp(playerXp);
        }
        catch (IllegalArgumentException e) {
            playerClass = PlayerClass.GENERAL;
            playerXp = PlayerCallbacks.getPlayerTotalXp(player.getUniqueId().toString());
            playerLevel = PlayerCallbacks.getLevelFromXp(playerXp);
        }

        if (playerLevel < reqLevel) {
            event.setCancelled(true);
            player.sendMessage("Level " + reqLevel + ChatColor.WHITE + " " +
                    PlayerClassConverter.playerClassToString(playerClass) + " required.");
            return;
        }


        boolean isCrop = false;
        if (event.getBlock().getState().getData() instanceof Crops) {
            Crops crops = (Crops)(event.getBlock().getState().getData());
            if (crops.getState() == CropState.RIPE) {
                isCrop = true;
            }
        }


        int breakXp = (int) resultSet.get(0).get("breakXp");
        if (BlockCallbacks.hasBlockGivenXp(location)) {
            try {
                Database.executeSQLUpdateDelete(SQLProcedures.deleteFromBlockDataTable(location));
                if (!isCrop) {
                    breakXp = 0;
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

        }


        String playerId = event.getPlayer().getUniqueId().toString();

        PlayerCallbacks.xpGained(playerId, playerClass, breakXp);




    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void OnBlockPlace(BlockPlaceEvent event) {

        if (!PlayerCallbacks.isPlayerLevelingEnabled(event.getPlayer())) {
            return;
        }
        Location location = event.getBlockPlaced().getLocation();
        BlockCallbacks.addBlockLocationToLogTable(location);
        if (event.getBlock().getType() == Material.TORCH || event.getBlock().getType() == Material.WALL_TORCH) {
            Datamaps.torchSet.add(location);
            if (event.getBlockReplacedState().getType() == Material.SNOW) {
                event.getBlockPlaced().breakNaturally();
                Datamaps.torchSet.remove(location);
                return;
            }
            if (event.getPlayer().getWorld().hasStorm()) {
                if (location.getWorld().getHighestBlockYAt(location) == location.getBlockY()) {
                    if (location.getBlock().getType() == Material.TORCH || location.getBlock().getType() == Material.WALL_TORCH) {
                        location.getBlock().breakNaturally();
                        Datamaps.torchSet.remove(location);
                    }
                }
                return;
            }
            if (snowBiomes.contains(event.getBlockAgainst().getBiome())) {
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        if (location.getBlock().getType() == Material.TORCH || event.getBlock().getType() == Material.WALL_TORCH) {
                            location.getBlock().breakNaturally();
                            Datamaps.torchSet.remove(location);

                        }
                    }

                }.runTaskLater(Main.plugin, 20 * 60 * 5);
            }
            else {
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        if (location.getBlock().getType() == Material.TORCH || event.getBlock().getType() == Material.WALL_TORCH) {
                            location.getBlock().breakNaturally();
                            Datamaps.torchSet.remove(location);
                        }
                    }

                }.runTaskLater(Main.plugin, 20 * 60 * 60);
            }
        }
        Player player = event.getPlayer();
        String blockName = event.getBlock().getType().toString();
        String sql = SQLProcedures.getCustomBlock(blockName);

        ItemStack stack = event.getItemInHand();
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            if (meta.hasLore()) {
                return;
            }
        }


        List<HashMap> resultSet = Database.executeSQLGet(sql);
        if (resultSet.size() == 0) {
            return;
        }

        int reqLevel = (int) resultSet.get(0).get("levelToPlace");
        PlayerClass playerClass;
        int playerXp;
        int playerLevel;
        try {
            playerClass = PlayerClass.valueOf((String) resultSet.get(0).get("playerClass"));
            if (playerClass == PlayerClass.GENERAL) {
                playerXp = PlayerCallbacks.getPlayerTotalXp(player.getUniqueId().toString());
            }
            else {
                playerXp = PlayerCallbacks.getPlayerXpForClass(player.getUniqueId().toString(), playerClass);
            }

            playerLevel = PlayerCallbacks.getLevelFromXp(playerXp);
        }
        catch (IllegalArgumentException e) {
            playerClass = PlayerClass.GENERAL;
            playerXp = PlayerCallbacks.getPlayerTotalXp(player.getUniqueId().toString());
            playerLevel = PlayerCallbacks.getLevelFromXp(playerXp);
        }

        if (playerLevel < reqLevel) {
            event.setCancelled(true);
            player.sendMessage("Level " + reqLevel + ChatColor.WHITE + " " +
                    PlayerClassConverter.playerClassToString(playerClass) + " required.");
            return;
        }

        int placeXp = (int) resultSet.get(0).get("placeXp");


        String playerId = event.getPlayer().getUniqueId().toString();
        PlayerCallbacks.xpGained(playerId, playerClass, placeXp);


    }

}
