package nz.co.noirland.noirxp.classes;

import nz.co.noirland.noirxp.callbacks.PlayerCallbacks;
import nz.co.noirland.noirxp.NoirXP;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.UUID;

public class NoirPlayer {
    private Player bukkitPlayer;
    private String username;
    private String playerId;
    public Alchemy alchemy = new Alchemy();
    public Building building = new Building();
    public Cooking cooking = new Cooking();
    public Farming farming = new Farming();
    public Fishing fishing = new Fishing();
    public Gathering gathering = new Gathering();
    public Hunting hunting = new Hunting();
    public Mining mining = new Mining();
    public Smithing smithing = new Smithing();
    public Taming taming = new Taming();
    private float currentHealth = 20;
    private float maxHealth = 20;
    private int level = 1;
    private int xp;

    public NoirPlayer(String playerId, Server server) {
        this.playerId = playerId;
        this.bukkitPlayer = server.getPlayer(UUID.fromString(this.playerId));
    }

    public NoirPlayer(String playerId) {
        this.playerId = playerId;
        this.bukkitPlayer = NoirXP.inst().getServer().getPlayer(UUID.fromString(this.playerId));
    }

    public void setCurrentHealth(float health) {
        this.currentHealth = health;
    }

    public float getCurrentHealth() {
        return this.currentHealth;
    }

    public void setMaxHealth(float health) {
        this.maxHealth = health;
    }

    public float getMaxHealth() {
        return this.maxHealth;
    }

    public Player getBukkitPlayer() {
        return this.bukkitPlayer;
    }

    public void setBukkitPlayer(Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
    }

    public String getUniqueId() {
        return this.playerId;
    }


    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
        this.xp = PlayerCallbacks.GetXpFromLevel(this.level);
        this.maxHealth = PlayerCallbacks.getHealthFromLevel(this.level);
        PlayerCallbacks.setPlayerMaxHealth(this.getUniqueId(), this.maxHealth);
    }

    public int getXp() {
        return this.xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
        this.level = PlayerCallbacks.getLevelFromXp(this.xp);
        this.maxHealth = PlayerCallbacks.getHealthFromLevel(this.level);
        PlayerCallbacks.setPlayerMaxHealth(this.getUniqueId(), this.maxHealth);
    }

    public void addXp(int xp) {
        this.xp += xp;
        this.level = PlayerCallbacks.getLevelFromXp(this.xp);
        this.maxHealth = PlayerCallbacks.getHealthFromLevel(this.level);
        PlayerCallbacks.setPlayerMaxHealth(this.getUniqueId(), this.maxHealth);
    }

    public boolean isLevelUp(int oldxp, int newxp) {
        int oldLevel = PlayerCallbacks.getLevelFromXp(oldxp);
        int newLevel = PlayerCallbacks.getLevelFromXp(newxp);

        return newLevel > oldLevel;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
