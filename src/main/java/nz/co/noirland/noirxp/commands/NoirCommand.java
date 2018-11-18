package nz.co.noirland.noirxp.commands;

import nz.co.noirland.noirxp.callbacks.CommandCallbacks;
import nz.co.noirland.noirxp.callbacks.InventoryCallbacks;
import nz.co.noirland.noirxp.callbacks.PlayerCallbacks;
import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.constants.PlayerClassList;
import nz.co.noirland.noirxp.interfaces.INoirProfession;
import nz.co.noirland.noirxp.Main;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NoirCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This is a player-only command.");
            return true;
        }
        Player bukkitPlayer = (Player) commandSender;
        NoirPlayer noirPlayer = Main.players.get(bukkitPlayer.getUniqueId().toString());
        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "level":
                    int level = noirPlayer.getLevel();
                    int currentXp = noirPlayer.getXp();
                    int nextLevelXp = PlayerCallbacks.GetXpFromLevel(level + 1);
                    int percentage = (int)(((float)currentXp / (float)nextLevelXp) * 100);
                    bukkitPlayer.sendMessage(String.format("Level: %d - %d%% [%dXP Required]", level, percentage, nextLevelXp - currentXp));
                    return true;
                case "disable":
                    if (!bukkitPlayer.hasPermission("NoirLeveling.op")) {
                        commandSender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                    }
                    CommandCallbacks.disablePlayerLeveling(bukkitPlayer.getUniqueId().toString());
                    return true;
                case "enable":
                    if (!bukkitPlayer.hasPermission("NoirLeveling.op")) {
                        commandSender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                    }
                    CommandCallbacks.disablePlayerLeveling(bukkitPlayer.getUniqueId().toString());
                    return true;
                case "reset":
                    if (!bukkitPlayer.hasPermission("NoirLeveling.op")) {
                        commandSender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                    }
                    CommandCallbacks.resetAllXp(noirPlayer.getUniqueId());
                    bukkitPlayer.sendMessage("Success!");
                    return true;
                case "convert":
                    InventoryCallbacks.addTagsToPlayerInventory(bukkitPlayer);
                    return true;
                case "show":
                    CommandCallbacks.enablePlayerVerbose(bukkitPlayer);
                    return true;
                case "hide":
                    CommandCallbacks.disablePlayerVerbose(bukkitPlayer);
                    return true;
                default:
                    return false;
            }
        }

        else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("level") && args[1].equalsIgnoreCase("top")) {
                bukkitPlayer.sendMessage(PlayerCallbacks.getTopTenPlayers());
                return true;
            }
            if (args[0].equalsIgnoreCase("reset")) {
                if (!bukkitPlayer.hasPermission("NoirLeveling.op")) {
                    commandSender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                }
                String playerName = args[1];
                Player player = Main.server.getPlayer(playerName);
                if (player == null) {
                    bukkitPlayer.sendMessage("Player not found.");
                    return true;
                }
                CommandCallbacks.resetAllXp(player.getUniqueId().toString());
                bukkitPlayer.sendMessage("Success!");
                return true;
            }

            if (args[0].equalsIgnoreCase("class") && PlayerClassList.playerClassList.contains(args[1])) {
                INoirProfession profession = PlayerCallbacks.getProfessionFromString(noirPlayer, args[1]);
                int level = profession.getLevel();
                int currentXp = profession.getXp();
                int nextLevelXp = PlayerCallbacks.GetXpFromLevel(level + 1);
                int percentage = (int)(((float)currentXp / (float)nextLevelXp) * 100);

                bukkitPlayer.sendMessage(String.format("Level %d %s - %d%% [%dXP Required]", level, profession.getFirstLetterUppercase(), percentage, nextLevelXp - currentXp));
                return true;
            }
        }

        else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("reset") && PlayerClassList.playerClassList.contains(args[2])) {
                if (!bukkitPlayer.hasPermission("NoirLeveling.op")) {
                    commandSender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                }
                String playerName = args[1];
                NoirPlayer player = PlayerCallbacks.getNoirPlayerByName(playerName);
                if (player == null) {
                    bukkitPlayer.sendMessage("Player not found.");
                    return true;
                }

                INoirProfession profession = PlayerCallbacks.getProfessionFromString(player, args[2]);
                profession.setXp(0);
                bukkitPlayer.sendMessage("Success!");
                return true;
            }

            if (args[0].equalsIgnoreCase("class") && PlayerClassList.playerClassList.contains(args[1]) && args[2].equalsIgnoreCase("top")) {
                bukkitPlayer.sendMessage(PlayerCallbacks.getTopTenPlayersForProfession(args[1]));
                return true;
            }

            if (args[0].equalsIgnoreCase("set") && StringUtils.isNumeric(args[2])) {
                if (!bukkitPlayer.hasPermission("NoirLeveling.op")) {
                    commandSender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                }
                NoirPlayer player = PlayerCallbacks.getNoirPlayerByName(args[1]);

                if (player == null) {
                    bukkitPlayer.sendMessage("Player not found.");
                    return true;
                }

                int amount;
                try {
                    amount = NumberUtils.createInteger(args[2]);
                }
                catch (NumberFormatException e) {
                    return false;
                }
                if (amount < 0) {
                    amount = 0;
                }

                player.setXp(amount);
                bukkitPlayer.sendMessage("Success!");
                return true;
            }



        }

        else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("set") && PlayerClassList.playerClassList.contains(args[3])) {
                NoirPlayer player = PlayerCallbacks.getNoirPlayerByName(args[1]);
                if (player == null) {
                    bukkitPlayer.sendMessage("Player not found.");
                    return true;
                }

                int amount;
                try {
                    amount = NumberUtils.createInteger(args[2]);
                }
                catch (NumberFormatException e) {
                    return false;
                }
                if (amount < 0) {
                    amount = 0;
                }

                INoirProfession profession = PlayerCallbacks.getProfessionFromString(player, args[3]);
                if (profession == null) {
                    return false;
                }

                profession.setXp(amount);
                bukkitPlayer.sendMessage("Success!");
                return true;
            }
        }

        return false;

    }
}
