package nz.co.noirland.noirxp.callbacks;

import nz.co.noirland.noirxp.NoirXP;
import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.config.UserdataConfig;
import nz.co.noirland.noirxp.constants.PlayerClass;
import nz.co.noirland.noirxp.helpers.PlayerClassConverter;
import nz.co.noirland.noirxp.interfaces.INoirProfession;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;


public final class PlayerCallbacks {

    public static final int MAX_LEVEL = 55;

    /**
     * Mapping from level, to the total XP required to reach that level
     */
    private static final Map<Integer, Integer> levelToXpMap = new HashMap<>();
    private static final SortedMap<Integer, Integer> xpToLevelMap = new TreeMap<>();

    // Calculate these once on startup, rather than all the time
    static {
        levelToXpMap.put(1, 0);
        xpToLevelMap.put(0, 1);
        for(int lvl = 2; lvl <= MAX_LEVEL; lvl++) {

            double result;
            if(lvl <= 50) {
                // The formula for xp is (8x^3/3 + 8x^2 - 32x/3)
                result = (8 * Math.pow(lvl, 3)) / 3 + (8 * Math.pow(lvl, 2)) - ((32 * lvl) / 3);
            } else {
                result = levelToXpMap.get(lvl-1)*2;
            }

            int xpRequired = (int) Math.round(result);

            levelToXpMap.put(lvl, xpRequired);
            xpToLevelMap.put(xpRequired, lvl);
        }
    }

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
        SortedMap<Integer, Integer> levelsLesser = xpToLevelMap.headMap(xp+1); // Search for all levels which are less than or equal to current XP
        if(levelsLesser.size() == 0) throw new IllegalArgumentException("No XP mapping found for the given XP amount!");

        return levelsLesser.get(levelsLesser.lastKey());
    }

    public static int GetXpFromLevel(int level) {
        if(level > MAX_LEVEL) throw new IllegalArgumentException("Invalid level requested!");
        return levelToXpMap.get(level);
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

        if (newClassLevel <= MAX_LEVEL) {
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
                    Bukkit.getConsoleSender().sendMessage(String.format("[NOIRXP] %s level increased to %s for %s", player.getUsername(),
                            newClassLevel, playerClass.toString()));
                }
            }
        }
        int currentLevel = player.getLevel();

        if (currentLevel < MAX_LEVEL) {
            player.addXp(xpGained);
            int newLevel = player.getLevel();

            if (newLevel > currentLevel) {
                player.getBukkitPlayer().sendMessage("Your overall level just " +
                        "increased to " + ChatColor.GOLD + newLevel + ChatColor.WHITE + "!");
                Bukkit.getConsoleSender().sendMessage(String.format("[NOIRXP] %s overall level increased to %s ", player.getUsername(),
                        newLevel));
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
        List<NoirPlayer> playerList = new ArrayList<>(NoirXP.players.values());
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
        List<NoirPlayer> playerList = new ArrayList<>(NoirXP.players.values());

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
