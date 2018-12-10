package nz.co.noirland.noirxp.commands;

import nz.co.noirland.libs.acf.BaseCommand;
import nz.co.noirland.libs.acf.CommandHelp;
import nz.co.noirland.libs.acf.annotation.CommandAlias;
import nz.co.noirland.libs.acf.annotation.CommandCompletion;
import nz.co.noirland.libs.acf.annotation.CommandPermission;
import nz.co.noirland.libs.acf.annotation.Default;
import nz.co.noirland.libs.acf.annotation.Description;
import nz.co.noirland.libs.acf.annotation.Flags;
import nz.co.noirland.libs.acf.annotation.HelpCommand;
import nz.co.noirland.libs.acf.annotation.Optional;
import nz.co.noirland.libs.acf.annotation.Subcommand;
import nz.co.noirland.libs.acf.annotation.Syntax;
import nz.co.noirland.noirxp.NoirXP;
import nz.co.noirland.noirxp.callbacks.InventoryCallbacks;
import nz.co.noirland.noirxp.callbacks.PlayerCallbacks;
import nz.co.noirland.noirxp.classes.NoirPlayer;
import nz.co.noirland.noirxp.config.UserdataConfig;
import nz.co.noirland.noirxp.constants.PlayerClass;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("noir")
public class CommandNoir extends BaseCommand {

    @Subcommand("level top")
    @Description("Get top overall players")
    @Syntax("<class>")
    @CommandCompletion("@playerclass")
    public void levelTop(CommandSender sender, @Default("overall") @Optional PlayerClass playerClass) {
        for(String m : PlayerCallbacks.getTopTenPlayersForProfession(playerClass)) sender.sendMessage(m);
    }

    @Subcommand("level")
    @Description("Get a player's overall level")
    @Syntax("<class> <player>")
    @CommandCompletion("@playerclass @players")
    public void level(CommandSender sender, @Default("overall") @Optional PlayerClass playerClass,
            @Optional @Flags("defaultself") NoirPlayer player) {
        sender.sendMessage(getRemainingXPString(playerClass, player));
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

    @Subcommand("disable")
    @CommandPermission("NoirLeveling.op")
    @CommandCompletion("@players")
    @Description("Disable levelling for a player")
    @Syntax("<player>")
    public void disable(CommandSender sender, @Optional @Flags("defaultself") NoirPlayer player) {
        UserdataConfig.inst().setLeveling(player.getUniqueId(), false);
        sender.sendMessage("Leveling has been DISABLED.");
    }

    @Subcommand("enable")
    @CommandPermission("NoirLeveling.op")
    @CommandCompletion("@players")
    @Description("Enable levelling for a player")
    @Syntax("<player>")
    public void enable(CommandSender sender, @Optional @Flags("defaultself") NoirPlayer player) {
        UserdataConfig.inst().setLeveling(player.getUniqueId(), true);
        sender.sendMessage("Leveling has been ENABLED.");
    }

    @Subcommand("reset")
    @CommandPermission("NoirLeveling.op")
    @CommandCompletion("@players")
    @Description("Reset a player's levels to 0")
    @Syntax("<player>")
    public void reset(CommandSender sender, @Optional @Flags("defaultself") NoirPlayer player) {
        player.resetLevels();
        sender.sendMessage("Success!");
    }

    @Subcommand("set")
    @CommandCompletion("@players  @playerclass")
    @CommandPermission("NoirLeveling.op")
    @Description("Set a player's overall or skill level")
    @Syntax("<player> <xp> [class]")
    public void set(CommandSender sender, @Optional @Flags("defaultself") NoirPlayer player, Integer xp,
            @Default("general") PlayerClass playerClass) {
        if (xp < 0) {
            xp = 0;
        }
        player.setXP(playerClass, xp);
        sender.sendMessage("Success!");
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
