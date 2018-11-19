package nz.co.noirland.noirxp.database.queries.blocklog;

import nz.co.noirland.noirxp.database.queries.XPQuery;
import org.bukkit.Location;

import java.util.UUID;

public class AddBlockLogQuery extends XPQuery {

    public AddBlockLogQuery(Location location, UUID player, boolean ownsBlock) {
        super(6, "INSERT INTO PlacedBlockData (playerId, world, x, y, z, ownsBlock) VALUES (?, ?, ?, ?, ?, ?)");

        setValue(1, location.getWorld().getName());
        setValue(2, location.getBlockX());
        setValue(3, location.getBlockY());
        setValue(4, location.getBlockZ());
        setValue(5, player.toString());
        setValue(6, ownsBlock);
    }
}
