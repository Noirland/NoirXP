package nz.co.noirland.noirxp.database.queries.blocklog;

import nz.co.noirland.noirxp.database.queries.XPQuery;

public class PruneBlockLogQuery extends XPQuery {

    public PruneBlockLogQuery(long deleteAmount) {
        super(1, "DELETE FROM PlacedBlockData ORDER BY id ASC LIMIT ?;");

        setValue(1, deleteAmount);
    }
}
