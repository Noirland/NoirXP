package nz.co.noirland.noirxp.database;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import nz.co.noirland.noirxp.NoirXP;
import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.classes.TameBreedEntity;
import nz.co.noirland.noirxp.config.XPConfig;
import nz.co.noirland.noirxp.constants.PlayerClass;
import nz.co.noirland.noirxp.database.queries.GetCustomBlocksQuery;
import nz.co.noirland.noirxp.database.queries.TameDataQuery;
import nz.co.noirland.noirxp.database.queries.XPQuery;
import nz.co.noirland.noirxp.database.queries.blocklog.AddBlockLogQuery;
import nz.co.noirland.noirxp.database.queries.blocklog.GetBlockLogQuery;
import nz.co.noirland.noirxp.database.queries.blocklog.PruneBlockLogQuery;
import nz.co.noirland.noirxp.database.queries.blocklog.RemoveBlockLogQuery;
import nz.co.noirland.noirxp.database.queries.player.AddPlayerQuery;
import nz.co.noirland.noirxp.database.queries.player.GetPlayersQuery;
import nz.co.noirland.noirxp.database.queries.player.ResetPlayerQuery;
import nz.co.noirland.noirxp.database.queries.player.UpdatePlayerQuery;
import nz.co.noirland.noirxp.database.queries.torch.AddTorchQuery;
import nz.co.noirland.noirxp.database.queries.torch.DeleteTorchQuery;
import nz.co.noirland.noirxp.database.queries.torch.GetTorchesQuery;
import nz.co.noirland.noirxp.database.schema.Schema1;
import nz.co.noirland.noirxp.database.schema.Schema2;
import nz.co.noirland.noirxp.helpers.Datamaps;
import nz.co.noirland.noirxp.struct.ItemXPData;
import nz.co.noirland.zephcore.Callback;
import nz.co.noirland.zephcore.Debug;
import nz.co.noirland.zephcore.database.mysql.MySQLDatabase;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
        schemas.put(2, new Schema2());
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

    public void removeTorch(Location location) {
        new DeleteTorchQuery(location).executeAsync();
    }

    public Set<Location> getTorches() {
        Set<Location> ret = Sets.newHashSet();
        try {
            List<Map<String, Object>> result = new GetTorchesQuery().executeQuery();

            for(Map<String, Object> entry : result) {
                ret.add(constructLocation(entry));
            }
        } catch (SQLException e) {
            debug().warning("Failed to query DB:", e);
        }
        return ret;
    }

    public void removeBlockLog(Location location) {
        new RemoveBlockLogQuery(location).executeAsync();
    }

    public void addPlayer(UUID player) {
        new AddPlayerQuery(player).executeAsync();
    }

    public Map<String, ItemXPData> loadCustomBlocks() {
        Map<String, ItemXPData> ret = Maps.newHashMap();

        List<Map<String, Object>> result;
        try {
            result = new GetCustomBlocksQuery().executeQuery();
        } catch (SQLException e) {
            debug().warning("Unable to query custom blocks:", e);
            return ret;
        }

        for(Map<String, Object> data : result) {
            String blockType = (String) data.get("name");
            int levelToBreak = (int) data.get("levelToBreak");
            int levelToPlace = (int) data.get("levelToPlace");
            int levelToCreate = (int) data.get("levelToCreate");
            int levelToUse = (int) data.get("levelToUse");
            int placeXP = (int) data.get("placeXp");
            int breakXP = (int) data.get("breakXp");
            int createXP = (int) data.get("createXp");
            PlayerClass classType = PlayerClass.valueOf((String) data.get("playerClass"));

            ret.put(blockType,
                    new ItemXPData(blockType, levelToBreak, levelToPlace, levelToCreate, levelToUse, placeXP, breakXP, createXP, classType));
        }

        return ret;
    }

    public void resetPlayer(UUID player) {
        new ResetPlayerQuery(player).executeAsync();
    }

    public Map<String, NoirPlayer> getAllPlayers() {
        Map<String, NoirPlayer> ret = Maps.newHashMap();
        List<Map<String, Object>> result;
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
        try {
            new UpdatePlayerQuery(data.values()).execute();
        } catch (SQLException e) {
            debug().warning("Unable to save user data! " + e.getMessage(), e);
        }
    }

    public void addTorch(Location location) {
        new AddTorchQuery(location).executeAsync();
    }

    /**
     * Should be run async to avoid lagging server
     */
    public void pruneBlockLog() {
        saveBlockLog();
        int limit = XPConfig.inst().getBlockLogLimit();

        try {
            long currentAmount = (long) new XPQuery("SELECT COUNT(*) AS count FROM PlacedBlockData;").executeQuery().get(0).get("count");

            long toRemove = Math.max(currentAmount - limit, 0);

            if(toRemove < 1000) return; // Not enough to justify recreating the table

            new PruneBlockLogQuery(toRemove).execute();

            Map<Chunk, Set<Location>> newBlockLog = new HashMap<>();

            for(World world : NoirXP.inst().getServer().getWorlds()) {
                for(Chunk chunk : world.getLoadedChunks()) {
                    List<Map<String, Object>> result = new GetBlockLogQuery(chunk).executeQuery();

                    for (Map<String, Object> entry : result) {
                        Location loc = constructLocation(entry);
                        if(!newBlockLog.containsKey(chunk)) newBlockLog.put(chunk, new HashSet<>());
                        newBlockLog.get(chunk).add(loc);
                    }
                }
            }

            Datamaps.replaceBlockLog(newBlockLog);
        } catch (SQLException e) {
            debug().warning("Failed to prune the block log! " + e.getMessage(), e);
        }
    }

    public void saveBlockLog() {
        Map<Chunk, Set<Location>> unsyncedAdds = Datamaps.clearUnsyncedAddBlock();
        Map<Chunk, Set<Location>> unsyncedRemoves = Datamaps.clearUnsyncedRemoveBlock();

        try {
            Set<Location> unsyncedAddsSet = unsyncedAdds.values().stream().flatMap(Set::stream).collect(Collectors.toSet());
            Set<Location> unsyncedRemovesSet = unsyncedRemoves.values().stream().flatMap(Set::stream).collect(Collectors.toSet());

            if(unsyncedAddsSet.size() > 0) new AddBlockLogQuery(unsyncedAddsSet).execute();

            if(unsyncedRemovesSet.size() > 0) new RemoveBlockLogQuery(unsyncedRemovesSet).execute();
        }
        catch(SQLException e){
            debug().warning("Failed to save the block log! " + e.getMessage(), e);
        }
    }

    public void loadBlockLog(Chunk chunk) {
        ListenableFuture<List<Map<String, Object>>> future = new GetBlockLogQuery(chunk).executeQueryAsync();
        Callback.addSyncCallback(future, new FutureCallback<List<Map<String, Object>>>() {
            @Override
            public void onSuccess(List<Map<String, Object>> result) {
                Set<Location> ret = new HashSet<>();
                for (Map<String, Object> entry : result) {
                    Location loc = constructLocation(entry);
                    Chunk c = loc.getChunk();
                    ret.add(loc);
                }

                Datamaps.loadChunk(chunk, ret);
            }

            @Override
            public void onFailure(Throwable t) {
                debug().warning("Unable to pull block log for chunk! " + t.getMessage(), t);
            }
        });
    }

    private Location constructLocation(Map<String, Object> entry) {
        String world = (String) entry.get("world");
        int x = (int) entry.get("x");
        int y = (int) entry.get("y");
        int z = (int) entry.get("z");
        return new Location(NoirXP.inst().getServer().getWorld(world), x, y, z);
    }
}
