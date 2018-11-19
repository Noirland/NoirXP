package nz.co.noirland.noirxp.database.queries.blocklog;

import nz.co.noirland.noirxp.database.queries.XPQuery;
import org.bukkit.Location;

public class RemoveBlockLogQuery extends XPQuery {

    public RemoveBlockLogQuery(Location location) {
        super(4, "DELETE FROM PlacedBlockData WHERE world = ? AND x = ? AND y = ? AND z = ?;");

        setValue(1, location.getWorld().getName());
        setValue(2, location.getBlockX());
        setValue(3, location.getBlockY());
        setValue(4, location.getBlockZ());
    }
}
