package nz.co.noirland.noirxp.database.schema;

import nz.co.noirland.noirxp.NoirXP;
import nz.co.noirland.noirxp.database.queries.XPQuery;
import nz.co.noirland.zephcore.database.Schema;

import java.sql.SQLException;

public class Schema2 implements Schema {

    @Override
    public void run() {
        try {
            changeColumns();
            addChunks();
            addChunkIndex();
            updateTorches();
            updateSchema();
        } catch (SQLException e) {
            NoirXP.debug().disable("Unable to update database version! " + e.getMessage(), e);
        }
    }

    private void updateSchema() throws SQLException {
        new XPQuery("UPDATE `{PREFIX}_schema` SET `version` = 2").execute();
    }

    private void changeColumns() throws SQLException {
        new XPQuery("ALTER TABLE `PlacedBlockData` \n"
                + "DROP COLUMN `ownsBlock`,\n"
                + "DROP COLUMN `playerId`,\n"
                + "ADD COLUMN `chunkX` INT NULL AFTER `world`,\n"
                + "ADD COLUMN `chunkZ` INT NULL AFTER `chunkX`,\n"
                + "CHANGE COLUMN `x` `x` INT NOT NULL ,\n"
                + "CHANGE COLUMN `y` `y` INT NOT NULL ,\n"
                + "CHANGE COLUMN `z` `z` INT NOT NULL ;").execute();
    }

    private void addChunks() throws SQLException {
        new XPQuery("UPDATE `PlacedBlockData` \n"
                + "SET chunkX=FLOOR(x / 16), chunkZ=FLOOR(z / 16);").execute();
    }

    private void addChunkIndex() throws SQLException {
        new XPQuery("ALTER TABLE `PlacedBlockData` \n"
                + "CHANGE COLUMN `chunkX` `chunkX` INT(11) NOT NULL ,\n"
                + "CHANGE COLUMN `chunkZ` `chunkZ` INT(11) NOT NULL ,\n"
                + "ADD INDEX `CHUNK` (`world` ASC, `chunkX` ASC, `chunkZ` ASC);\n"
                + ";").execute();
    }

    private void updateTorches() throws SQLException {
        new XPQuery("ALTER TABLE `TorchPlacedData` \n"
                + "CHANGE COLUMN `x` `x` INT NOT NULL ,\n"
                + "CHANGE COLUMN `y` `y` INT NOT NULL ,\n"
                + "CHANGE COLUMN `z` `z` INT NOT NULL ;").execute();
    }
}
