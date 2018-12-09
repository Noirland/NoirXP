package nz.co.noirland.noirxp.events;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import nz.co.noirland.noirxp.NoirXP;
import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.classes.TameBreedEntity;
import nz.co.noirland.noirxp.config.UserdataConfig;
import nz.co.noirland.noirxp.constants.PlayerClass;
import nz.co.noirland.noirxp.helpers.Datamaps;
import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class TameBreedEvents implements Listener {

    private static final Multimap<EntityType, Material> breedingItems = HashMultimap.create();
    private static final Multimap<EntityType, Material> tamingItems = HashMultimap.create();

    static {
        breedingItems.put(EntityType.HORSE, Material.GOLDEN_APPLE);
        breedingItems.put(EntityType.HORSE, Material.GOLDEN_CARROT);

        breedingItems.put(EntityType.DONKEY, Material.GOLDEN_APPLE);
        breedingItems.put(EntityType.DONKEY, Material.GOLDEN_CARROT);

        tamingItems.put(EntityType.MULE, Material.GOLDEN_APPLE);
        tamingItems.put(EntityType.MULE, Material.GOLDEN_CARROT);


        breedingItems.put(EntityType.SHEEP, Material.WHEAT);

        breedingItems.put(EntityType.COW, Material.WHEAT);

        breedingItems.put(EntityType.MUSHROOM_COW, Material.WHEAT);

        breedingItems.put(EntityType.PIG, Material.CARROT);
        breedingItems.put(EntityType.PIG, Material.POTATO);
        breedingItems.put(EntityType.PIG, Material.BEETROOT);

        breedingItems.put(EntityType.CHICKEN, Material.WHEAT_SEEDS);
        breedingItems.put(EntityType.CHICKEN, Material.PUMPKIN_SEEDS);
        breedingItems.put(EntityType.CHICKEN, Material.MELON_SEEDS);
        breedingItems.put(EntityType.CHICKEN, Material.BEETROOT_SEEDS);

        for(Material material : Arrays.asList(Material.PORKCHOP, Material.BEEF, Material.CHICKEN, Material.RABBIT, Material.MUTTON,
                Material.ROTTEN_FLESH, Material.PORKCHOP, Material.COOKED_BEEF, Material.COOKED_CHICKEN, Material.COOKED_RABBIT,
                Material.COOKED_MUTTON)) {
            breedingItems.put(EntityType.WOLF, material);
            tamingItems.put(EntityType.WOLF, material);
        }

        for(Material material : Arrays.asList(Material.COD, Material.SALMON, Material.TROPICAL_FISH, Material.PUFFERFISH)) {
            breedingItems.put(EntityType.OCELOT, material);
            tamingItems.put(EntityType.OCELOT, material);
        }

        breedingItems.put(EntityType.RABBIT, Material.DANDELION);
        breedingItems.put(EntityType.RABBIT, Material.CARROT);
        breedingItems.put(EntityType.RABBIT, Material.GOLDEN_CARROT);

        breedingItems.put(EntityType.LLAMA, Material.HAY_BLOCK);

        breedingItems.put(EntityType.TURTLE, Material.SEAGRASS);

        tamingItems.put(EntityType.PARROT, Material.WHEAT_SEEDS);
        tamingItems.put(EntityType.PARROT, Material.PUMPKIN_SEEDS);
        tamingItems.put(EntityType.PARROT, Material.MELON_SEEDS);
        tamingItems.put(EntityType.PARROT, Material.BEETROOT_SEEDS);
    }

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

    @EventHandler(ignoreCancelled = true)
    public void onInitiateTameBreed(PlayerInteractEntityEvent event) {
        if(!(event.getRightClicked() instanceof Animals)) return;
        Animals animal = ((Animals) event.getRightClicked());

        Player player = event.getPlayer();
        if (!UserdataConfig.inst().isLevelling(player.getUniqueId())) return;

        if (!Datamaps.tameBreedEntityMap.containsKey(animal.getType())) return;

        ItemStack item;
        switch(event.getHand()) {
            case OFF_HAND:
                item = player.getInventory().getItemInOffHand();
                break;
            default:
                item = player.getInventory().getItemInMainHand();

        }
        if(item == null || item.getType() == Material.AIR) return;

        NoirPlayer noirPlayer = NoirXP.getPlayer(player.getUniqueId());
        TameBreedEntity breedTameData = Datamaps.tameBreedEntityMap.get(animal.getType());

        if(animal instanceof Tameable && !((Tameable) animal).isTamed()) {
            if(tamingItems.containsEntry(animal.getType(), item.getType())) {
                if (noirPlayer.getLevel(PlayerClass.FARMING) < breedTameData.getLevelToTame()) {
                    player.sendMessage("Level " + breedTameData.getLevelToTame() + " farming required.");
                    event.setCancelled(true);
                }
            }
            return; // Won't breed if they aren't tamed
        }

        // Parrots are a special case where they cannot be bred but can be tamed
        if(!breedingItems.containsEntry(animal.getType(), item.getType()) || animal.getType() != EntityType.PARROT) return;

        if (noirPlayer.getLevel(PlayerClass.FARMING) < breedTameData.getLevelToBreed()) {
            player.sendMessage("Level " + breedTameData.getLevelToBreed() + " farming required.");
            event.setCancelled(true);
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

            // Checks were already done during onInitiateTameBreed to ensure player is correct level
            noirPlayer.giveXP(PlayerClass.FARMING, entity.getBreedXp());
        }
    }
}
