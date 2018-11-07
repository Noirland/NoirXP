package NoirLeveling.Callbacks;

import NoirLeveling.Classes.NoirPlayer;
import NoirLeveling.Constants.PlayerClass;
import NoirLeveling.Database.Database;
import NoirLeveling.Helpers.Datamaps;
import NoirLeveling.Main;
import NoirLeveling.SQLProcedures.SQLProcedures;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

public class BlockCallbacks {
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
        bukkitPlayer.sendMessage(command + bukkitPlayer.getName() + ChatColor.WHITE);


    }

    public static boolean hasBlockGivenXp(Location location) {
        String locationSql = SQLProcedures.getBlockDataLog(location);
        List<HashMap> result = Database.executeSQLGet(locationSql);
        if (result.size() > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean addBlockLocationToLogTable(Location location) {
        String sql = SQLProcedures.insertIntoBlockDataLogTable(location);
        try {
            Database.executeSQLUpdateDelete(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void addTorchDataToMap() {
        String sql = SQLProcedures.getTorchData();

        List<HashMap> torchList = Database.executeSQLGet(sql);
        if (torchList.size() == 0) {
            return;
        }
        for(HashMap hashMap : torchList ) {
            String worldName = (String) hashMap.get("world");
            float x = (float) hashMap.get("x");
            float y = (float) hashMap.get("y");
            float z = (float) hashMap.get("z");
            Location location = new Location(Main.server.getWorld(worldName), x, y, z);
            Datamaps.torchSet.add(location);

        }
    }

    public static void replaceTorchDataTable() {
        try {
            Database.executeSQLUpdateDelete(SQLProcedures.deleteTorchData());
            Connection conn;
            conn = DriverManager.getConnection(Main.url, Main.username, Main.password);
            String sql = "INSERT INTO TorchPlacedData (world, x, y, z) VALUES (?, ?, ?, ?);";
            PreparedStatement stmt = conn.prepareStatement(sql);
            for (Location location : Datamaps.torchSet) {
                stmt.setString(1, location.getWorld().getName());
                stmt.setFloat(2, (float) location.getX());
                stmt.setFloat(3, (float) location.getY());
                stmt.setFloat(4, (float) location.getZ());
                stmt.addBatch();

            }
            stmt.executeBatch();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<HashMap> getCustomBlock(String blockName) {
        String sql = SQLProcedures.getCustomBlock(blockName);

        List<HashMap> resultSet = Database.executeSQLGet(sql);
        if (resultSet == null || resultSet.size() == 0) {
            return null;
        }
        return resultSet;
    }
}
