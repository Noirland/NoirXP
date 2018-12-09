package nz.co.noirland.noirxp.database.queries.player;

import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.constants.PlayerClass;
import nz.co.noirland.noirxp.database.queries.XPQuery;

import java.util.Collection;
import java.util.Collections;

public class UpdatePlayerQuery extends XPQuery {

    public UpdatePlayerQuery(NoirPlayer player) {
        this(Collections.singleton(player));


    }

    public UpdatePlayerQuery(Collection<NoirPlayer> players) {
        super(15, "UPDATE Player SET username = ?, alchemyXp = ?, buildingXp = ?, " +
                "cookingXp = ?, farmingXp = ?, fishingXp = ?, gatheringXp = ?, huntingXp = ?, miningXp = ?, " +
                "smithingXp = ?, tamingXp = ?, totalXp = ?, currentHealth = ?, maxHealth = ? WHERE playerId = ?");

        for(NoirPlayer player : players) {
            setValue(1, player.getUsername());
            setValue(2, player.getXP(PlayerClass.ALCHEMY));
            setValue(3, player.getXP(PlayerClass.BUILDING));
            setValue(4, player.getXP(PlayerClass.COOKING));
            setValue(5, player.getXP(PlayerClass.FARMING));
            setValue(6, player.getXP(PlayerClass.FISHING));
            setValue(7, 0); // Unused
            setValue(8, player.getXP(PlayerClass.HUNTING));
            setValue(9, player.getXP(PlayerClass.MINING));
            setValue(10, player.getXP(PlayerClass.SMITHING));
            setValue(11, player.getXP(PlayerClass.TAMING));
            setValue(12, player.getXP(PlayerClass.GENERAL));
            setValue(13, 20); // Current Health not used
            setValue(14, player.getMaxHealth());
            setValue(15, player.getUniqueId().toString());

            batch();
        }
    }
}
