package nz.co.noirland.noirxp.events;

import nz.co.noirland.noirxp.NoirXP;
import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.classes.TameBreedEntity;
import nz.co.noirland.noirxp.config.UserdataConfig;
import nz.co.noirland.noirxp.constants.PlayerClass;
import nz.co.noirland.noirxp.helpers.Datamaps;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class TameBreedEvents implements Listener {
    private Set<LivingEntity> currentlyBreeding = new HashSet<>();
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onTame(EntityTameEvent event) {
        if (!UserdataConfig.inst().isLevelling(event.getOwner().getUniqueId())) return;

        if (!(event.getOwner() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getOwner();
        if (Datamaps.tameBreedEntityMap.containsKey(event.getEntityType())) {
            TameBreedEntity entity = Datamaps.tameBreedEntityMap.get(event.getEntityType());
            NoirPlayer noirPlayer = NoirXP.players.get(player.getUniqueId().toString());
            if (noirPlayer.getLevel(PlayerClass.FARMING) < entity.getLevelToTame()) {
                player.sendMessage("Level " + entity.getLevelToTame() + " farming required.");
                event.setCancelled(true);
            } else {
                noirPlayer.giveXP(PlayerClass.FARMING, entity.getTameXp());
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBreed(EntityBreedEvent event) {
        if (!(event.getBreeder() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getBreeder();

        if (!UserdataConfig.inst().isLevelling(player.getUniqueId())) return;

        if (Datamaps.tameBreedEntityMap.containsKey(event.getEntityType())) {
            TameBreedEntity entity = Datamaps.tameBreedEntityMap.get(event.getEntityType());
            NoirPlayer noirPlayer = NoirXP.players.get(player.getUniqueId().toString());

            if (noirPlayer.getLevel(PlayerClass.FARMING) < entity.getLevelToBreed()) {
                if (!currentlyBreeding.contains(event.getFather()) && !currentlyBreeding.contains(event.getMother())) {
                    player.sendMessage("Level " + entity.getLevelToBreed() + " farming required.");
                    currentlyBreeding.add(event.getFather());
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            currentlyBreeding.remove(event.getFather());
                        }
                    }.runTaskLater(NoirXP.inst(), 20 * 20);
                }
                event.setCancelled(true);
            }
            else {
                noirPlayer.giveXP(PlayerClass.FARMING, entity.getBreedXp());
            }
        }
    }
}
