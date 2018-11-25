package nz.co.noirland.noirxp.events;

import nz.co.noirland.noirxp.NoirXP;
import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.config.UserdataConfig;
import nz.co.noirland.noirxp.constants.PlayerClass;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;

import java.util.Collections;

public class EnchantEvents implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPrepareEnchant(PrepareItemEnchantEvent event) {
        if(!UserdataConfig.inst().isLevelling(event.getEnchanter().getUniqueId())) return;

        NoirPlayer noirPlayer = NoirXP.players.get(event.getEnchanter().getUniqueId().toString());

        int alchemyLevel = noirPlayer.getLevel(PlayerClass.ALCHEMY);
        int maxEnchant = 0;
        if(alchemyLevel >= 20) maxEnchant = 1;
        if(alchemyLevel >= 30) maxEnchant = 2;
        if(alchemyLevel >= 40) maxEnchant = 3;

        for(int i = 0; i < event.getOffers().length; i++) {
            EnchantmentOffer offer = event.getOffers()[i];
            if(offer == null) continue;
            if(offer.getEnchantmentLevel() > maxEnchant) event.getOffers()[i] = null;


        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemEnchant(EnchantItemEvent event) {
        int highestLevel = Collections.max(event.getEnchantsToAdd().values());
        NoirPlayer noirPlayer = NoirXP.players.get(event.getEnchanter().getUniqueId().toString());
        if (highestLevel == 1) {
            noirPlayer.giveXP(PlayerClass.ALCHEMY, 50);
        }
        else if (highestLevel == 2) {
            noirPlayer.giveXP(PlayerClass.ALCHEMY, 70);
        }
        else if (highestLevel == 3) {
            noirPlayer.giveXP(PlayerClass.ALCHEMY, 85);
        }
    }


}
