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
    public static Set<UUID> currentlyBreeding = new HashSet<>();
    public static Set<LivingEntity> matingSet = new HashSet<>();

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
                if (!currentlyBreeding.contains(player.getUniqueId())) {
                    currentlyBreeding.add(player.getUniqueId());
                    player.sendMessage("Level " + entity.getLevelToTame() + " farming required.");
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            currentlyBreeding.remove(player.getUniqueId());
                        }
                    }.runTaskLater(Main.plugin, 20 * 5);
                }
                event.setCancelled(true);
            } else {
                if (!currentlyBreeding.contains(player.getUniqueId())) {
                    currentlyBreeding.add(player.getUniqueId());
                    PlayerCallbacks.xpGained(player.getUniqueId().toString(), PlayerClass.FARMING, entity.getTameXp());
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            currentlyBreeding.remove(player.getUniqueId());
                        }
                    }.runTaskLater(Main.plugin, 20 * 5);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreed(EntityBreedEvent event) {
        if (!(event.getBreeder() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getBreeder();


        if (matingSet.contains(event.getFather()) && matingSet.contains(event.getMother())) {
            event.setCancelled(true);
            return;
        }

        matingSet.add(event.getFather());
        matingSet.add(event.getMother());

        if (Datamaps.tameBreedEntityMap.containsKey(event.getEntityType())) {
            TameBreedEntity entity = Datamaps.tameBreedEntityMap.get(event.getEntityType());
            NoirPlayer noirPlayer = Main.players.get(player.getUniqueId().toString());

            if (noirPlayer.farming.getLevel() < entity.getLevelToBreed()) {

                player.sendMessage("Level " + entity.getLevelToBreed() + " farming required.");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        matingSet.remove(event.getMother());
                        matingSet.remove(event.getFather());
                    }
                }.runTaskLater(Main.plugin, 20 * 20);
                event.setCancelled(true);
            }
            else {
                PlayerCallbacks.xpGained(player.getUniqueId().toString(), PlayerClass.FARMING, entity.getBreedXp());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        matingSet.remove(event.getMother());
                        matingSet.remove(event.getFather());
                    }
                }.runTaskLater(Main.plugin, 20 * 10);
            }
        }
    }
}
