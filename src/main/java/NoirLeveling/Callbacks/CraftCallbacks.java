package NoirLeveling.Callbacks;

import NoirLeveling.Database.Database;
import NoirLeveling.SQLProcedures.SQLProcedures;

import java.util.HashMap;
import java.util.List;

public class CraftCallbacks {

    public static List<HashMap> getCustomBlock(String blockName) {
        String sql = SQLProcedures.getCustomBlock(blockName);

        List<HashMap> resultSet = Database.executeSQLGet(sql);
        if (resultSet == null || resultSet.size() == 0) {
            return null;
        }
        return resultSet;
    }

}
