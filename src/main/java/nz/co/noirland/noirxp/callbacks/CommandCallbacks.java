package nz.co.noirland.noirxp.callbacks;

import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.NoirXP;

public class CommandCallbacks {
    /**
     * Sets total xp and xp for all classes to 0.
     * @param playerId The player UUID to reset.
     */
    public static void resetAllXp(String playerId) {
        NoirPlayer player = NoirXP.players.get(playerId);
        player.setXp(0);
        player.alchemy.setXp(0);
        player.building.setXp(0);
        player.cooking.setXp(0);
        player.farming.setXp(0);
        player.fishing.setXp(0);
        player.gathering.setXp(0);
        player.hunting.setXp(0);
        player.mining.setXp(0);
        player.smithing.setXp(0);
        player.taming.setXp(0);
        player.setMaxHealth(20);
        player.setCurrentHealth(20);
    }
}
