package NoirLeveling.Callbacks;

import NoirLeveling.Classes.NoirPlayer;
import NoirLeveling.Classes.TameBreedEntity;
import NoirLeveling.Database.Database;
import NoirLeveling.Helpers.Datamaps;
import NoirLeveling.Main;
import NoirLeveling.SQLProcedures.SQLProcedures;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.List;

public class EntityCallbacks {
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
