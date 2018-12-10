package nz.co.noirland.noirxp.events;

import nz.co.noirland.noirxp.NoirXP;
import nz.co.noirland.noirxp.callbacks.CraftCallbacks;
import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.config.UserdataConfig;
import nz.co.noirland.noirxp.constants.PlayerClass;
import nz.co.noirland.noirxp.helpers.Datamaps;
import nz.co.noirland.noirxp.struct.ItemXPData;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class FurnaceEvents implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onInventoryMoveItem(InventoryClickEvent event) {
        if(!(event.getInventory() instanceof FurnaceInventory)) return;

        if(!UserdataConfig.inst().isLevelling(event.getWhoClicked().getUniqueId())) return;

        ItemStack placed;

        switch(event.getAction()) {
            case PLACE_ALL:
            case PLACE_SOME:
            case PLACE_ONE:
            case SWAP_WITH_CURSOR:
                // Player didn't place in furnace's slots
                if(!(event.getClickedInventory() instanceof FurnaceInventory)) return;

                placed = event.getCursor();
                break;
            case MOVE_TO_OTHER_INVENTORY:
                // Player moved items out of furnace
                if(event.getClickedInventory() instanceof FurnaceInventory) return;

                placed = event.getCurrentItem();
                break;
            default:
                return; // We only care if player is modifying the placing/moving to furnace
        }

        Optional<Material> output = CraftCallbacks.getFurnaceResult(placed.getType());
        if(!output.isPresent()) return; // Not a valid smelting item

        Optional<ItemXPData> xp = Datamaps.getCustomBlock(output.get());
        if(!xp.isPresent()) return;

        int reqLevel = xp.get().levelToCreate;
        PlayerClass playerClass = xp.get().type;

        NoirPlayer noirPlayer = NoirXP.getPlayer(event.getWhoClicked().getUniqueId());

        int playerLevel = noirPlayer.getLevel(playerClass);

        if (playerLevel < reqLevel) {
            event.setCancelled(true);
            event.getWhoClicked().sendMessage("Level " + reqLevel + " " + playerClass.getTitleLower() + " required.");
        }
    }

    @EventHandler
    public void onFurnaceExtract(FurnaceExtractEvent event) {
        NoirPlayer noirPlayer = NoirXP.players.get(event.getPlayer().getUniqueId().toString());

        Optional<ItemXPData> xp = Datamaps.getCustomBlock(event.getItemType());
        if (!xp.isPresent()) return;

        noirPlayer.giveXP(xp.get().type, xp.get().createXP * event.getItemAmount());
    }
}
