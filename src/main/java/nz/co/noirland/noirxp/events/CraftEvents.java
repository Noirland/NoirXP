package nz.co.noirland.noirxp.events;

import nz.co.noirland.noirxp.callbacks.PlayerCallbacks;
import nz.co.noirland.noirxp.config.UserdataConfig;
import nz.co.noirland.noirxp.constants.PlayerClass;
import nz.co.noirland.noirxp.helpers.Datamaps;
import nz.co.noirland.noirxp.helpers.LoreHelper;
import nz.co.noirland.noirxp.struct.ItemXPData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Optional;

public class CraftEvents implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onItemCraft(CraftItemEvent event) {

        if (event.getWhoClicked() instanceof Player && !UserdataConfig.inst().isLevelling(event.getWhoClicked().getUniqueId())) return;

        ItemStack itemStack = event.getRecipe().getResult();

        if(event.getAction() == InventoryAction.NOTHING ||  itemStack == null || itemStack.getType().equals(Material.AIR)) return;

        int craftAmount = itemStack.getAmount();

        if(event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
            craftAmount *= getShiftCraftAmount(event.getInventory());
        }

        Player player = (Player) event.getWhoClicked();
        ItemMeta itemMeta = itemStack.getItemMeta();
        String blockName;
        if (itemMeta.hasDisplayName()) {
            blockName = ChatColor.stripColor(itemMeta.getDisplayName());
        }
        else {
            blockName = itemStack.getType().toString();
        }

        Optional<ItemXPData> xp = Datamaps.getCustomBlock(blockName);
        if(!xp.isPresent()) return;

        int reqLevel = xp.get().levelToCreate;
        PlayerClass playerClass = xp.get().type;

        int playerXp = PlayerCallbacks.getPlayerXpForClass(player.getUniqueId().toString(), playerClass);

        int playerLevel = PlayerCallbacks.getLevelFromXp(playerXp);

        if (playerLevel < reqLevel) {
            event.setCancelled(true);
            player.sendMessage("Level " + reqLevel + " " + playerClass.getTitleLower() + " required.");
            return;
        }

        PlayerCallbacks.xpGained(player.getUniqueId().toString(), playerClass, xp.get().createXP * craftAmount);


    }

    @EventHandler(ignoreCancelled = true)
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        if (event.getInventory().getResult() == null) {
            return;
        }
        ItemStack itemStack = event.getInventory().getResult();

        if(LoreHelper.addLoreToItem(itemStack)) event.getInventory().setResult(itemStack);
    }

    private int getShiftCraftAmount(CraftingInventory inv) {
        boolean first = true;
        int maxAmount = 1;

        for(ItemStack item : inv.getMatrix()) {
            if(item == null || item.getType().equals(Material.AIR)) continue;

            if(first) {
                maxAmount = item.getAmount();
                first = false;
            } else {
                maxAmount = Math.min(maxAmount, item.getAmount());
            }
        }

        return maxAmount;
    }
}
