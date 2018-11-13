package NoirLeveling.Callbacks;

import NoirLeveling.Classes.NoirPlayer;
import NoirLeveling.Constants.PlayerClass;
import NoirLeveling.Database.Database;
import NoirLeveling.Helpers.PlayerClassConverter;
import NoirLeveling.Interfaces.INoirProfession;
import NoirLeveling.Main;
import NoirLeveling.SQLProcedures.SQLProcedures;
import NoirLeveling.Structs.PlayerXpClassPair;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;


public final class PlayerCallbacks {

    public static int getPlayerLevel(String playerId) {
        NoirPlayer player = Main.players.get(playerId);
        return player.getLevel();
    }

    public static int getPlayerXpForClass(String playerId, PlayerClass playerClass) {
        NoirPlayer player = Main.players.get(playerId);
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

    public static int getPlayerTotalXp(String playerId) {
        int totalXp = Main.players.get(playerId).getXp();
        return totalXp;
    }

    public static List<HashMap> getPlayerXpClasses(Player player) {
        String playerXpSql = SQLProcedures.getPlayerXpClasses(player.getUniqueId().toString());
        List<HashMap> xpClasses = Database.executeSQLGet(playerXpSql);
        return xpClasses;
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


    public static PlayerXpClassPair getHighestXpPlayerClass(Player player) {
        String sql = SQLProcedures.getPlayerXpClasses(player.getUniqueId().toString());
        List<HashMap> dataList = Database.executeSQLGet(sql);
        int alchemyXp = (int)dataList.get(0).get("alchemyXp");
        int buildingXp = (int)dataList.get(0).get("buildingXp");
        int cookingXp = (int)dataList.get(0).get("cookingXp");
        int farmingXp = (int)dataList.get(0).get("farmingXp");
        int fishingXp = (int)dataList.get(0).get("fishingXp");
        int gatheringXp = (int)dataList.get(0).get("gatheringXp");
        int huntingXp = (int)dataList.get(0).get("huntingXp");
        int miningXp = (int)dataList.get(0).get("miningXp");
        int smithingXp = (int)dataList.get(0).get("smithingXp");
        int tamingXp = (int)dataList.get(0).get("tamingXp");
        int highest = NumberUtils.max(new int[] {alchemyXp, buildingXp, cookingXp, farmingXp, fishingXp, gatheringXp, huntingXp, miningXp, smithingXp, tamingXp});

        PlayerXpClassPair playerXpClassPair = new PlayerXpClassPair();
        playerXpClassPair.classXp = highest;
        if (highest == 0) {
            playerXpClassPair.playerClass = PlayerClass.GYPSY;
        }
        else if (highest == alchemyXp) {
            playerXpClassPair.playerClass = PlayerClass.ALCHEMY;
        }
        else if (highest == buildingXp) {
            playerXpClassPair.playerClass = PlayerClass.BUILDING;
        }
        else if (highest == cookingXp) {
            playerXpClassPair.playerClass = PlayerClass.COOKING;
        }
        else if (highest == farmingXp) {
            playerXpClassPair.playerClass = PlayerClass.FARMING;
        }
        else if (highest == fishingXp) {
            playerXpClassPair.playerClass = PlayerClass.FISHING;
        }
        else if (highest == gatheringXp) {
            playerXpClassPair.playerClass = PlayerClass.GATHERING;
        }
        else if (highest == huntingXp) {
            playerXpClassPair.playerClass = PlayerClass.HUNTING;
        }
        else if (highest == miningXp) {
            playerXpClassPair.playerClass = PlayerClass.MINING;
        }
        else if (highest == smithingXp) {
            playerXpClassPair.playerClass = PlayerClass.SMITHING;
        }
        else if (highest == tamingXp) {
            playerXpClassPair.playerClass = PlayerClass.TAMING;
        }
        return playerXpClassPair;

    }

    /**
     * Call this when player xp is about to change, BEFORE any updates to the database
     * @param playerId The player to check
     * @param playerClass The class that gained the xp
     * @param xpGained The amount of xp the player is about to gain
     */
    public static void xpGained(String playerId, PlayerClass playerClass, int xpGained) {
        NoirPlayer player = Main.players.get(playerId);
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

            if (isPlayerVerboseEnabled(player.getBukkitPlayer())) {
                player.getBukkitPlayer().sendMessage("+" + xpGained + " " + PlayerClassConverter.PlayerClassToString(playerClass));
            }

            if (playerClass != PlayerClass.GENERAL) {
                if (player.isLevelUp(currentClassXp, newClassXp)) {
                    player.getBukkitPlayer().sendMessage("Your " + PlayerClassConverter.PlayerClassToString(playerClass) + " level just " +
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

    public static boolean isPlayerLevelingEnabled(Player player) {
        File file = new File(Main.userdataFilePath);
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = configuration.getConfigurationSection(player.getUniqueId().toString());
        if (section.getBoolean("leveling") == false) {
            return false;
        }

        return true;
    }

    public static boolean isPlayerVerboseEnabled(Player player) {
        File file = new File(Main.userdataFilePath);
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = configuration.getConfigurationSection(player.getUniqueId().toString());
        if (section.getBoolean("verbose") == true) {
            return true;
        }

        return false;
    }

    public static void addPlayersToMap() {

        String sql = SQLProcedures.getAllPlayers();

        List<HashMap> playerList = Database.executeSQLGet(sql);
        if (playerList.size() == 0) {
            return;
        }
        for(HashMap map : playerList ) {
            String playerId = (String)map.get("playerId");
            NoirPlayer player = new NoirPlayer(playerId);
            player.setUsername((String)map.get("username"));
            player.setCurrentHealth((float)map.get("currentHealth"));
            player.setMaxHealth((float)map.get("maxHealth"));
            player.alchemy.setXp((int)map.get("alchemyXp"));
            player.building.setXp((int)map.get("buildingXp"));
            player.cooking.setXp((int)map.get("cookingXp"));
            player.farming.setXp((int)map.get("farmingXp"));
            player.fishing.setXp((int)map.get("fishingXp"));
            player.gathering.setXp((int)map.get("gatheringXp"));
            player.hunting.setXp((int)map.get("huntingXp"));
            player.mining.setXp((int)map.get("miningXp"));
            player.smithing.setXp((int)map.get("smithingXp"));
            player.taming.setXp((int)map.get("tamingXp"));
            player.setXp((int)map.get("totalXp"));

            Main.players.put(playerId, player);

        }

    }

    public static NoirPlayer getNoirPlayerByName(String username) {
        for (NoirPlayer player : Main.players.values()) {
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
        List<NoirPlayer> playerList = new ArrayList<NoirPlayer>(Main.players.values());
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
        List<NoirPlayer> playerList = new ArrayList<NoirPlayer>(Main.players.values());

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
        Player player = Main.server.getPlayer(UUID.fromString(playerId));
        if (player == null) {
            return;
        }
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
    }

    public static void startUpdatePlayerTableTimer() {
        Main.server.getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
            @Override
            public void run() {
                try {
                    Connection conn;
                    conn = DriverManager.getConnection(Main.url, Main.username, Main.password);
                    String sql = "UPDATE Player SET username = ?, alchemyXp = ?, buildingXp = ?, " +
                            "cookingXp = ?, farmingXp = ?, fishingXp = ?, gatheringXp = ?, huntingXp = ?, miningXp = ?, " +
                            "smithingXp = ?, tamingXp = ?, totalXp = ?, currentHealth = ?, maxHealth = ? WHERE playerId = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    for (NoirPlayer player : Main.players.values()) {
                        stmt.setString(1, player.getUsername());
                        stmt.setInt(2, player.alchemy.getXp());
                        stmt.setInt(3, player.building.getXp());
                        stmt.setInt(4, player.cooking.getXp());
                        stmt.setInt(5, player.farming.getXp());
                        stmt.setInt(6, player.fishing.getXp());
                        stmt.setInt(7, player.gathering.getXp());
                        stmt.setInt(8, player.hunting.getXp());
                        stmt.setInt(9, player.mining.getXp());
                        stmt.setInt(10, player.smithing.getXp());
                        stmt.setInt(11, player.taming.getXp());
                        stmt.setInt(12, player.getXp());
                        stmt.setFloat(13, player.getCurrentHealth());
                        stmt.setFloat(14, player.getMaxHealth());
                        stmt.setString(15, player.getUniqueId());
                        stmt.addBatch();

                    }
                    stmt.executeBatch();
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }, 0L, 20L * 60L * 10L);

    }

}
