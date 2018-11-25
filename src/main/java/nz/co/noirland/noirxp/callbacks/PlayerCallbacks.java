package nz.co.noirland.noirxp.callbacks;

import com.google.common.collect.Streams;
import nz.co.noirland.noirxp.NoirXP;
import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.constants.PlayerClass;
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
import java.util.stream.Collectors;


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

     public static int getLevelFromXp(int xp) {
        SortedMap<Integer, Integer> levelsLesser = xpToLevelMap.headMap(xp+1); // Search for all levels which we have enough XP for
        if(levelsLesser.size() == 0) throw new IllegalArgumentException("No XP mapping found for the given XP amount!");

        return levelsLesser.get(levelsLesser.lastKey());
    }

    public static int getXpFromLevel(int level) {
        if(level > MAX_LEVEL) throw new IllegalArgumentException("Invalid level requested!");
        return levelToXpMap.get(level);
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
        playerList.sort(Collections.reverseOrder(Comparator.comparing(NoirPlayer::getOverallLevel)));
        playerList = playerList.subList(0, playerList.size() < 10 ? playerList.size() : 10);
        String[] formattedList = new String[playerList.size()];

        int index = 0;
        for (NoirPlayer player : playerList) {
            formattedList[index] = String.format("%d: %s %d", index + 1, player.getUsername(), player.getOverallLevel());
            index++;
        }
        return formattedList;
    }

    public static List<String> getTopTenPlayersForProfession(PlayerClass playerClass) {
        /*
          Gets the list of players as a stream
          Sorts them in descending order by total XP for class
          Selects top 10
          Maps index with player, then formats the data for display
         */
        return Streams.mapWithIndex(
                        NoirXP.players.values().stream()
                        .sorted(Collections.reverseOrder(Comparator.comparing(x -> x.getXP(playerClass))))
                        .limit(10),
                (player, index) -> {
                        int level = player.getLevel(playerClass);
                        return String.format("%d: %s %d", index + 1, player.getUsername(), level);
                })
                .collect(Collectors.toList());
    }

    public static void setPlayerMaxHealth(UUID playerId, float maxHealth) {
        Player player = NoirXP.inst().getServer().getPlayer(playerId);
        if (player == null) {
            return;
        }
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
    }

}
