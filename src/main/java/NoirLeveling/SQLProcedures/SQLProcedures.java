package NoirLeveling.SQLProcedures;

import NoirLeveling.Constants.PlayerClass;
import org.bukkit.Location;

public class SQLProcedures {
    public static String createDatabase() {
       return  "CREATE DATABASE IF NOT EXISTS NoirLive;";
    }

    public static String createTameBreedTable() {
        return "CREATE TABLE IF NOT EXISTS `CustomTameBreedData` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `entityName` varchar(128) NOT NULL,\n" +
                "  `levelToTame` int(11) NOT NULL DEFAULT '0',\n" +
                "  `levelToBreed` int(11) NOT NULL DEFAULT '0',\n" +
                "  `tameXp` int(11) NOT NULL DEFAULT '0',\n" +
                "  `breedXp` int(11) NOT NULL DEFAULT '0',\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  UNIQUE KEY `entityName_UNIQUE` (`entityName`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=latin1;\n";
    }

    public static String createTorchDataTable() {
        return "CREATE TABLE IF NOT EXISTS `TorchPlacedData` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `world` varchar(128) NOT NULL,\n" +
                "  `x` float NOT NULL,\n" +
                "  `y` float NOT NULL,\n" +
                "  `z` float NOT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n";
    }
    public static String createCustomBlockTable() {
        return "CREATE TABLE IF NOT EXISTS `CustomBlocks` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `name` varchar(128) DEFAULT NULL,\n" +
                "  `baseBlockName` varchar(128) DEFAULT NULL,\n" +
                "  `levelToBreak` int(11) DEFAULT '0',\n" +
                "  `levelToPlace` int(11) DEFAULT '0',\n" +
                "  `levelToCreate` int(11) DEFAULT '0',\n" +
                "  `levelToUse` int(11) DEFAULT '0',\n" +
                "  `placeXp` int(11) DEFAULT '0',\n" +
                "  `breakXp` int(11) DEFAULT '0',\n" +
                "  `createXp` int(11) DEFAULT '0',\n" +
                "  `playerClass` varchar(128) DEFAULT 'GENERAL',\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  UNIQUE KEY `id_UNIQUE` (`id`),\n" +
                "  UNIQUE KEY `name_UNIQUE` (`name`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=760 DEFAULT CHARSET=latin1;\n";
    }

    public static String createPlayerTable() {
        return "CREATE TABLE IF NOT EXISTS `Player` (\n" +
                "  `playerId` varchar(256) NOT NULL,\n" +
                "  `username` varchar(256) DEFAULT NULL,\n" +
                "  `alchemyXp` int(11) NOT NULL DEFAULT '0',\n" +
                "  `buildingXp` int(11) NOT NULL DEFAULT '0',\n" +
                "  `cookingXp` int(11) NOT NULL DEFAULT '0',\n" +
                "  `farmingXp` int(11) NOT NULL DEFAULT '0',\n" +
                "  `fishingXp` int(11) NOT NULL DEFAULT '0',\n" +
                "  `gatheringXp` int(11) NOT NULL DEFAULT '0',\n" +
                "  `huntingXp` int(11) NOT NULL DEFAULT '0',\n" +
                "  `miningXp` int(11) NOT NULL DEFAULT '0',\n" +
                "  `smithingXp` int(11) NOT NULL DEFAULT '0',\n" +
                "  `tamingXp` int(11) NOT NULL DEFAULT '0',\n" +
                "  `totalXp` int(11) NOT NULL DEFAULT '0',\n" +
                "  `level` int(11) NOT NULL DEFAULT '1',\n" +
                "  `currentHealth` float NOT NULL DEFAULT '20',\n" +
                "  `maxHealth` float NOT NULL DEFAULT '20',\n" +
                "  PRIMARY KEY (`playerId`),\n" +
                "  UNIQUE KEY `playerId` (`playerId`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n";
    }

    public static String createBlockDataTable() {
        return "CREATE TABLE IF NOT EXISTS `PlacedBlockData` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `playerId` varchar(128) DEFAULT NULL,\n" +
                "  `world` varchar(128) NOT NULL,\n" +
                "  `x` float NOT NULL,\n" +
                "  `y` float NOT NULL,\n" +
                "  `z` float NOT NULL,\n" +
                "  `ownsBlock` bit(1) DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=79393 DEFAULT CHARSET=latin1;\n";
    }


    public static String getBlockDataLog(Location location) {
        return String.format("SELECT world, x, y, z FROM PlacedBlockData WHERE world = '%s' AND x = %s AND y = %s AND z = %s;",
                location.getWorld().getName(),
                location.getX(), location.getY(), location.getZ());
    }

    public static String getBlockDataLog(Location location, String playerId) {
        return String.format("SELECT world, x, y, z FROM PlacedBlockData WHERE world = '%s' AND playerId = '%s' AND x = %s AND y = %s AND z = %s;",
                playerId,
                location.getWorld().getName(),
                location.getX(), location.getY(), location.getZ());
    }

    public static String getBlockDataLog(Location location, String playerId, boolean ownsBlock) {
        return String.format("SELECT world, x, y, z FROM PlacedBlockData WHERE world = '%s' AND playerId = '%s' AND x = %s AND y = %s AND z = %s AND ownsBlock = '%b';",
                playerId,
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                ownsBlock);
    }

