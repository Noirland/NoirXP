package nz.co.noirland.noirxp.events;

import nz.co.noirland.noirxp.NoirXP;
import nz.co.noirland.noirxp.callbacks.PlayerCallbacks;
import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.database.XPDatabase;
import nz.co.noirland.noirxp.struct.ItemXPData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;

import java.util.Optional;

public class FurnaceEvents implements Listener {
    @EventHandler
    public void onFurnaceExtract(FurnaceExtractEvent event) {
        NoirPlayer noirPlayer = NoirXP.players.get(event.getPlayer().getUniqueId().toString());

        Optional<ItemXPData> xp = XPDatabase.inst().getCustomBlock(event.getItemType());
        if (!xp.isPresent()) return;

        PlayerCallbacks.xpGained(noirPlayer.getUniqueId(), xp.get().type, xp.get().createXP * event.getItemAmount());
    }
}
