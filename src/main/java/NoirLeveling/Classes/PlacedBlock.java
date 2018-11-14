package NoirLeveling.Classes;

import org.bukkit.Location;
import org.bukkit.World;

public class PlacedBlock {
    private String playerId;
    private Location location;
    private boolean ownsBlock;
    public PlacedBlock(String playerId, Location location, boolean ownsBlock) {
        this.playerId = playerId;
        this.location = location;
        this.ownsBlock = ownsBlock;
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public Location getLocation() {
        return this.location;
    }

    public boolean ownsBlock() {
        return this.ownsBlock;
    }
}
