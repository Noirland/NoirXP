package nz.co.noirland.noirxp.constants;

import nz.co.noirland.noirxp.classes.LevelRequirementStats;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.HashMap;

public final class POTION_TYPES {
    public static HashMap<PotionEffectType, LevelRequirementStats> potionEffectStats = new HashMap<>();
    public static HashMap<PotionType, LevelRequirementStats> potionTypeStats = new HashMap<>();
    static {
        potionEffectStats.put(PotionEffectType.SLOW, new LevelRequirementStats(0, 6, 1,
                0,0,5));
        potionEffectStats.put(PotionEffectType.POISON, new LevelRequirementStats(0, 7, 1,
                0,0,5));
        potionEffectStats.put(PotionEffectType.SPEED, new LevelRequirementStats(0, 9, 1,
                0,0,10));
        potionEffectStats.put(PotionEffectType.HARM, new LevelRequirementStats(0, 10, 1,
                0,0,30));
        potionEffectStats.put(PotionEffectType.JUMP, new LevelRequirementStats(0, 10, 1,
                0,0,50));
        potionEffectStats.put(PotionEffectType.SLOW_FALLING, new LevelRequirementStats(0, 15, 1,
                0,0,50));
        potionEffectStats.put(PotionEffectType.NIGHT_VISION, new LevelRequirementStats(0, 17, 1,
                0,0,25));
        potionEffectStats.put(PotionEffectType.INCREASE_DAMAGE, new LevelRequirementStats(0, 20, 1,
                0,0,25));
        potionEffectStats.put(PotionEffectType.INVISIBILITY, new LevelRequirementStats(0, 25, 1,
                0,0,35));
        potionEffectStats.put(PotionEffectType.WATER_BREATHING, new LevelRequirementStats(0, 30, 1,
                0,0,35));
        potionEffectStats.put(PotionEffectType.REGENERATION, new LevelRequirementStats(0, 35, 1,
                0,0,200));
        potionEffectStats.put(PotionEffectType.HEAL, new LevelRequirementStats(0, 40, 1,
                0,0,50));
        potionEffectStats.put(PotionEffectType.FIRE_RESISTANCE, new LevelRequirementStats(0, 45, 1,
                0,0,60));

        potionTypeStats.put(PotionType.WEAKNESS, new LevelRequirementStats(0, 5, 1,
                0,0,5));
        potionTypeStats.put(PotionType.TURTLE_MASTER, new LevelRequirementStats(0, 12, 1,
                0,0,300));
    }
}
