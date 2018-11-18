package nz.co.noirland.noirxp.helpers;

import nz.co.noirland.noirxp.constants.PlayerClass;
import nz.co.noirland.noirxp.database.Database;
import nz.co.noirland.noirxp.sqlprocedures.SQLProcedures;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoreHelper {
    public static void addLoreToItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta.hasLore()) {
            return;
        }

        String blockName;

        if (itemMeta.hasDisplayName()) {
            blockName = ChatColor.stripColor(itemMeta.getDisplayName());
        }
        else {
            blockName = item.getType().toString();
        }
        String sql = SQLProcedures.getCustomBlock(blockName);

        String playerClassName;

        List<HashMap> resultSet = Database.executeSQLGet(sql);
        if (resultSet == null || resultSet.size() == 0) {
            playerClassName = "GENERAL";
        }
        else {
            playerClassName = (String) resultSet.get(0).get("playerClass");
        }

        String playerClassFormatted = PlayerClassConverter.playerClassToCapitalString(PlayerClass.valueOf(playerClassName));
        List<String> loreList = new ArrayList<String>();
        loreList.add(playerClassFormatted);
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
    }
}
