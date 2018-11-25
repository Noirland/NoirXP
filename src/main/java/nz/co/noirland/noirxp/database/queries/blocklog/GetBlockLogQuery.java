package nz.co.noirland.noirxp.database.queries.blocklog;

import nz.co.noirland.noirxp.database.queries.XPQuery;
import org.bukkit.Chunk;

public class GetBlockLogQuery extends XPQuery {

    public GetBlockLogQuery(Chunk chunk) {
        super(3, "SELECT * FROM PlacedBlockData WHERE world = ? AND chunkX = ? AND chunkZ = ?;");

        setValue(1, chunk.getWorld().getName());
        setValue(2, chunk.getX());
        setValue(3, chunk.getZ());
    }
}
