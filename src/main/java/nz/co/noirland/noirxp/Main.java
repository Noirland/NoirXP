package nz.co.noirland.noirxp;

import nz.co.noirland.noirxp.callbacks.BlockCallbacks;
import nz.co.noirland.noirxp.callbacks.EntityCallbacks;
import nz.co.noirland.noirxp.callbacks.PlayerCallbacks;
import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.commands.BalanceCommand;
import nz.co.noirland.noirxp.commands.NoirCommand;
import nz.co.noirland.noirxp.commands.XpTabCompleter;
import noirxp.customitems.*;
import nz.co.noirland.noirxp.database.Database;
import noirxp.events.*;
import nz.co.noirland.noirxp.customitems.ChainBoots;
import nz.co.noirland.noirxp.customitems.ChainChest;
import nz.co.noirland.noirxp.customitems.ChainHelmet;
import nz.co.noirland.noirxp.customitems.ChainLeggings;
import nz.co.noirland.noirxp.customitems.DiamondHorseArmour;
import nz.co.noirland.noirxp.customitems.GoldenHorseArmour;
import nz.co.noirland.noirxp.customitems.ICustomItem;
import nz.co.noirland.noirxp.customitems.IronHorseArmour;
import nz.co.noirland.noirxp.customitems.ItemCreator;
import nz.co.noirland.noirxp.customitems.Lead;
import nz.co.noirland.noirxp.customitems.NameTag;
import nz.co.noirland.noirxp.customitems.Saddle;
import nz.co.noirland.noirxp.customitems.SpaceBoots;
import nz.co.noirland.noirxp.customitems.SpaceHelmet;
import nz.co.noirland.noirxp.customitems.SpacePants;
import nz.co.noirland.noirxp.customitems.SpaceVest;
import nz.co.noirland.noirxp.events.BlockEvents;
import nz.co.noirland.noirxp.events.BrewEvents;
import nz.co.noirland.noirxp.events.CraftEvents;
import nz.co.noirland.noirxp.events.DamageEvents;
import nz.co.noirland.noirxp.events.EnchantEvents;
import nz.co.noirland.noirxp.events.FurnaceEvents;
import nz.co.noirland.noirxp.events.InventoryEvents;
import nz.co.noirland.noirxp.events.PickupEvents;
import nz.co.noirland.noirxp.events.PlayerEvents;
import nz.co.noirland.noirxp.events.TameBreedEvents;
import nz.co.noirland.noirxp.events.WeatherEvents;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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

        new BukkitRunnable() {

            @Override
            public void run() {
                savePlayerData();
            }

        }.runTaskTimerAsynchronously(this, 20 * 60 * 10, 20 * 60 * 10); // Run backup every 10 mins (tick time)

    }

    @Override
    public void onDisable() {
        BlockCallbacks.replaceTorchDataTable();
        savePlayerData();
    }

    public void savePlayerData() {
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
        getServer().getPluginManager().registerEvents(new BrewEvents(), this);
    }

}
