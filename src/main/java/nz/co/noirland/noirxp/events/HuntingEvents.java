package nz.co.noirland.noirxp.events;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import nz.co.noirland.noirxp.NoirXP;
import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.constants.PlayerClass;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Optional;

public class HuntingEvents implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if(killer == null) return;

        NoirPlayer noirPlayer = NoirXP.getPlayer(killer.getUniqueId());
        if(noirPlayer == null) return;

        int xp = 1;

        Optional<ActiveMob> mythicMob = MythicMobs.inst().getMobManager().getActiveMob(event.getEntity().getUniqueId());

        if(mythicMob.isPresent()) {
            xp = 3 * mythicMob.get().getLevel();
        }

        noirPlayer.giveXP(PlayerClass.HUNTING, xp);
    }
}
