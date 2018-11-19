package nz.co.noirland.noirxp.database.queries.player;

import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.database.queries.XPQuery;

public class UpdatePlayerQuery extends XPQuery {

    public UpdatePlayerQuery(NoirPlayer player) {
        super(15, "UPDATE Player SET username = ?, alchemyXp = ?, buildingXp = ?, " +
                "cookingXp = ?, farmingXp = ?, fishingXp = ?, gatheringXp = ?, huntingXp = ?, miningXp = ?, " +
                "smithingXp = ?, tamingXp = ?, totalXp = ?, currentHealth = ?, maxHealth = ? WHERE playerId = ?");

        setValue(1, player.getUsername());
        setValue(2, player.alchemy.getXp());
        setValue(3, player.building.getXp());
        setValue(4, player.cooking.getXp());
        setValue(5, player.farming.getXp());
        setValue(6, player.fishing.getXp());
        setValue(7, player.gathering.getXp());
        setValue(8, player.hunting.getXp());
        setValue(9, player.mining.getXp());
        setValue(10, player.smithing.getXp());
        setValue(11, player.taming.getXp());
        setValue(12, player.getXp());
        setValue(13, player.getCurrentHealth());
        setValue(14, player.getMaxHealth());
        setValue(15, player.getUniqueId());
    }
}
