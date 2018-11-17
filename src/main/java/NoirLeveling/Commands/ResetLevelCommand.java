package NoirLeveling.Commands;

import NoirLeveling.Database.Database;
import NoirLeveling.SQLProcedures.SQLProcedures;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class ResetLevelCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This is a player-only command.");
            return true;
        }

        if (args.length > 0) {
            return false;
        }
        Player player = (Player) sender;

        String sql = SQLProcedures.resetPlayerXp(player.getUniqueId().toString());
        Database.executeSQLUpdateDelete(sql);
        player.sendMessage("Successfully reset xp.");
        return true;
    }
}
