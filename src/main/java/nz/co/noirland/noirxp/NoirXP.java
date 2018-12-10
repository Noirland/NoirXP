package nz.co.noirland.noirxp;

import nz.co.noirland.libs.acf.ACFUtil;
import nz.co.noirland.libs.acf.BukkitCommandManager;
import nz.co.noirland.libs.acf.InvalidCommandArgument;
import nz.co.noirland.libs.acf.MessageKeys;
import nz.co.noirland.libs.acf.MinecraftMessageKeys;
import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.commands.CommandNoir;
import nz.co.noirland.noirxp.constants.PlayerClass;
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
import nz.co.noirland.noirxp.events.ChunkEvents;
import nz.co.noirland.noirxp.events.CraftEvents;
import nz.co.noirland.noirxp.events.DamageEvents;
import nz.co.noirland.noirxp.events.EnchantEvents;
import nz.co.noirland.noirxp.events.FurnaceEvents;
import nz.co.noirland.noirxp.events.HuntingEvents;
import nz.co.noirland.noirxp.events.InventoryEvents;
import nz.co.noirland.noirxp.events.PickupEvents;
import nz.co.noirland.noirxp.events.PlayerEvents;
import nz.co.noirland.noirxp.events.TameBreedEvents;
import nz.co.noirland.noirxp.events.WeatherEvents;
import nz.co.noirland.noirxp.helpers.Datamaps;
import nz.co.noirland.zephcore.Debug;
import nz.co.noirland.zephcore.ZephCore;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class NoirXP extends JavaPlugin implements Listener {

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

        // Datamaps.torchSet.addAll(XPDatabase.inst().getTorches()); // TODO Re-enable
        Datamaps.tameBreedEntityMap.putAll(XPDatabase.inst().getTamingData());
        Datamaps.customBlocks.putAll(XPDatabase.inst().loadCustomBlocks());

        new BukkitRunnable() {
            @Override
            public void run() {
                debug().warning("Backing up player data...");
                XPDatabase.inst().saveUserData(players);
                //debug().warning("Saving and reloading block log...");
                //XPDatabase.inst().pruneBlockLog();
                debug().warning("Backup complete!");
            }

        }.runTaskTimerAsynchronously(this, 20 * 60 * 10, 20 * 60 * 10); // Run backup every 10 mins (tick time)
    }

    @EventHandler
    public void onZephCoreDisabled(PluginDisableEvent event) {
        if(event.getPlugin() == ZephCore.inst()) {
            debug().warning("ZephCore is being disabled, backing up data and then disabling this plugin also.");
            XPDatabase.inst().saveUserData(players);
            XPDatabase.inst().saveBlockLog();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        XPDatabase.inst().saveUserData(players);
        XPDatabase.inst().saveBlockLog();
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
        BukkitCommandManager manager = new BukkitCommandManager(this);
        manager.enableUnstableAPI("help");

        manager.getCommandContexts().registerContext(PlayerClass.class, c -> {
            String first = c.popFirstArg();
            Optional<PlayerClass> optClass = PlayerClass.fromName(first);
            if(optClass.isPresent()) return optClass.get();
            if(c.isOptional()) return null;

            throw new InvalidCommandArgument(MessageKeys.PLEASE_SPECIFY_ONE_OF, "{valid}", ACFUtil.join(PlayerClass.getNames()));
        });

        manager.getCommandContexts().registerIssuerAwareContext(NoirPlayer.class, c -> {
            String name = c.popFirstArg();

            if(name == null) {
                if(c.hasFlag("defaultself") && c.isOptional()) {
                    if(c.getIssuer().isPlayer()) {
                        return NoirXP.getPlayer(c.getPlayer().getUniqueId());
                    }

                    throw new InvalidCommandArgument(MessageKeys.NOT_ALLOWED_ON_CONSOLE, false);
                }

                throw new InvalidCommandArgument(true);

            }

            UUID uuid = null;
            if (c.hasFlag("uuid")) {
                try {
                    uuid = UUID.fromString(name);
                } catch (IllegalArgumentException ignored) {}
            }

            OfflinePlayer offlinePlayer = uuid != null ? Bukkit.getOfflinePlayer(uuid) : Bukkit.getOfflinePlayer(name);

            if (offlinePlayer != null && (offlinePlayer.hasPlayedBefore() || offlinePlayer.isOnline())) {
                return NoirXP.getPlayer(offlinePlayer.getUniqueId());
            } else {
                throw new InvalidCommandArgument(MinecraftMessageKeys.NO_PLAYER_FOUND_OFFLINE, false, "{search}", name);
            }
        });

        manager.getCommandCompletions().registerStaticCompletion("playerclass", PlayerClass.getNames());

        manager.registerCommand(new CommandNoir());
    }

    private void enableEventHooks() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this, this);
        pm.registerEvents(new PlayerEvents(), this);
        pm.registerEvents(new BlockEvents(), this);
        pm.registerEvents(new PickupEvents(), this);
        pm.registerEvents(new CraftEvents(), this);
        pm.registerEvents(new DamageEvents(), this);
        pm.registerEvents(new WeatherEvents(), this);
        pm.registerEvents(new InventoryEvents(), this);
        pm.registerEvents(new EnchantEvents(), this);
        pm.registerEvents(new TameBreedEvents(), this);
        pm.registerEvents(new FurnaceEvents(), this);
        pm.registerEvents(new ChunkEvents(), this);
        pm.registerEvents(new HuntingEvents(), this);
    }

    public static NoirPlayer getPlayer(UUID uuid) {
        return players.get(uuid.toString());
    }

}
