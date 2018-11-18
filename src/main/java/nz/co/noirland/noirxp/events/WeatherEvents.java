package nz.co.noirland.noirxp.events;

import nz.co.noirland.noirxp.helpers.Datamaps;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.util.ArrayList;
import java.util.List;

public class WeatherEvents implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            List<Location> locationsToRemove = new ArrayList<>();
            for (Location location : Datamaps.torchSet) {
                if (location.getWorld().getHighestBlockYAt(location) == location.getBlockY()) {
                    if (location.getBlock().getType() == Material.TORCH || location.getBlock().getType() == Material.WALL_TORCH) {
                        location.getBlock().breakNaturally();
                        locationsToRemove.add(location);
                    }
                }

            }
            Datamaps.torchSet.removeAll(locationsToRemove);
        }
    }
}
