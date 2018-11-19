package nz.co.noirland.noirxp.database.queries.blocklog;

import nz.co.noirland.noirxp.database.queries.XPQuery;
import org.bukkit.Location;

public class CheckBlockLogQuery extends XPQuery {

    public CheckBlockLogQuery(Location location) {
        super(4, "SELECT COUNT(*) AS COUNT FROM PlacedBlockData WHERE world = ? AND x = ? AND y = ? AND z = ?;");

        setValue(1, location.getWorld().getName());
        setValue(2, location.getBlockX());
        setValue(3, location.getBlockY());
        setValue(4, location.getBlockZ());

    }
}
