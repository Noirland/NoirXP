package nz.co.noirland.noirxp;

import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.commands.BalanceCommand;
import nz.co.noirland.noirxp.commands.NoirCommand;
import nz.co.noirland.noirxp.commands.XpTabCompleter;
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
import nz.co.noirland.noirxp.database.XPDatabase;
import nz.co.noirland.noirxp.events.BlockEvents;
import nz.co.noirland.noirxp.events.CraftEvents;
import nz.co.noirland.noirxp.events.DamageEvents;
import nz.co.noirland.noirxp.events.EnchantEvents;
import nz.co.noirland.noirxp.events.FurnaceEvents;
import nz.co.noirland.noirxp.events.InventoryEvents;
import nz.co.noirland.noirxp.events.PickupEvents;
import nz.co.noirland.noirxp.events.PlayerEvents;
import nz.co.noirland.noirxp.events.TameBreedEvents;
import nz.co.noirland.noirxp.events.WeatherEvents;
import nz.co.noirland.noirxp.helpers.Datamaps;
import nz.co.noirland.zephcore.Debug;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NoirXP extends JavaPlugin {

    private static NoirXP inst;
    private static Debug debug;

    public static String userdataFilePath;
    public static Map<String, NoirPlayer> players;

    public static NoirXP inst() {
        return inst;
    }

    public static Debug debug() {
        return debug;
    }

    @Override
    public void onEnable(){
        inst = this;
        debug = new Debug(this);

        // Init the database, and ensure most current schema is applied
        XPDatabase.inst().checkSchema();

        players = XPDatabase.inst().getAllPlayers();

        enableEventHooks();
        enableCommandHooks();
        enableCustomRecipes();

        Datamaps.torchSet.addAll(XPDatabase.inst().getTorches());
        Datamaps.tameBreedEntityMap.putAll(XPDatabase.inst().getTamingData());

        new BukkitRunnable() {
            @Override
            public void run() {
                XPDatabase.inst().saveUserData(players);
            }

        }.runTaskTimer(this, 20 * 60 * 10, 20 * 60 * 10); // Run backup every 10 mins (tick time)

    }

    @Override
    public void onDisable() {
        XPDatabase.inst().saveUserData(players);
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
        ItemCreator.CreateCustomItems(getServer(), customItemList);
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
