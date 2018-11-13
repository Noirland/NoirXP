package NoirLeveling.Helpers;

import NoirLeveling.Constants.PlayerClass;
import org.bukkit.ChatColor;

public final class PlayerClassConverter {
    /**
     * Formats a profession to its title
     * @param playerClass The profession to format
     * @return A chatcolour-formatted profession title
     */
    public static String playerClassToTitle(PlayerClass playerClass) {
        switch (playerClass) {
            case GATHERING:
                return ChatColor.DARK_GREEN + "Gatherer" + ChatColor.WHITE;
            case SMITHING:
                return ChatColor.DARK_GRAY + "Blacksmith" + ChatColor.WHITE;
            case BUILDING:
                return ChatColor.RED + "Builder" + ChatColor.WHITE;
            case HUNTING:
                return ChatColor.DARK_RED + "Hunter" + ChatColor.WHITE;
            case FISHING:
                return ChatColor.BLUE + "Fisherman" + ChatColor.WHITE;
            case FARMING:
                return ChatColor.GREEN + "Farmer" + ChatColor.WHITE;
            case COOKING:
                return ChatColor.YELLOW + "Chef" + ChatColor.WHITE;
            case ALCHEMY:
                return ChatColor.DARK_PURPLE + "Alchemist" + ChatColor.WHITE;
            case TAMING:
                return ChatColor.DARK_AQUA + "Tamer" + ChatColor.WHITE;
            case MINING:
                return ChatColor.GOLD + "Miner" + ChatColor.WHITE;
            case GYPSY:
                return ChatColor.LIGHT_PURPLE + "Gypsy" + ChatColor.WHITE;
            case GENERAL:
                return ChatColor.YELLOW + "error" + ChatColor.WHITE;
            default:
                return "error";
        }
    }

    /**
     * Formats a profession to its lowercase description
     * @param playerClass The profession to format
     * @return A chatcolour-formatted lowercase profession description
     */
    public static String playerClassToString(PlayerClass playerClass) {
        switch (playerClass) {
            case GATHERING:
                return ChatColor.DARK_GREEN + "gathering" + ChatColor.WHITE;
            case SMITHING:
                return ChatColor.DARK_GRAY + "smithing" + ChatColor.WHITE;
            case BUILDING:
                return ChatColor.RED + "building" + ChatColor.WHITE;
            case HUNTING:
                return ChatColor.DARK_RED + "hunting" + ChatColor.WHITE;
            case FISHING:
                return ChatColor.BLUE + "fishing" + ChatColor.WHITE;
            case FARMING:
                return ChatColor.GREEN + "farming" + ChatColor.WHITE;
            case COOKING:
                return ChatColor.YELLOW + "cooking" + ChatColor.WHITE;
            case ALCHEMY:
                return ChatColor.DARK_PURPLE + "alchemy" + ChatColor.WHITE;
            case TAMING:
                return ChatColor.DARK_AQUA + "taming" + ChatColor.WHITE;
            case MINING:
                return ChatColor.GOLD + "mining" + ChatColor.WHITE;
            case GYPSY:
                return ChatColor.LIGHT_PURPLE + "gypsy" + ChatColor.WHITE;
            case GENERAL:
                return ChatColor.YELLOW + "overall" + ChatColor.WHITE;
            default:
                return "error";
        }
    }

    /**
     * Formats a profession to its first-letter uppercase description
     * @param playerClass The profession to format
     * @return A chatcolour-formatted first-letter uppercase profession description
     */
    public static String playerClassToCapitalString(PlayerClass playerClass) {
        switch (playerClass) {
            case GATHERING:
                return ChatColor.DARK_GREEN + "Gathering" + ChatColor.WHITE;
            case SMITHING:
                return ChatColor.DARK_GRAY + "Smithing" + ChatColor.WHITE;
            case BUILDING:
                return ChatColor.RED + "Building" + ChatColor.WHITE;
            case HUNTING:
                return ChatColor.DARK_RED + "Hunting" + ChatColor.WHITE;
            case FISHING:
                return ChatColor.BLUE + "Fishing" + ChatColor.WHITE;
            case FARMING:
                return ChatColor.GREEN + "Farming" + ChatColor.WHITE;
            case COOKING:
                return ChatColor.YELLOW + "Cooking" + ChatColor.WHITE;
            case ALCHEMY:
                return ChatColor.DARK_PURPLE + "Alchemy" + ChatColor.WHITE;
            case TAMING:
                return ChatColor.DARK_AQUA + "Taming" + ChatColor.WHITE;
            case MINING:
                return ChatColor.GOLD + "Mining" + ChatColor.WHITE;
            case GYPSY:
                return ChatColor.LIGHT_PURPLE + "Gypsy" + ChatColor.WHITE;
            case GENERAL:
                return ChatColor.YELLOW + "General" + ChatColor.WHITE;
            default:
                return ChatColor.YELLOW + "General" + ChatColor.WHITE;
        }
    }
}
