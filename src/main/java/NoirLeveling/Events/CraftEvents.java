package NoirLeveling.Events;

import NoirLeveling.Callbacks.PlayerCallbacks;
import NoirLeveling.Constants.ITEM_CONSTANTS;
import NoirLeveling.Constants.PlayerClass;
import NoirLeveling.Database.Database;
import NoirLeveling.Helpers.Datamaps;
import NoirLeveling.Helpers.PlayerClassConverter;
import NoirLeveling.SQLProcedures.SQLProcedures;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wood;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CraftEvents implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onItemCraft(CraftItemEvent event) {

        if (event.getWhoClicked() instanceof Player) {
            if (!PlayerCallbacks.isPlayerLevelingEnabled((Player) event.getWhoClicked())) {
                return;
            }
        }

        ItemStack itemStack = event.getRecipe().getResult();
        if (itemStack == null) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        ItemMeta itemMeta = itemStack.getItemMeta();
        String blockName = itemStack.getType().toString();
        if (itemMeta.hasDisplayName()) {
            blockName = ChatColor.stripColor(itemMeta.getDisplayName());
        }
        else {
            blockName = itemStack.getType().toString();
        }

        String sql = SQLProcedures.getCustomBlock(blockName);

        List<HashMap> resultSet = Database.executeSQLGet(sql);
        if (resultSet == null || resultSet.size() == 0) {
            return;
        }
        int reqLevel = (int) resultSet.get(0).get("levelToCreate");
        PlayerClass playerClass;
        int playerXp;
        int playerLevel;
        try {
            playerClass = PlayerClass.valueOf((String) resultSet.get(0).get("playerClass"));
            if (playerClass == PlayerClass.GENERAL) {
                playerXp = PlayerCallbacks.GetPlayerTotalXp(player.getUniqueId().toString());
            }
            else {
                playerXp = PlayerCallbacks.GetPlayerXpForClass(player.getUniqueId().toString(), playerClass);
            }

            playerLevel = PlayerCallbacks.GetLevelFromXp(playerXp);
        }
        catch (IllegalArgumentException e) {
            playerClass = PlayerClass.GENERAL;
            playerXp = PlayerCallbacks.GetPlayerTotalXp(player.getUniqueId().toString());
            playerLevel = PlayerCallbacks.GetLevelFromXp(playerXp);
        }

        if (playerLevel < reqLevel) {
            event.setCancelled(true);
            player.sendMessage("Level " + reqLevel + ChatColor.WHITE + " " +
            PlayerClassConverter.PlayerClassToString(playerClass) + " required.");
            return;
        }

        int createXp = (int) resultSet.get(0).get("createXp");

        PlayerCallbacks.xpGained(player.getUniqueId().toString(), playerClass, createXp);



    }

    @EventHandler(ignoreCancelled = true)
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        if (event.getInventory().getResult() == null) {
            return;
        }
        ItemStack itemStack = event.getInventory().getResult();
        ItemMeta itemMeta = itemStack.getItemMeta();
        String blockName = itemStack.getType().toString();
        if (itemMeta != null) {
            if (itemMeta.hasDisplayName()) {
                blockName = ChatColor.stripColor(itemMeta.getDisplayName());
            }
        }
        else {
            return;
        }
        if (!itemMeta.hasLore()) {
            String sql = SQLProcedures.getCustomBlock(blockName);

            List<HashMap> resultSet = Database.executeSQLGet(sql);
            String playerClassName;
            if (resultSet == null || resultSet.size() == 0) {
                playerClassName = PlayerClass.GENERAL.toString();
            }
            else {
                playerClassName = (String) resultSet.get(0).get("playerClass");
            }
            String playerClassFormatted = PlayerClassConverter.PlayerClassToCapitalString(PlayerClass.valueOf(playerClassName));
            List<String> loreList = new ArrayList<String>();
            loreList.add(playerClassFormatted);
            if (Datamaps.armourItems.contains(itemStack.getType())) {
                int durability = (int) (itemStack.getType().getMaxDurability() * ITEM_CONSTANTS.GOLDEN_DURABILITY_MODIFIER);
                String durabilityLore = String.format("%1$d/%1$d", durability);
                loreList.add(durabilityLore);
            }

            itemMeta.setLore(loreList);
            itemStack.setItemMeta(itemMeta);
            event.getInventory().setResult(itemStack);
        }
    }
}
