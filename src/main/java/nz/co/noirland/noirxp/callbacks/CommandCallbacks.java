package nz.co.noirland.noirxp.callbacks;

import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.NoirXP;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class CommandCallbacks {
    /**
     * Disables the leveling aspect of the plugin.
     * @param playerId The player UUID to disable.
     */
    public static void disablePlayerLeveling(String playerId) {
        NoirPlayer noirPlayer = NoirXP.players.get(playerId);
        File file = new File(NoirXP.userdataFilePath);
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        Boolean currentCmdValue = configuration.getConfigurationSection(noirPlayer.getUniqueId()).getBoolean("leveling");
        configuration.getConfigurationSection(noirPlayer.getUniqueId()).set("leveling", !currentCmdValue);
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String message;
        if (currentCmdValue == true) {
            message = "Leveling has been DISABLED.";
        }
        else {
            message = "Leveling has been ENABLED.";
        }
        noirPlayer.getBukkitPlayer().sendMessage(message);
    }

    /**
     * Enables verbose mode, which shows xp gained messages in chat.
     * @param player The player to enable it for.
     */
    public static void enablePlayerVerbose(Player player) {
        File file = new File(NoirXP.userdataFilePath);
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        configuration.getConfigurationSection(player.getUniqueId().toString()).set("verbose", true);
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendMessage("Verbose mode ENABLED.");
    }

    /**
     * Disables verbose mode, which shows xp gained messages in chat.
     * @param player The player to enable it for.
     */
    public static void disablePlayerVerbose(Player player) {
        File file = new File(NoirXP.userdataFilePath);
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        configuration.getConfigurationSection(player.getUniqueId().toString()).set("verbose", false);
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendMessage("Verbose mode DISABLED.");
    }

    /**
     * Sets total xp and xp for all classes to 0.
     * @param playerId The player UUID to reset.
     */
    public static void resetAllXp(String playerId) {
        NoirPlayer player = NoirXP.players.get(playerId);
        player.setXp(0);
        player.alchemy.setXp(0);
        player.building.setXp(0);
        player.cooking.setXp(0);
        player.farming.setXp(0);
        player.fishing.setXp(0);
        player.gathering.setXp(0);
        player.hunting.setXp(0);
        player.mining.setXp(0);
        player.smithing.setXp(0);
        player.taming.setXp(0);
        player.setMaxHealth(20);
        player.setCurrentHealth(20);
    }
}
