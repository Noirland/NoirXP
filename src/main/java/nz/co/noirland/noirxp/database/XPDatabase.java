package nz.co.noirland.noirxp.database;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import nz.co.noirland.noirxp.NoirXP;
import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.classes.TameBreedEntity;
import nz.co.noirland.noirxp.config.XPConfig;
import nz.co.noirland.noirxp.constants.PlayerClass;
import nz.co.noirland.noirxp.database.queries.ItemXPDataQuery;
import nz.co.noirland.noirxp.database.queries.TameDataQuery;
import nz.co.noirland.noirxp.database.queries.blocklog.AddBlockLogQuery;
import nz.co.noirland.noirxp.database.queries.blocklog.CheckBlockLogQuery;
import nz.co.noirland.noirxp.database.queries.blocklog.RemoveBlockLogQuery;
import nz.co.noirland.noirxp.database.queries.player.AddPlayerQuery;
import nz.co.noirland.noirxp.database.queries.player.GetPlayersQuery;
import nz.co.noirland.noirxp.database.queries.player.ResetPlayerQuery;
import nz.co.noirland.noirxp.database.queries.player.UpdatePlayerQuery;
import nz.co.noirland.noirxp.database.queries.torch.AddTorchQuery;
import nz.co.noirland.noirxp.database.queries.torch.DeleteTorchesQuery;
import nz.co.noirland.noirxp.database.queries.torch.GetTorchesQuery;
import nz.co.noirland.noirxp.database.schema.Schema1;
import nz.co.noirland.noirxp.struct.ItemXPData;
import nz.co.noirland.zephcore.Debug;
import nz.co.noirland.zephcore.database.mysql.MySQLDatabase;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class XPDatabase extends MySQLDatabase {

    private static XPDatabase inst;

    public static XPDatabase inst() {
        if(inst == null) {
            return new XPDatabase();
        }
        return inst;
    }

    private XPDatabase() {
        inst = this;
        schemas.put(1, new Schema1());
    }

    @Override
    public Debug debug() {
        return NoirXP.debug();
    }

    @Override
    protected String getHost() {
        return XPConfig.inst().getDBHost();
    }

    @Override
    protected int getPort() {
        return XPConfig.inst().getDBPort();
    }

    @Override
    protected String getDatabase() {
        return "NoirLive";
    }

    @Override
    protected String getUsername() {
        return XPConfig.inst().getDBUser();
    }

    @Override
    protected String getPassword() {
        return XPConfig.inst().getDBPassword();
    }

    @Override
    public String getPrefix() {
        return "";
    }

    public boolean hasBeenPlaced(Location location) {
        try {
            List<Map<String, Object>> result = new CheckBlockLogQuery(location).executeQuery();
            return ((float) result.get(0).get("COUNT")) > 0;
        } catch (Exception e) {
            debug().warning("Failed to query DB:", e);
            return false;
        }
    }

    public void replaceTorches(Set<Location> torchSet) {
        new DeleteTorchesQuery().executeAsync();

        for(Location torch : torchSet) {
            new AddTorchQuery(torch).executeAsync();
        }
    }

    public Set<Location> getTorches() {
        Set<Location> ret = Sets.newHashSet();
        try {
            List<Map<String, Object>> result = new GetTorchesQuery().executeQuery();

            for(Map<String, Object> entry : result) {
                String world = (String) entry.get("world");
                float x = (float) entry.get("x");
                float y = (float) entry.get("y");
                float z = (float) entry.get("z");
                Location location = new Location(NoirXP.inst().getServer().getWorld(world), x, y, z);
                ret.add(location);
            }
        } catch (SQLException e) {
            debug().warning("Failed to query DB:", e);
        }
        return ret;
    }

    public void addBlockLog(Location location, UUID player, boolean ownsBlock) {
        new AddBlockLogQuery(location,player,ownsBlock).executeAsync();
    }

    public void removeBlockLog(Location location) {
        new RemoveBlockLogQuery(location).executeAsync();
    }

    public void addPlayer(UUID player) {
        new AddPlayerQuery(player).executeAsync();
    }

    public Optional<ItemXPData> getCustomBlock(Material type) {
        return getCustomBlock(type.toString());
    }

    public Optional<ItemXPData> getCustomBlock(String type) {
        List<Map<String, Object>> result;
        try {
            result = new ItemXPDataQuery(type).executeQuery();
        } catch (SQLException e) {
            debug().warning("Unable to query DB:", e);
            return Optional.empty();
        }

        if(result.size() == 0) return Optional.empty();

        Map<String, Object> data = result.get(0);
        String blockType = (String) data.get("name");
        int levelToBreak = (int) data.get("levelToBreak");
        int levelToPlace = (int) data.get("levelToPlace");
        int levelToCreate = (int) data.get("levelToCreate");
        int levelToUse = (int) data.get("levelToUse");
        int placeXP = (int) data.get("placeXP");
        int breakXP = (int) data.get("breakXP");
        int createXP = (int) data.get("createXP");
        PlayerClass classType = PlayerClass.valueOf((String) data.get("playerClass"));

        return Optional.of(new ItemXPData(blockType, levelToBreak, levelToPlace, levelToCreate, levelToUse, placeXP, breakXP, createXP, classType));
    }

    public void resetPlayer(UUID player) {
        new ResetPlayerQuery(player).executeAsync();
    }

    public Map<String, NoirPlayer> getAllPlayers() {
        Map<String, NoirPlayer> ret = Maps.newHashMap();
        List<Map<String, Object>> result = null;
        try {
            result = new GetPlayersQuery().executeQuery();
        } catch (SQLException e) {
            debug().disable("Unable to query list of players!", e);
            return null;
        }

        for (Map<String, Object> map : result) {
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

            ret.put(playerId, player);
        }

        return ret;
    }

    public Map<EntityType, TameBreedEntity> getTamingData() {
        Map<EntityType, TameBreedEntity> ret = Maps.newHashMap();

        List<Map<String, Object>> result;
        try {
            result = new TameDataQuery().executeQuery();
        } catch (SQLException e) {
            debug().warning("Unable to get taming data", e);
            return ret;
        }

        for (Map<String, Object> entry : result) {
            EntityType entityType = EntityType.valueOf((String) entry.get("entityName"));
            int levelToTame = (int)entry.get("levelToTame");
            int levelToBreed = (int)entry.get("levelToBreed");
            int tameXp = (int)entry.get("tameXp");
            int breedXp = (int)entry.get("breedXp");
            TameBreedEntity entity = new TameBreedEntity(entityType, levelToTame, levelToBreed, tameXp, breedXp);

            ret.put(entityType, entity);
        }

        return ret;
    }

    public void saveUserData(Map<String, NoirPlayer> data) {
        for (NoirPlayer player : data.values()) {
            new UpdatePlayerQuery(player).executeAsync();
        }
    }
}