    public static String getTorchPlacedData(Location location) {
        return String.format("SELECT world, x, y, z FROM TorchPlacedData WHERE world = '%s' AND x = %s AND y = %s AND z = %s;",
                location.getWorld().getName(),
                location.getX(), location.getY(), location.getZ());
    }

    public static String deleteTorchData() {
        return "DELETE FROM TorchPlacedData;";
    }

    public static String InsertIntoTorchDataTable(Location location) {
        return String.format("INSERT INTO TorchPlacedData (world, x, y, z) VALUES ('%s', %.2f, %.2f, %.2f)",
                location.getWorld().getName(),
                location.getX(),
                location.getY(), location.getZ());
    }

    public static String getTorchData() {
        return "SELECT * FROM TorchPlacedData";
    }

    public static String insertIntoBlockDataLogTable(Location location, String playerId, boolean ownsBlock) {
        return String.format("INSERT INTO PlacedBlockData (playerId, world, x, y, z, ownsBlock) VALUES ('%s', '%s', %.2f, %.2f, %.2f, %b)",
                playerId,
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                ownsBlock);
    }

    public static String deleteFromBlockDataTable(Location location) {
        return String.format("DELETE FROM PlacedBlockData WHERE world = '%s' AND x = %.2f AND y = %.2f AND z = %.2f",
                location.getWorld().getName(),
                location.getX(), location.getY(), location.getZ());
    }

    public static String insertIntoPlayerTable(String playerId) {
        return String.format("INSERT IGNORE INTO Player (playerId) VALUES ('%s')", playerId);
    }

    public static String updatePlayerTable2Xp(String playerId, int xp, PlayerClass playerClass) {
        switch (playerClass) {
            case FARMING:
                return String.format("UPDATE Player SET farmingXp = farmingXp + %1$d, totalXp = totalXp + %1$d WHERE playerId = '%2$s'", xp, playerId);
            case COOKING:
                return String.format("UPDATE Player SET cookingXp = cookingXp + %1$d, totalXp = totalXp + %1$d WHERE playerId = '%2$s'", xp, playerId);
            case ALCHEMY:
                return String.format("UPDATE Player SET alchemyXp = alchemyXp + %1$d, totalXp = totalXp + %1$d WHERE playerId = '%2$s'", xp, playerId);
            case TAMING:
                return String.format("UPDATE Player SET tamingXp = tamingXp + %1$d, totalXp = totalXp + %1$d WHERE playerId = '%2$s'", xp, playerId);
            case MINING:
                return String.format("UPDATE Player SET miningXp = miningXp + %1$d, totalXp = totalXp + %1$d WHERE playerId = '%2$s'", xp, playerId);
            case FISHING:
                return String.format("UPDATE Player SET fishingXp = fishingXp + %1$d, totalXp = totalXp + %1$d WHERE playerId = '%2$s'", xp, playerId);
            case HUNTING:
                return String.format("UPDATE Player SET huntingXp = huntingXp + %1$d, totalXp = totalXp + %1$d WHERE playerId = '%2$s'", xp, playerId);
            case BUILDING:
                return String.format("UPDATE Player SET buildingXp = buildingXp + %1$d, totalXp = totalXp + %1$d WHERE playerId = '%2$s'", xp, playerId);
            case SMITHING:
                return String.format("UPDATE Player SET smithingXp = smithingXp + %1$d, totalXp = totalXp + %1$d WHERE playerId = '%2$s'", xp, playerId);
            case GATHERING:
                return String.format("UPDATE Player SET gatheringXp = gatheringXp + %1$d, totalXp = totalXp + %1$d WHERE playerId = '%2$s'", xp, playerId);
            case GYPSY:
                return null;
            default:
                return null;
        }

    }

    public static String getHighestXpClass(String playerId) {
        return String.format("SELECT GREATEST(alchemyXp, buildingXp, cookingXp, farmingXp, fishingXp, " +
                "gatheringXp, huntingXp, miningXp, smithingXp, tamingXp) highestXp FROM Player " +
                "WHERE playerId = '%s'", playerId);
    }

    public static String getPlayerXpClasses(String playerId) {
        return String.format("SELECT alchemyXp, buildingXp, cookingXp, farmingXp, fishingXp, " +
                "gatheringXp, huntingXp, miningXp, smithingXp, tamingXp FROM Player " +
                "WHERE playerId = '%s'", playerId);
    }

    public static String getPlayerXp(String playerId) {
        return String.format("SELECT totalXp FROM Player " +
                "WHERE playerId = '%s'", playerId);
    }

    public static String getPlayerLevel(String playerId) {
        return String.format("SELECT level FROM Player " +
                "WHERE playerId = '%s'", playerId);
    }

    public static String getCustomBlock(String blockName) {

        return String.format("SELECT * FROM CustomBlocks WHERE name = '%s'", blockName);

    }

    public static String resetPlayerXp(String playerId) {
        return String.format("UPDATE Player SET alchemyXp = 0, buildingXp = 0, cookingXp = 0, " +
                "farmingXp = 0, fishingXp = 0, gatheringXp = 0, huntingXp = 0, miningXp = 0, " +
                "smithingXp = 0, tamingXp = 0, totalXp = 0 WHERE playerId = '%s'", playerId);
    }

    public static String getAllPlayers() {
        return "SELECT * FROM Player";
    }

    public static String getAllTameBreedEntities() {
        return "SELECT entityName, levelToTame, levelToBreed, tameXp, breedXp FROM NoirLive.CustomTameBreedData;";
    }
}
