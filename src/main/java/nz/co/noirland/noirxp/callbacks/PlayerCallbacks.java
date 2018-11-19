package nz.co.noirland.noirxp.callbacks;

import nz.co.noirland.noirxp.NoirXP;
import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.config.UserdataConfig;
import nz.co.noirland.noirxp.constants.PlayerClass;
import nz.co.noirland.noirxp.helpers.PlayerClassConverter;
import nz.co.noirland.noirxp.interfaces.INoirProfession;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;


public final class PlayerCallbacks {

    public static int getPlayerXpForClass(String playerId, PlayerClass playerClass) {
        NoirPlayer player = NoirXP.players.get(playerId);
        switch (playerClass) {
            case MINING:
                return player.mining.getXp();
            case TAMING:
                return player.taming.getXp();
            case ALCHEMY:
                return player.alchemy.getXp();
            case COOKING:
                return player.cooking.getXp();
            case FARMING:
                return player.farming.getXp();
            case FISHING:
                return player.fishing.getXp();
            case HUNTING:
                return player.hunting.getXp();
            case BUILDING:
                return player.building.getXp();
            case SMITHING:
                return player.smithing.getXp();
            case GATHERING:
                return player.gathering.getXp();
            case GENERAL:
                return player.getXp();
            default:
                return player.getXp();

        }
    }

     public static int getLevelFromXp(int xp) {
        // The formula for xp is (8x^3/3 + 8x^2 - 32x/3)
        if (xp < 32) {
            return 1;
        }
        int levelCounter = 2;
        double result = 0;
        while (true) {
            result = (8 * Math.pow(levelCounter, 3)) / 3 + (8 * Math.pow(levelCounter, 2)) - ((32 * levelCounter) / 3);
            if (result > xp) {
                break;
            }
            levelCounter++;
        }
        return levelCounter - 1;
    }

    public static int GetXpFromLevel(int level) {
        if (level <= 2) {
            return 32;
        }
        double result = (8 * Math.pow(level, 3)) / 3 + (8 * Math.pow(level, 2)) - ((32 * level) / 3);
        return (int)Math.round(result);
    }

    /**
     * Call this when player xp is about to change, BEFORE any updates to the database
     * @param playerId The player to check
     * @param playerClass The class that gained the xp
     * @param xpGained The amount of xp the player is about to gain
     */
    public static void xpGained(String playerId, PlayerClass playerClass, int xpGained) {
        NoirPlayer player = NoirXP.players.get(playerId);
        int currentClassXp = PlayerCallbacks.getPlayerXpForClass(playerId, playerClass);
        int newClassXp = currentClassXp + xpGained;

        int newClassLevel = PlayerCallbacks.getLevelFromXp(newClassXp);

        if (newClassLevel <= 50) {
            switch (playerClass) {
                case ALCHEMY:
                    player.alchemy.addXp(xpGained);
                    break;
                case BUILDING:
                    player.building.addXp(xpGained);
                    break;
                case COOKING:
                    player.cooking.addXp(xpGained);
                    break;
                case FARMING:
                    player.farming.addXp(xpGained);
                    break;
                case FISHING:
                    player.fishing.addXp(xpGained);
                    break;
                case GATHERING:
                    player.gathering.addXp(xpGained);
                    break;
                case HUNTING:
                    player.hunting.addXp(xpGained);
                    break;
                case MINING:
                    player.mining.addXp(xpGained);
                    break;
                case SMITHING:
                    player.smithing.addXp(xpGained);
                    break;
                case TAMING:
                    player.taming.addXp(xpGained);
                    break;
                case GYPSY:
                    break;
                case GENERAL:
                    break;
            }

            if (UserdataConfig.inst().isVerbose(player.getBukkitPlayer().getUniqueId())) {
                player.getBukkitPlayer().sendMessage("+" + xpGained + " " + PlayerClassConverter.playerClassToString(playerClass));
            }

            if (playerClass != PlayerClass.GENERAL) {
                if (player.isLevelUp(currentClassXp, newClassXp)) {
                    player.getBukkitPlayer().sendMessage("Your " + PlayerClassConverter.playerClassToString(playerClass) + " level just " +
                            "increased to " + ChatColor.GOLD + newClassLevel + ChatColor.WHITE + "!");
                }
            }
        }
        int currentLevel = player.getLevel();

        if (currentLevel < 50) {
            player.addXp(xpGained);
            int newLevel = player.getLevel();

            if (newLevel > currentLevel) {
                player.getBukkitPlayer().sendMessage("Your overall level just " +
                        "increased to " + ChatColor.GOLD + newLevel + ChatColor.WHITE + "!");
            }
        }

    }

    public static NoirPlayer getNoirPlayerByName(String username) {
        for (NoirPlayer player : NoirXP.players.values()) {
            if (player.getUsername().equalsIgnoreCase(username)) {
                return player;
            }
        }
        return null;
    }

    public static int getHealthFromLevel(int level) {
        return 20 + (level - 1);
    }

    public static String[] getTopTenPlayers() {
        List<NoirPlayer> playerList = new ArrayList<NoirPlayer>(NoirXP.players.values());
        playerList.sort(Collections.reverseOrder(Comparator.comparing(NoirPlayer::getLevel)));
        playerList = playerList.subList(0, playerList.size() < 10 ? playerList.size() : 10);
        String[] formattedList = new String[playerList.size()];

        int index = 0;
        for (NoirPlayer player : playerList) {
            formattedList[index] = String.format("%d: %s %d", index + 1, player.getUsername(), player.getLevel());
            index++;

        }
        return formattedList;
    }

    public static String[] getTopTenPlayersForProfession(String professionName) {
        List<NoirPlayer> playerList = new ArrayList<NoirPlayer>(NoirXP.players.values());

        playerList.sort(Collections.reverseOrder(Comparator.comparing(x -> getProfessionFromString(x, professionName.toLowerCase()).getXp())));
        playerList = playerList.subList(0, playerList.size() < 10 ? playerList.size() : 10);
        String[] formattedList = new String[playerList.size()];

        int index = 0;
        for (NoirPlayer player : playerList) {
            INoirProfession profession = getProfessionFromString(player, professionName.toLowerCase());
            formattedList[index] = String.format("%d: %s %d", index + 1, player.getUsername(), profession.getLevel());
            index++;
        }
        return formattedList;
    }


    public static INoirProfession getProfessionFromString(NoirPlayer player, String playerClass) {
        switch (playerClass.toLowerCase()) {
            case "alchemy":
                return player.alchemy;
            case "building":
                return player.building;
            case "cooking":
                return player.cooking;
            case "farming":
                return player.farming;
            case "fishing":
                return player.fishing;
            case "gathering":
                return player.gathering;
            case "hunting":
                return player.hunting;
            case "mining":
                return player.mining;
            case "smithing":
                return player.smithing;
            case "taming":
                return player.taming;
            default:
                return null;
        }
    }

    public static void setPlayerMaxHealth(String playerId, float maxHealth) {
        Player player = NoirXP.inst().getServer().getPlayer(UUID.fromString(playerId));
        if (player == null) {
            return;
        }
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
    }

}
