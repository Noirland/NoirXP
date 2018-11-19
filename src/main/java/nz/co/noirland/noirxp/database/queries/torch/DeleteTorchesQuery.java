package nz.co.noirland.noirxp.database.queries.torch;

import nz.co.noirland.noirxp.database.queries.XPQuery;

public class DeleteTorchesQuery extends XPQuery {

    public DeleteTorchesQuery() {
        super("DELETE FROM TorchPlacedData;");
    }
}
