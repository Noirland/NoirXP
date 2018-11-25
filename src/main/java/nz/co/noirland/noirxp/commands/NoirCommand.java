package nz.co.noirland.noirxp.commands;

import nz.co.noirland.noirxp.NoirXP;
import nz.co.noirland.noirxp.callbacks.InventoryCallbacks;
import nz.co.noirland.noirxp.callbacks.PlayerCallbacks;
import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.config.UserdataConfig;
import nz.co.noirland.noirxp.constants.PlayerClass;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class NoirCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This is a player-only command.");
            return true;
        }
        Player bukkitPlayer = (Player) commandSender;
        NoirPlayer noirPlayer = NoirXP.players.get(bukkitPlayer.getUniqueId().toString());
        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "level":
                    bukkitPlayer.sendMessage(getRemainingXPString(PlayerClass.GENERAL, noirPlayer));
                    return true;
                case "disable":
                    if (!bukkitPlayer.hasPermission("NoirLeveling.op")) {
                        commandSender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                        return true;
                    }
                    UserdataConfig.inst().setLeveling(bukkitPlayer.getUniqueId(), false);
                    bukkitPlayer.sendMessage("Leveling has been DISABLED.");
                    return true;
                case "enable":
                    if (!bukkitPlayer.hasPermission("NoirLeveling.op")) {
                        commandSender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                        return true;
                    }
                    UserdataConfig.inst().setLeveling(bukkitPlayer.getUniqueId(), true);
                    bukkitPlayer.sendMessage("Leveling has been ENABLED.");
                    return true;
                case "reset":
                    if (!bukkitPlayer.hasPermission("NoirLeveling.op")) {
                        commandSender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                        return true;
                    }
                    noirPlayer.resetLevels();
                    bukkitPlayer.sendMessage("Success!");
                    return true;
                case "convert":
                    InventoryCallbacks.addTagsToPlayerInventory(bukkitPlayer);
                    return true;
                case "show":
                    UserdataConfig.inst().setVerbose(bukkitPlayer.getUniqueId(), true);
                    bukkitPlayer.sendMessage("Verbose mode ENABLED.");
                    return true;
                case "hide":
                    UserdataConfig.inst().setVerbose(bukkitPlayer.getUniqueId(), false);
                    bukkitPlayer.sendMessage("Verbose mode DISABLED.");
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
                OfflinePlayer player = NoirXP.inst().getServer().getOfflinePlayer(playerName);
                NoirXP.getPlayer(player.getUniqueId()).resetLevels();
                bukkitPlayer.sendMessage("Success!");
                return true;
            }

            if (args[0].equalsIgnoreCase("class")) {
                Optional<PlayerClass> optClass = PlayerClass.fromName(args[1]);
                if(!optClass.isPresent()) return false;

                bukkitPlayer.sendMessage(getRemainingXPString(optClass.get(), noirPlayer));
                return true;
            }
        }

        else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("reset")) {
                Optional<PlayerClass> optClass = PlayerClass.fromName(args[2]);
                if(!optClass.isPresent()) return false;

                if (!bukkitPlayer.hasPermission("NoirLeveling.op")) {
                    commandSender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                }
                String playerName = args[1];
                NoirPlayer player = PlayerCallbacks.getNoirPlayerByName(playerName);
                if (player == null) {
                    bukkitPlayer.sendMessage("Player not found.");
                    return true;
                }

                noirPlayer.setXP(optClass.get(), 0);
                bukkitPlayer.sendMessage("Success!");
                return true;
            }

            if (args[0].equalsIgnoreCase("class") && args[2].equalsIgnoreCase("top")) {
                Optional<PlayerClass> optClass = PlayerClass.fromName(args[1]);
                if(!optClass.isPresent()) return false;
                for(String message : PlayerCallbacks.getTopTenPlayersForProfession(optClass.get())) bukkitPlayer.sendMessage(message);
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

                player.setXP(PlayerClass.GENERAL, amount);
                bukkitPlayer.sendMessage("Success!");
                return true;
            }



        }

        else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("set")) {
                Optional<PlayerClass> optClass = PlayerClass.fromName(args[3]);
                if(!optClass.isPresent()) return false;

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

                noirPlayer.setXP(optClass.get(), amount);
                bukkitPlayer.sendMessage("Success!");
                return true;
            }
        }

        return false;

    }

    private static String getRemainingXPString(PlayerClass playerClass, NoirPlayer player) {
        int currentLevel = player.getLevel(playerClass);
        int currentXp = player.getXP(playerClass);

        int currentLevelXp = PlayerCallbacks.getXpFromLevel(currentLevel);
        int nextLevelXp = PlayerCallbacks.getXpFromLevel(currentLevel + 1);

        int requiredXp = nextLevelXp - currentXp;

        double progress = (double) requiredXp / (nextLevelXp - currentLevelXp);

        int remaining = (int) ((1 - progress) * 100);

        return String.format("Level %d %s - %d%% [%dXP Required]",
                currentLevel,
                playerClass.getTitle(),
                remaining,
                requiredXp);
    }
}
