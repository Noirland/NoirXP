package nz.co.noirland.noirxp.commands;

import nz.co.noirland.libs.acf.BaseCommand;
import nz.co.noirland.libs.acf.CommandHelp;
import nz.co.noirland.libs.acf.CommandIssuer;
import nz.co.noirland.libs.acf.annotation.CommandAlias;
import nz.co.noirland.libs.acf.annotation.CommandCompletion;
import nz.co.noirland.libs.acf.annotation.CommandPermission;
import nz.co.noirland.libs.acf.annotation.Default;
import nz.co.noirland.libs.acf.annotation.Description;
import nz.co.noirland.libs.acf.annotation.HelpCommand;
import nz.co.noirland.libs.acf.annotation.Subcommand;
import nz.co.noirland.libs.acf.annotation.Syntax;
import nz.co.noirland.noirxp.NoirXP;
import nz.co.noirland.noirxp.callbacks.InventoryCallbacks;
import nz.co.noirland.noirxp.callbacks.PlayerCallbacks;
import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.config.UserdataConfig;
import nz.co.noirland.noirxp.constants.PlayerClass;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@CommandAlias("noir")
public class CommandNoir extends BaseCommand {

    @Subcommand("level top")
    @Description("Get top overall players")
    public void levelTop(CommandIssuer issuer) {
        for(String m : PlayerCallbacks.getTopTenPlayers()) issuer.sendMessage(m);
    }

    @Subcommand("level")
    @Description("Get your overall level")
    public void level(Player player) {
        player.sendMessage(getRemainingXPString(PlayerClass.GENERAL, noirPlayer(player)));
    }

    @Subcommand("convert")
    @CommandAlias("nconvert")
    @Description("Fix all item tags in your inventory")
    public void convert(Player player) {
        InventoryCallbacks.addTagsToPlayerInventory(player);
    }

    @Subcommand("show")
    @Description("Send message whenever XP is gained")
    public void show(Player player) {
        UserdataConfig.inst().setVerbose(player.getUniqueId(), true);
        player.sendMessage("Verbose mode ENABLED.");
    }

    @Subcommand("hide")
    @Description("Disable sending message whenever XP is gained")
    public void hide(Player player) {
        UserdataConfig.inst().setVerbose(player.getUniqueId(), false);
        player.sendMessage("Verbose mode DISABLED.");
    }

    @Subcommand("class top")
    @Description("Get top players for a class")
    @CommandCompletion("@playerclass")
    @Syntax("<class>")
    public void classTop(CommandIssuer issuer, PlayerClass playerClass) {
        for(String message : PlayerCallbacks.getTopTenPlayersForProfession(playerClass)) issuer.sendMessage(message);
    }

    @Subcommand("class")
    @CommandCompletion("@playerclass")
    @Description("Get a class's level")
    @Syntax("<class>")
    public void classInfo(Player player, PlayerClass playerClass) {
        player.sendMessage(getRemainingXPString(playerClass, noirPlayer(player)));
    }

    @Subcommand("disable")
    @CommandPermission("NoirLeveling.op")
    @CommandCompletion("@players")
    @Description("Disable levelling for a player")
    @Syntax("<player>")
    public void disable(CommandIssuer issuer, OfflinePlayer player) {
        UserdataConfig.inst().setLeveling(player.getUniqueId(), false);
        issuer.sendMessage("Leveling has been DISABLED.");
    }

    @Subcommand("enable")
    @CommandPermission("NoirLeveling.op")
    @CommandCompletion("@players")
    @Description("Enable levelling for a player")
    @Syntax("<player>")
    public void enable(CommandIssuer issuer, OfflinePlayer player) {
        UserdataConfig.inst().setLeveling(player.getUniqueId(), true);
        issuer.sendMessage("Leveling has been ENABLED.");
    }

    @Subcommand("reset")
    @CommandPermission("NoirLeveling.op")
    @CommandCompletion("@players")
    @Description("Reset a player's levels to 0")
    @Syntax("<player>")
    public void reset(CommandIssuer issuer, OfflinePlayer player) {
        NoirXP.getPlayer(player.getUniqueId()).resetLevels();
        issuer.sendMessage("Success!");
    }

    @Subcommand("set")
    @CommandCompletion("@players  @playerclass")
    @CommandPermission("NoirLeveling.op")
    @Description("Set a player's overall or skill level")
    @Syntax("<player> <xp> [class]")
    public void set(CommandIssuer issuer, OfflinePlayer player, Integer xp,
            @Default("general") PlayerClass playerClass) {
        if (xp < 0) {
            xp = 0;
        }
        noirPlayer(player).setXP(playerClass, xp);
        issuer.sendMessage("Success!");
    }

    @HelpCommand
    @Syntax("<page>")
    public void help(CommandHelp help) {
        help.showHelp();
    }

    private NoirPlayer noirPlayer(OfflinePlayer player) {
        return NoirXP.getPlayer(player.getUniqueId());
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
