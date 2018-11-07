package NoirLeveling.Events;

import NoirLeveling.Callbacks.BlockCallbacks;
import NoirLeveling.Callbacks.PlayerCallbacks;
import NoirLeveling.Classes.NoirPlayer;
import NoirLeveling.Database.Database;
import NoirLeveling.Main;
import NoirLeveling.SQLProcedures.SQLProcedures;
import org.bukkit.Color;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class PlayerEvents implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void playerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!Main.players.containsKey(player.getUniqueId().toString())) {
            NoirPlayer noirPlayer = new NoirPlayer(player.getUniqueId().toString());
            noirPlayer.setUsername(player.getName());
            Main.players.put(player.getUniqueId().toString(), noirPlayer);
            try {
                Database.executeSQLUpdateDelete(SQLProcedures.insertIntoPlayerTable(player.getUniqueId().toString()));
            }
            catch (SQLException e) {
                event.getPlayer().getServer().getConsoleSender().sendMessage(Color.RED + "PlayerJoinEvent error.");
            }
        }

        NoirPlayer noirPlayer = Main.players.get(player.getUniqueId().toString());
        noirPlayer.setUsername(player.getName());
        noirPlayer.setBukkitPlayer(event.getPlayer());
        BlockCallbacks.setPlayerChatColor(noirPlayer);

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(noirPlayer.getMaxHealth());
        File file = new File(Main.userdataFilePath);

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        if (configuration.contains(player.getUniqueId().toString())) {
            return;
        }
        configuration.createSection(player.getUniqueId().toString()).set("leveling", true);
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

}
