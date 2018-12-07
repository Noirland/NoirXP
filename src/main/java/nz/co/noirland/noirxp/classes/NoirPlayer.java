package nz.co.noirland.noirxp.classes;

import nz.co.noirland.noirxp.NoirXP;
import nz.co.noirland.noirxp.callbacks.PlayerCallbacks;
import nz.co.noirland.noirxp.config.UserdataConfig;
import nz.co.noirland.noirxp.constants.PlayerClass;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NoirPlayer {
    private Player bukkitPlayer;
    private String username;
    private UUID playerUUID;
    private float maxHealth = 20;

    private Map<PlayerClass, Integer> classXP = new HashMap<>();

    public NoirPlayer(UUID playerId) {
        this.bukkitPlayer = NoirXP.inst().getServer().getPlayer(playerId);
        this.playerUUID = playerId;
    }

    public int getXP(PlayerClass playerClass) {
        return classXP.get(playerClass);
    }

    public int getLevel(PlayerClass playerClass) {
        return PlayerCallbacks.getLevelFromXp(classXP.get(playerClass));
    }

    public Map<PlayerClass, Integer> getXP() {
        return Collections.unmodifiableMap(classXP);
    }

    public void giveXP(PlayerClass playerClass, int xp) {
        if(!UserdataConfig.inst().isLevelling(playerUUID)) return;

        int oldXP = classXP.get(playerClass);
        int newXP = oldXP + xp;
        int newLevel = PlayerCallbacks.getLevelFromXp(newXP);

        if (UserdataConfig.inst().isVerbose(playerUUID) && playerClass != PlayerClass.GENERAL) {
            getBukkitPlayer().sendMessage("+" + xp + " " + playerClass.getFormattedLower());
        }

        if(PlayerCallbacks.getLevelFromXp(newXP) <= PlayerCallbacks.MAX_LEVEL) classXP.put(playerClass, newXP);

        if (isLevelUp(oldXP, newXP)) {
            getBukkitPlayer().sendMessage("Your " + playerClass.getTitleLower() + " level just increased to "
                    + ChatColor.GOLD + newLevel + ChatColor.WHITE + "!");
        }

        if(playerClass != PlayerClass.GENERAL) giveXP(PlayerClass.GENERAL, xp); // Give General XP as well
    }

    public void setXP(PlayerClass playerClass, int xp) {
        classXP.put(playerClass, xp);

        if(playerClass != PlayerClass.GENERAL) {
            int xpDiff = xp - getXP(playerClass);
            classXP.computeIfPresent(PlayerClass.GENERAL, (c, genXP) -> genXP - xpDiff);
        }

        int level = PlayerCallbacks.getLevelFromXp(xp);
        setMaxHealth(PlayerCallbacks.getHealthFromLevel(level));
        PlayerCallbacks.setPlayerMaxHealth(this.playerUUID, this.maxHealth);
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

    public UUID getUniqueId() {
        return this.playerUUID;
    }


    public int getOverallLevel() {
        return PlayerCallbacks.getLevelFromXp(classXP.get(PlayerClass.GENERAL));
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

    public void resetLevels() {
        for(PlayerClass playerClass : PlayerClass.values()) {
            classXP.put(playerClass, 0);
        }
        setMaxHealth(20);
        PlayerCallbacks.setPlayerMaxHealth(this.playerUUID, this.maxHealth);
    }

}
