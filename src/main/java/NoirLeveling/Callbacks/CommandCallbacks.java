package NoirLeveling.Callbacks;

import NoirLeveling.Classes.NoirPlayer;
import NoirLeveling.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CommandCallbacks {
    public static void disablePlayerLeveling(String playerId) {
        NoirPlayer noirPlayer = Main.players.get(playerId);
        File file = new File(Main.userdataFilePath);
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

    public static void resetAllXp(String playerId) {
        NoirPlayer player = Main.players.get(playerId);
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
