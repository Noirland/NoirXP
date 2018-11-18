package nz.co.noirland.noirxp.commands;

import nz.co.noirland.noirxp.database.Database;
import nz.co.noirland.noirxp.sqlprocedures.SQLProcedures;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
