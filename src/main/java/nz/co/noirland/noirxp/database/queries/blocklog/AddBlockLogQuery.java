package nz.co.noirland.noirxp.database.queries.blocklog;

import nz.co.noirland.noirxp.database.queries.XPQuery;
import org.bukkit.Location;

import java.util.Collection;
import java.util.Collections;

public class AddBlockLogQuery extends XPQuery {

    public AddBlockLogQuery(Location location) {
        this(Collections.singleton(location));
    }

    public AddBlockLogQuery(Collection<Location> locations) {
        super(6, "INSERT INTO PlacedBlockData (world, chunkX, chunkZ, x, y, z) VALUES (?, ?, ?, ?, ?, ?)");

        for(Location location : locations) {
            setValue(1, location.getWorld().getName());
            setValue(2, location.getChunk().getX());
            setValue(3, location.getChunk().getZ());
            setValue(4, location.getBlockX());
            setValue(5, location.getBlockY());
            setValue(6, location.getBlockZ());

            batch();
        }
    }
}
