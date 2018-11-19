package nz.co.noirland.noirxp.database.queries.torch;

import nz.co.noirland.noirxp.database.queries.XPQuery;

public class GetTorchesQuery extends XPQuery {

    public GetTorchesQuery() {
        super("SELECT * FROM TorchPlacedData");
    }
}
