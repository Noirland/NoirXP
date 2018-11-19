package nz.co.noirland.noirxp.database.queries.player;

import nz.co.noirland.noirxp.database.queries.XPQuery;

import java.util.UUID;

public class AddPlayerQuery extends XPQuery {

    public AddPlayerQuery(UUID player) {
        super(1, "INSERT IGNORE INTO Player (playerId) VALUES (?)");

        setValue(1, player.toString());
    }
}
