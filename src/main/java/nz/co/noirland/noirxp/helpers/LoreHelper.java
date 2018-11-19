package nz.co.noirland.noirxp.helpers;

import nz.co.noirland.noirxp.constants.PlayerClass;
import nz.co.noirland.noirxp.database.XPDatabase;
import nz.co.noirland.noirxp.struct.ItemXPData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoreHelper {
    public static boolean addLoreToItem(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return false;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null || itemMeta.hasLore()) return false;

        String blockName = itemStack.getType().toString();

        if (itemMeta.hasDisplayName()) {
            blockName = ChatColor.stripColor(itemMeta.getDisplayName());
        }

        Optional<ItemXPData> xp = XPDatabase.inst().getCustomBlock(blockName);

        PlayerClass classType = PlayerClass.GENERAL;
        if(xp.isPresent()) {
            classType = xp.get().type;
        }

        String playerClassFormatted = PlayerClassConverter.playerClassToCapitalString(classType);
        List<String> loreList = new ArrayList<>();
        loreList.add(playerClassFormatted);
        if (Datamaps.armourItems.containsKey(itemStack.getType())) {
            int durability = Datamaps.armourItems.get(itemStack.getType());
            String durabilityLore = String.format("%1$d/%1$d", durability);
            loreList.add(durabilityLore);
        }

        itemMeta.setLore(loreList);
        itemStack.setItemMeta(itemMeta);
        return true;
    }

}
