package nz.co.noirland.noirxp.events;

import nz.co.noirland.noirxp.callbacks.BlockCallbacks;
import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.database.Database;
import nz.co.noirland.noirxp.Main;
import nz.co.noirland.noirxp.sqlprocedures.SQLProcedures;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class PlayerEvents implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void playerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!Main.players.containsKey(player.getUniqueId().toString())) {
            NoirPlayer noirPlayer = new NoirPlayer(player.getUniqueId().toString());
            noirPlayer.setUsername(player.getName());
            Main.players.put(player.getUniqueId().toString(), noirPlayer);
            Database.executeSQLUpdateDelete(SQLProcedures.insertIntoPlayerTable(player.getUniqueId().toString()));

            event.getPlayer().getServer().getConsoleSender().sendMessage(Color.RED + "PlayerJoinEvent error.");

        }

        NoirPlayer noirPlayer = Main.players.get(player.getUniqueId().toString());
        noirPlayer.setUsername(player.getName());
        noirPlayer.setBukkitPlayer(event.getPlayer());
        BlockCallbacks.setPlayerChatColor(noirPlayer);

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(noirPlayer.getMaxHealth());
        File file = new File(Main.userdataFilePath);

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section;
        if (!configuration.contains(player.getUniqueId().toString())) {
            section = configuration.createSection(player.getUniqueId().toString());
        }
        else {
            section = configuration.getConfigurationSection(player.getUniqueId().toString());
        }
        if (!section.isSet("leveling")) {
            section.set("leveling", true);
        }
        if (!section.isSet("verbose")) {
            section.set("verbose", false);
        }
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equalsIgnoreCase("space")) {
            if (player.getLocation().getY() > 2000) {
                player.teleport(Main.plugin.getServer().getWorld("space").getSpawnLocation());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.hasItem()) {
            return;
        }

        NoirPlayer player = Main.players.get(event.getPlayer().getUniqueId().toString());

        String blockName;

        if (event.getItem().getItemMeta().hasDisplayName()) {
            blockName = ChatColor.stripColor(event.getItem().getItemMeta().getDisplayName());
        }
        else {
             blockName = event.getItem().getType().toString();
        }
        String sql = SQLProcedures.getCustomBlock(blockName);
        List<HashMap> resultSet = Database.executeSQLGet(sql);
        if (resultSet.size() == 0) {
            return;
        }

        int levelToUse = (int) resultSet.get(0).get("levelToUse");

        if (player.getLevel() < levelToUse) {
            event.getPlayer().sendMessage("Level " + levelToUse + " required to use.");
            event.setCancelled(true);
        }


    }

}
