package nz.co.noirland.noirxp.classes;

import org.bukkit.Location;

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
