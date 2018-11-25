package nz.co.noirland.noirxp.callbacks;

import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.constants.PlayerClass;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;

public class BlockCallbacks {
    /**
     * Sets a players username based on their highest xp profession.
     * @param player The player to set.
     */
    public static void setPlayerChatColor(NoirPlayer player) {
        // Gets all XP, removed the overall level, sorts by XP and then selects the highest value
        Map.Entry<PlayerClass, Integer> mainClass = player.getXP().entrySet().stream()
                .filter(e -> e.getKey() != PlayerClass.GENERAL)
                .max(Map.Entry.comparingByValue())
                .orElseThrow(IllegalStateException::new);

        ChatColor classColor = ChatColor.LIGHT_PURPLE;

        if(mainClass.getValue() > 0) {
            classColor = mainClass.getKey().getColor();
        }

        Player bukkitPlayer = player.getBukkitPlayer();
        bukkitPlayer.setDisplayName(classColor + bukkitPlayer.getName() + ChatColor.WHITE);
    }
}
