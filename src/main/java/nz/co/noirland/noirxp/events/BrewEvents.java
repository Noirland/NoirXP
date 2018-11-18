package nz.co.noirland.noirxp.events;

import nz.co.noirland.noirxp.callbacks.BlockCallbacks;
import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.classes.PlacedBlock;
import nz.co.noirland.noirxp.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.ItemStack;

public class BrewEvents implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onItemBrew(BrewEvent event) {
        PlacedBlock placedBlock = BlockCallbacks.getPlacedBlock(event.getBlock().getLocation());
        if (placedBlock != null) {
            if (placedBlock.ownsBlock()) {
                NoirPlayer player = Main.players.get(placedBlock.getPlayerId());
                ItemStack item1 = event.getContents().getItem(0);
                ItemStack item2 = event.getContents().getItem(1);
                ItemStack item3 = event.getContents().getItem(2);
            }
        }
    }
}
