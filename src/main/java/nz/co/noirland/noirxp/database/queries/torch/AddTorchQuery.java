package nz.co.noirland.noirxp.database.queries.torch;

import nz.co.noirland.noirxp.database.queries.XPQuery;
import org.bukkit.Location;

public class AddTorchQuery extends XPQuery {

    public AddTorchQuery(Location location) {
        super(4, "INSERT INTO TorchPlacedData (world, x, y, z) VALUES (?, ?, ?, ?);");

        setValue(1, location.getWorld().getName());
        setValue(2, location.getBlockX());
        setValue(3, location.getBlockY());
        setValue(4, location.getBlockZ());
    }
}
