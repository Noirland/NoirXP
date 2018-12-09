package nz.co.noirland.noirxp.constants;

import org.bukkit.ChatColor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum PlayerClass {
    ALCHEMY("Alchemy", ChatColor.DARK_PURPLE),
    BUILDING("Building", ChatColor.RED),
    COOKING("Cooking", ChatColor.YELLOW),
    FARMING("Farming", ChatColor.GREEN),
    FISHING("Fishing", ChatColor.BLUE),
    GATHERING("Gathering", ChatColor.DARK_GREEN),
    HUNTING("Hunting", ChatColor.DARK_RED),
    MINING("Mining", ChatColor.DARK_BLUE),
    SMITHING("Smithing", ChatColor.DARK_GRAY),
    TAMING("Taming", ChatColor.DARK_AQUA),
    GENERAL("Overall", ChatColor.YELLOW);

    private static final Map<String, PlayerClass> nameMapping = new HashMap<>();

    static {
        for(PlayerClass playerClass : PlayerClass.values()) {
            nameMapping.put(playerClass.name.toUpperCase(), playerClass);
        }
    }

    public static Optional<PlayerClass> fromName(String name) {
        return Optional.ofNullable(nameMapping.get(name.toUpperCase()));
    }

    private final String name;
    private final ChatColor color;

    PlayerClass(String name, ChatColor color) {
        this.name = name;
        this.color = color;
    }

    public String getTitle() {
        return name;
    }

    public String getTitleLower() {
        return name.toLowerCase();
    }

    public String getFormattedTitle() {
        return getColor() + getTitle() + ChatColor.WHITE;
    }

    public String getFormattedLower() {
        return getColor() + getTitleLower() + ChatColor.WHITE;
    }

    public ChatColor getColor() {
        return color;
    }

    public static Collection<String> getNames() {
        return Collections.unmodifiableCollection(nameMapping.keySet());
    }
}
