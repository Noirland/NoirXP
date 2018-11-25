package nz.co.noirland.noirxp.events;

import nz.co.noirland.noirxp.database.XPDatabase;
import nz.co.noirland.noirxp.helpers.Datamaps;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkEvents implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onChunkLoad(ChunkLoadEvent event) {
        if(event.isNewChunk()) return;
        XPDatabase.inst().loadBlockLog(event.getChunk());

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onChunkUnload(ChunkUnloadEvent event) {
        Datamaps.unlockChunk(event.getChunk());
    }
}
