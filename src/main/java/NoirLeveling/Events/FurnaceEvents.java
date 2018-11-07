package NoirLeveling.Events;

import NoirLeveling.Callbacks.BlockCallbacks;
import NoirLeveling.Callbacks.PlayerCallbacks;
import NoirLeveling.Classes.NoirPlayer;
import NoirLeveling.Constants.PlayerClass;
import NoirLeveling.Helpers.PlayerClassConverter;
import NoirLeveling.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;

import java.util.HashMap;
import java.util.List;

public class FurnaceEvents implements Listener {
    @EventHandler
    public void onFurnaceExtract(FurnaceExtractEvent event) {
        NoirPlayer noirPlayer = Main.players.get(event.getPlayer().getUniqueId().toString());
        List<HashMap> customBlock = BlockCallbacks.getCustomBlock(event.getItemType().toString());

        if (customBlock == null) {
            return;
        }

        int createXp = (int) customBlock.get(0).get("createXp");

        PlayerClass playerClass = PlayerClass.valueOf((String) customBlock.get(0).get("playerClass"));
        PlayerCallbacks.xpGained(noirPlayer.getUniqueId(), playerClass, createXp * event.getItemAmount());
    }
}
