package nz.co.noirland.noirxp.database.queries.blocklog;

import nz.co.noirland.noirxp.database.queries.XPQuery;
import org.bukkit.Location;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class RemoveBlockLogQuery extends XPQuery {

    public RemoveBlockLogQuery(Location location) {
        this(Collections.singleton(location));
    }

    public RemoveBlockLogQuery(Collection<Location> locations) {
        super(4, "DELETE FROM PlacedBlockData WHERE world = ? AND x = ? AND y = ? AND z = ?;");

        for(Location location : locations) {
            setValue(1, location.getWorld().getName());
            setValue(2, location.getBlockX());
            setValue(3, location.getBlockY());
            setValue(4, location.getBlockZ());

            batch();
        }
    }
}
