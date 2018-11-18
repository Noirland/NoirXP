package nz.co.noirland.noirxp.callbacks;

import nz.co.noirland.noirxp.classes.TameBreedEntity;
import nz.co.noirland.noirxp.database.Database;
import nz.co.noirland.noirxp.helpers.Datamaps;
import nz.co.noirland.noirxp.sqlprocedures.SQLProcedures;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.List;

public class EntityCallbacks {
    /**
     * Adds the taming/breeding data from the database to a memory map
     */
    public static void addTameBreedDataToMap() {
        String sql = SQLProcedures.getAllTameBreedEntities();

        List<HashMap> entityList = Database.executeSQLGet(sql);
        if (entityList.size() == 0) {
            return;
        }
        for (HashMap map : entityList ) {
            EntityType entityType = EntityType.valueOf((String) map.get("entityName"));
            int levelToTame = (int)map.get("levelToTame");
            int levelToBreed = (int)map.get("levelToBreed");
            int tameXp = (int)map.get("tameXp");
            int breedXp = (int)map.get("breedXp");
            TameBreedEntity entity = new TameBreedEntity(entityType, levelToTame, levelToBreed, tameXp, breedXp);

            Datamaps.tameBreedEntityMap.put(entityType, entity);

        }
    }
}
