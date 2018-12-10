package nz.co.noirland.noirxp.events;

import nz.co.noirland.noirxp.NoirXP;
import nz.co.noirland.noirxp.callbacks.BlockCallbacks;
import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.config.UserdataConfig;
import nz.co.noirland.noirxp.constants.PlayerClass;
import nz.co.noirland.noirxp.helpers.Datamaps;
import nz.co.noirland.noirxp.struct.ItemXPData;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Optional;

public class PlayerEvents implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void playerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        NoirPlayer noirPlayer = NoirXP.getPlayer(player.getUniqueId());
        noirPlayer.setUsername(player.getName());
        noirPlayer.setBukkitPlayer(event.getPlayer());
        BlockCallbacks.setPlayerChatColor(noirPlayer);

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(noirPlayer.getMaxHealth());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!player.getWorld().getName().equalsIgnoreCase("space")) {
            if (player.getLocation().getY() > 2000) {
                player.teleport(NoirXP.inst().getServer().getWorld("space").getSpawnLocation());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.hasItem() || !UserdataConfig.inst().isLevelling(event.getPlayer().getUniqueId())) return;

        NoirPlayer player = NoirXP.getPlayer(event.getPlayer().getUniqueId());

        String blockName;

        if (event.getItem().getItemMeta().hasDisplayName()) {
            blockName = ChatColor.stripColor(event.getItem().getItemMeta().getDisplayName());
        }
        else {
             blockName = event.getItem().getType().toString();
        }


        Optional<ItemXPData> xp = Datamaps.getCustomBlock(blockName);
        if(!xp.isPresent()) return;


        int levelToUse = xp.get().levelToUse;

        if (player.getLevel(PlayerClass.GENERAL) < levelToUse) {
            event.getPlayer().sendMessage("Level " + levelToUse + " required to use.");
            event.setCancelled(true);
        }


    }

}
