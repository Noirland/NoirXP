package nz.co.noirland.noirxp.events;

import nz.co.noirland.noirxp.callbacks.BlockCallbacks;
import nz.co.noirland.noirxp.callbacks.PlayerCallbacks;
import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.constants.PlayerClass;
import nz.co.noirland.noirxp.NoirXP;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;

import java.util.HashMap;
import java.util.List;

public class FurnaceEvents implements Listener {
    @EventHandler
    public void onFurnaceExtract(FurnaceExtractEvent event) {
        NoirPlayer noirPlayer = NoirXP.players.get(event.getPlayer().getUniqueId().toString());
        List<HashMap> customBlock = BlockCallbacks.getCustomBlock(event.getItemType().toString());

        if (customBlock == null) {
            return;
        }

        int createXp = (int) customBlock.get(0).get("createXp");

        PlayerClass playerClass = PlayerClass.valueOf((String) customBlock.get(0).get("playerClass"));
        PlayerCallbacks.xpGained(noirPlayer.getUniqueId(), playerClass, createXp * event.getItemAmount());
    }
}
