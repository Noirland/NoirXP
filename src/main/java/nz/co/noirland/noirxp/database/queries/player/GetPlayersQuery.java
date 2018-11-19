package nz.co.noirland.noirxp.database.queries.player;

import nz.co.noirland.noirxp.database.queries.XPQuery;

public class GetPlayersQuery extends XPQuery {

    public GetPlayersQuery() {
        super("SELECT * FROM Player");
    }
}
