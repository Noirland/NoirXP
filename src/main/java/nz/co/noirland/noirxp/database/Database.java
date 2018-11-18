package nz.co.noirland.noirxp.database;

import nz.co.noirland.noirxp.Main;
import nz.co.noirland.noirxp.sqlprocedures.SQLProcedures;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Database {

    public static List<HashMap> executeSQLGet(String sql) {
        try {
            Connection conn = DriverManager.getConnection(Main.url, Main.username, Main.password);
            Statement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery(sql);
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            int columns = resultSetMetaData.getColumnCount();
            List list = new ArrayList();
            while (rs.next()) {
                HashMap row = new HashMap(columns);
                for (int i = 1; i <= columns; ++i) {
                    row.put(resultSetMetaData.getColumnName(i), rs.getObject(i));

                }
                list.add(row);
            }
            rs.close();
            statement.close();
            conn.close();
            return list;
        }
        catch (SQLException e) {
            return new ArrayList<HashMap>();
        }
    }

    public static void executeSQLUpdateDelete(String sql) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Connection conn = DriverManager.getConnection(Main.url, Main.username, Main.password);
                    PreparedStatement statement = conn.prepareStatement(sql);
                    int result = statement.executeUpdate(sql);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }.runTaskAsynchronously(Main.plugin);


    }

    public static void createDatabase() throws SQLException {
        executeSQLUpdateDelete(SQLProcedures.createDatabase());
        executeSQLUpdateDelete(SQLProcedures.createPlayerTable());
        executeSQLUpdateDelete(SQLProcedures.createCustomBlockTable());
        executeSQLUpdateDelete(SQLProcedures.createBlockDataTable());
        executeSQLUpdateDelete(SQLProcedures.createTorchDataTable());
        executeSQLUpdateDelete(SQLProcedures.createTameBreedTable());
    }
}
