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

        if (UserdataConfig.inst().isVerbose(playerUUID)) {
            getBukkitPlayer().sendMessage("+" + xp + " " + playerClass.getFormattedLower());
        }

        if(playerClass != PlayerClass.GENERAL) {
            if(PlayerCallbacks.getLevelFromXp(newXP) <= PlayerCallbacks.MAX_LEVEL) classXP.put(playerClass, newXP);

            if (isLevelUp(oldXP, newXP)) {
                getBukkitPlayer().sendMessage("Your " + playerClass.getTitleLower() + " level just increased to "
                        + ChatColor.GOLD + newLevel + ChatColor.WHITE + "!");
            }
        }

        int oldOverallXP = classXP.get(PlayerClass.GENERAL);
        int newOverallXP = oldOverallXP + xp;

        if(PlayerCallbacks.getLevelFromXp(newOverallXP) <= PlayerCallbacks.MAX_LEVEL) classXP.put(PlayerClass.GENERAL, newOverallXP);

        if (isLevelUp(oldOverallXP, newOverallXP)) {
            getBukkitPlayer().sendMessage("Your overall level just increased to " +
                    ChatColor.GOLD + newLevel + ChatColor.WHITE + "!");
        }

    }

    public void setXP(PlayerClass playerClass, int xp) {
        classXP.put(playerClass, xp);
        if(playerClass == PlayerClass.GENERAL) {
            int level = PlayerCallbacks.getLevelFromXp(xp);
            this.maxHealth = PlayerCallbacks.getHealthFromLevel(level);
            PlayerCallbacks.setPlayerMaxHealth(this.playerUUID, this.maxHealth);
        }
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
        this.maxHealth = 20;
        PlayerCallbacks.setPlayerMaxHealth(this.playerUUID, this.maxHealth);
    }

}
