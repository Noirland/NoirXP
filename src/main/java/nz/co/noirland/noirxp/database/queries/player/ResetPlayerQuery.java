package nz.co.noirland.noirxp.database.queries.player;

import nz.co.noirland.noirxp.database.queries.XPQuery;

import java.util.UUID;

public class ResetPlayerQuery extends XPQuery {

    public ResetPlayerQuery(UUID player) {
        super(1, "UPDATE Player SET alchemyXp = 0, buildingXp = 0, cookingXp = 0, " +
                "farmingXp = 0, fishingXp = 0, gatheringXp = 0, huntingXp = 0, miningXp = 0, " +
                "smithingXp = 0, tamingXp = 0, totalXp = 0 WHERE playerId = ?");

        setValue(1, player.toString());
    }
}
