package NoirLeveling;

import NoirLeveling.Callbacks.BlockCallbacks;
import NoirLeveling.Callbacks.EntityCallbacks;
import NoirLeveling.Callbacks.PlayerCallbacks;
import NoirLeveling.Classes.NoirPlayer;
import NoirLeveling.Commands.BalanceCommand;
import NoirLeveling.Commands.NoirCommand;
import NoirLeveling.Commands.XpTabCompleter;
import NoirLeveling.CustomItems.*;
import NoirLeveling.Database.Database;
import NoirLeveling.Events.*;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin{
    public static JavaPlugin plugin;
    public static String url;
    public static String urlNoDb;
    public static String username;
    public static String password;
    public static String blocksFilePath;
    public static String userdataFilePath;
    public static Server server;
    public static HashMap<String, NoirPlayer> players;
    public static void main(String[] args){
    }
    @Override
    public void onEnable(){
        players = new HashMap<>();
        server = getServer();
        plugin = this;
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        urlNoDb = "jdbc:mysql://" + getConfig().getString("host") + ":" + getConfig().getString("port") + "?autoReconnect=true&useSSL=false&allowMultiQueries=true";
        url = "jdbc:mysql://" + getConfig().getString("host") + ":" + getConfig().getString("port") + "/NoirLive?autoReconnect=true&useSSL=false&allowMultiQueries=true";
        username = getConfig().getString("username");
        password = getConfig().getString("password");
        File blocksFile = new File(getDataFolder().getAbsolutePath(),"blocks.yml");
        File userDataFile = new File(getDataFolder().getAbsolutePath(),"userdata.yml");
        try {
            Boolean created = blocksFile.createNewFile();
            userDataFile.createNewFile();
            blocksFilePath = blocksFile.getAbsolutePath();
            userdataFilePath = userDataFile.getAbsolutePath();
            if (created) {
                getServer().getConsoleSender().sendMessage("blocks.yml does not exist, created default file.");
            }
            else {
                getServer().getConsoleSender().sendMessage("blocks.yml already exists, ignoring.");
            }
        } catch (IOException e) {
            getServer().getConsoleSender().sendMessage("FATAL: Could not access plugin directory!");
        }
        enableEventHooks();
        enableCommandHooks();
        enableCustomRecipes();
        try {
            Database.createDatabase();
            getServer().getConsoleSender().sendMessage("DB created/connected");
        }
        catch (Exception e) {
            e.printStackTrace();
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "Fatal: Could not start a database connection.");
        }
        this.saveDefaultConfig();

        PlayerCallbacks.addPlayersToMap();
        BlockCallbacks.addTorchDataToMap();
        EntityCallbacks.addTameBreedDataToMap();


    }

    @Override
    public void onDisable() {
        BlockCallbacks.replaceTorchDataTable();
        try {
            Connection conn;
            conn = DriverManager.getConnection(Main.url, Main.username, Main.password);
            String sql = "UPDATE Player SET username = ?, alchemyXp = ?, buildingXp = ?, " +
                    "cookingXp = ?, farmingXp = ?, fishingXp = ?, gatheringXp = ?, huntingXp = ?, miningXp = ?, " +
                    "smithingXp = ?, tamingXp = ?, totalXp = ?, currentHealth = ?, maxHealth = ? WHERE playerId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            for (NoirPlayer player : players.values()) {
                stmt.setString(1, player.getUsername());
                stmt.setInt(2, player.alchemy.getXp());
                stmt.setInt(3, player.building.getXp());
                stmt.setInt(4, player.cooking.getXp());
                stmt.setInt(5, player.farming.getXp());
                stmt.setInt(6, player.fishing.getXp());
                stmt.setInt(7, player.gathering.getXp());
                stmt.setInt(8, player.hunting.getXp());
                stmt.setInt(9, player.mining.getXp());
                stmt.setInt(10, player.smithing.getXp());
                stmt.setInt(11, player.taming.getXp());
                stmt.setInt(12, player.getXp());
                stmt.setFloat(13, player.getCurrentHealth());
                stmt.setFloat(14, player.getMaxHealth());
                stmt.setString(15, player.getUniqueId());
                stmt.addBatch();

            }
            stmt.executeBatch();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void enableCustomRecipes() {
        List<ICustomItem> customItemList = new ArrayList<ICustomItem>() {{
            add(new SpaceVest());
            add(new SpacePants());
            add(new SpaceHelmet());
            add(new SpaceBoots());
            add(new ChainBoots());
            add(new ChainHelmet());
            add(new ChainChest());
            add(new ChainLeggings());
            add(new Lead());
            add(new NameTag());
            add(new DiamondHorseArmour());
            add(new GoldenHorseArmour());
            add(new IronHorseArmour());
            add(new Saddle());
        }};
        ItemCreator.CreateCustomItems(server, customItemList);
    }

    private void enableCommandHooks() {
        getCommand("noir").setExecutor(new NoirCommand());
        getCommand("noir").setTabCompleter(new XpTabCompleter());
        getCommand("nbal").setExecutor(new BalanceCommand());
    }

    private void enableEventHooks() {
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        getServer().getPluginManager().registerEvents(new BlockEvents(), this);
        getServer().getPluginManager().registerEvents(new PickupEvents(), this);
        getServer().getPluginManager().registerEvents(new CraftEvents(), this);
        getServer().getPluginManager().registerEvents(new DamageEvents(), this);
        getServer().getPluginManager().registerEvents(new WeatherEvents(), this);
        getServer().getPluginManager().registerEvents(new InventoryEvents(), this);
        getServer().getPluginManager().registerEvents(new EnchantEvents(), this);
        getServer().getPluginManager().registerEvents(new TameBreedEvents(), this);
        getServer().getPluginManager().registerEvents(new FurnaceEvents(), this);
    }

}
