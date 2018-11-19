package nz.co.noirland.noirxp.database.schema;

import nz.co.noirland.noirxp.NoirXP;
import nz.co.noirland.noirxp.database.queries.XPQuery;
import nz.co.noirland.zephcore.database.Schema;

import java.sql.SQLException;

public class Schema1 implements Schema {

    @Override
    public void run() {
        try {
            createDatabase();
            createPlayerTable();
            createCustomBlockTable();
            createBlockDataTable();
            createTorchDataTable();
            createTameBreedTable();
            createSchemaTable();
        } catch (SQLException e) {
            NoirXP.debug().disable("Unable to setup database!", e);
        }
    }

    private void createSchemaTable() throws SQLException {
        new XPQuery("CREATE TABLE `schema` (`version` TINYINT UNSIGNED);").execute();
        new XPQuery("INSERT INTO `schema` VALUES(1);").execute();
    }

    private void createDatabase() throws SQLException {
        new XPQuery( "CREATE DATABASE IF NOT EXISTS NoirLive;").execute();
    }

    private void createTameBreedTable() throws SQLException {
        new XPQuery("CREATE TABLE IF NOT EXISTS `CustomTameBreedData` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `entityName` varchar(128) NOT NULL,\n" +
                "  `levelToTame` int(11) NOT NULL DEFAULT '0',\n" +
                "  `levelToBreed` int(11) NOT NULL DEFAULT '0',\n" +
                "  `tameXp` int(11) NOT NULL DEFAULT '0',\n" +
                "  `breedXp` int(11) NOT NULL DEFAULT '0',\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  UNIQUE KEY `entityName_UNIQUE` (`entityName`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=latin1;\n").execute();
    }

    private void createTorchDataTable() throws SQLException {
        new XPQuery("CREATE TABLE IF NOT EXISTS `TorchPlacedData` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `world` varchar(128) NOT NULL,\n" +
                "  `x` float NOT NULL,\n" +
                "  `y` float NOT NULL,\n" +
                "  `z` float NOT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n").execute();
    }
    private void createCustomBlockTable() throws SQLException {
        new XPQuery("CREATE TABLE IF NOT EXISTS `CustomBlocks` (\n" +
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
                ") ENGINE=InnoDB AUTO_INCREMENT=760 DEFAULT CHARSET=latin1;\n").execute();
    }

    private void createPlayerTable() throws SQLException {
        new XPQuery("CREATE TABLE IF NOT EXISTS `Player` (\n" +
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
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;\n").execute();
    }

    private void createBlockDataTable() throws SQLException {
        new XPQuery("CREATE TABLE IF NOT EXISTS `PlacedBlockData` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `playerId` varchar(128) DEFAULT NULL,\n" +
                "  `world` varchar(128) NOT NULL,\n" +
                "  `x` float NOT NULL,\n" +
                "  `y` float NOT NULL,\n" +
                "  `z` float NOT NULL,\n" +
                "  `ownsBlock` bit(1) DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=79393 DEFAULT CHARSET=latin1;\n").execute();
    }
}
