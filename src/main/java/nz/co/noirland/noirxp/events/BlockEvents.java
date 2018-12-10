package nz.co.noirland.noirxp.events;

import nz.co.noirland.noirxp.NoirXP;
import nz.co.noirland.noirxp.callbacks.PlayerCallbacks;
import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.config.UserdataConfig;
import nz.co.noirland.noirxp.constants.PlayerClass;
import nz.co.noirland.noirxp.helpers.Datamaps;
import nz.co.noirland.noirxp.struct.ItemXPData;
import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Crops;

import java.util.HashSet;
import java.util.Optional;

public class BlockEvents implements Listener {
    HashSet<Biome> snowBiomes = new HashSet<>();

    public BlockEvents() {
        snowBiomes.add(Biome.SNOWY_BEACH);
        snowBiomes.add(Biome.SNOWY_MOUNTAINS);
        snowBiomes.add(Biome.SNOWY_TAIGA);
        snowBiomes.add(Biome.SNOWY_TAIGA_HILLS);
        snowBiomes.add(Biome.SNOWY_TAIGA_MOUNTAINS);
        snowBiomes.add(Biome.SNOWY_TUNDRA);
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void OnBlockBreak(BlockBreakEvent event) {

        if (!UserdataConfig.inst().isLevelling(event.getPlayer().getUniqueId())) return;

        Location location = event.getBlock().getLocation();

        // Datamaps.removeTorch(location); // TODO Re-enable

        Player player = event.getPlayer();
        Optional<ItemXPData> xp = Datamaps.getCustomBlock(event.getBlock().getType());

        if(!xp.isPresent()) return;

        int reqLevel = xp.get().levelToBreak;
        PlayerClass playerClass = xp.get().type;

        int playerXp = NoirXP.getPlayer(player.getUniqueId()).getXP(playerClass);

        int playerLevel = PlayerCallbacks.getLevelFromXp(playerXp);

        if (playerLevel < reqLevel) {
            event.setCancelled(true);
            player.sendMessage("Level " + reqLevel + " " + playerClass.getTitleLower() + " required.");
            return;
        }


        boolean isCrop = false;
        if (event.getBlock().getState().getData() instanceof Crops) {
            Crops crops = (Crops)(event.getBlock().getState().getData());
            if (crops.getState() == CropState.RIPE) {
                isCrop = true;
            }
        }


        int breakXp = xp.get().breakXP;
        if (Datamaps.isPlaced(location)) {
            Datamaps.removeBlock(location);
            if (!isCrop) {
                breakXp = 0;
            }

        }

        Datamaps.removeOwner(event.getBlock().getLocation());

        NoirXP.getPlayer(event.getPlayer().getUniqueId()).giveXP(playerClass, breakXp);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void OnBlockPlace(BlockPlaceEvent event) {

        if (!UserdataConfig.inst().isLevelling(event.getPlayer().getUniqueId())) return;

        Player player = event.getPlayer();

        ItemStack stack = event.getItemInHand();
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            if (meta.hasLore()) {
                return;
            }
        }


        Optional<ItemXPData> xp = Datamaps.getCustomBlock(event.getBlock().getType());
        if(!xp.isPresent()) return;

        int reqLevel = xp.get().levelToPlace;
        PlayerClass playerClass = xp.get().type;

        NoirPlayer noirPlayer = NoirXP.getPlayer(player.getUniqueId());

        int playerLevel = noirPlayer.getLevel(playerClass);

        if (playerLevel < reqLevel) {
            event.setCancelled(true);
            player.sendMessage("Level " + reqLevel + " " + playerClass.getTitleLower() + " required.");
            return;
        }

        Location location = event.getBlockPlaced().getLocation();
        Datamaps.addBlock(location);

        switch(event.getBlockPlaced().getType()) {
            case FURNACE:
            case BREWING_STAND:
                Datamaps.setOwner(location, event.getPlayer().getUniqueId());
        }

        NoirXP.getPlayer(event.getPlayer().getUniqueId()).giveXP(playerClass, xp.get().placeXP);
    }

}
