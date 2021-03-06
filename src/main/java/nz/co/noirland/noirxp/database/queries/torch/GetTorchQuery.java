package nz.co.noirland.noirxp.database.queries.torch;

import nz.co.noirland.noirxp.database.queries.XPQuery;
import org.bukkit.Location;

public class GetTorchQuery extends XPQuery {

    public GetTorchQuery(Location location) {
        super(4, "SELECT world, x, y, z FROM TorchPlacedData WHERE world = ? AND x = ? AND y = ? AND z = ?;");

        setValue(1, location.getWorld().getName());
        setValue(2, location.getBlockX());
        setValue(3, location.getBlockY());
        setValue(4, location.getBlockZ());
    }
}
