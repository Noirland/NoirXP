package NoirLeveling.Events;

import NoirLeveling.Callbacks.PlayerCallbacks;
import NoirLeveling.Classes.NoirPlayer;
import NoirLeveling.Classes.TameBreedEntity;
import NoirLeveling.Constants.PlayerClass;
import NoirLeveling.Helpers.Datamaps;
import NoirLeveling.Main;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class TameBreedEvents implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onTame(EntityTameEvent event) {
        if (!(event.getOwner() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getOwner();
        if (Datamaps.tameBreedEntityMap.containsKey(event.getEntityType())) {
            TameBreedEntity entity = Datamaps.tameBreedEntityMap.get(event.getEntityType());
            NoirPlayer noirPlayer = Main.players.get(player.getUniqueId().toString());
            if (noirPlayer.farming.getLevel() < entity.getLevelToTame()) {
                player.sendMessage("Level " + entity.getLevelToTame() + " farming required.");
                event.setCancelled(true);
            } else {
                PlayerCallbacks.xpGained(player.getUniqueId().toString(), PlayerClass.FARMING, entity.getTameXp());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreed(EntityBreedEvent event) {
        if (!(event.getBreeder() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getBreeder();

        if (Datamaps.tameBreedEntityMap.containsKey(event.getEntityType())) {
            TameBreedEntity entity = Datamaps.tameBreedEntityMap.get(event.getEntityType());
            NoirPlayer noirPlayer = Main.players.get(player.getUniqueId().toString());

            if (noirPlayer.farming.getLevel() < entity.getLevelToBreed()) {
                player.sendMessage("Level " + entity.getLevelToBreed() + " farming required.");
                event.setCancelled(true);
            }
            else {
                PlayerCallbacks.xpGained(player.getUniqueId().toString(), PlayerClass.FARMING, entity.getBreedXp());
            }
        }
    }
}
