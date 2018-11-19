package nz.co.noirland.noirxp.callbacks;

import nz.co.noirland.noirxp.classes.NoirPlayer;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BlockCallbacks {
    /**
     * Sets a players username based on their highest xp profession.
     * @param player The player to set.
     */
    public static void setPlayerChatColor(NoirPlayer player) {
        ChatColor command = ChatColor.WHITE;
        int alchemyXp = player.alchemy.getXp();
        int buildingXp = player.building.getXp();
        int cookingXp = player.cooking.getXp();
        int farmingXp = player.farming.getXp();
        int fishingXp = player.fishing.getXp();
        int gatheringXp = player.gathering.getXp();
        int huntingXp = player.hunting.getXp();
        int miningXp = player.mining.getXp();
        int smithingXp = player.smithing.getXp();
        int tamingXp = player.taming.getXp();
        int highest = NumberUtils.max(new int[] {alchemyXp, buildingXp, cookingXp, farmingXp, fishingXp, gatheringXp, huntingXp, miningXp, smithingXp, tamingXp});

        if (highest == 0) {
            command = ChatColor.LIGHT_PURPLE;
        }
        else if (highest == alchemyXp) {
            command = ChatColor.DARK_PURPLE;
        }
        else if (highest == buildingXp) {
            command = ChatColor.RED;
        }
        else if (highest == cookingXp) {
            command = ChatColor.YELLOW;
        }
        else if (highest == farmingXp) {
            command = ChatColor.GREEN;
        }
        else if (highest == fishingXp) {
            command = ChatColor.BLUE;
        }
        else if (highest == gatheringXp) {
            command = ChatColor.DARK_GREEN;
        }
        else if (highest == huntingXp) {
            command = ChatColor.DARK_RED;
        }
        else if (highest == miningXp) {
            command = ChatColor.GOLD;
        }
        else if (highest == smithingXp) {
            command = ChatColor.DARK_GRAY;
        }
        else if (highest == tamingXp) {
            command = ChatColor.DARK_AQUA;
        }

        Player bukkitPlayer = player.getBukkitPlayer();
        bukkitPlayer.setDisplayName(command + bukkitPlayer.getName() + ChatColor.WHITE);


    }
}
