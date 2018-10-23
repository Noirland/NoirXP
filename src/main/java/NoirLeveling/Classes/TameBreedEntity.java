package NoirLeveling.Classes;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class TameBreedEntity {
    private EntityType entityType;
    private int levelToTame;
    private int levelToBreed;
    private int tameXp;
    private int breedXp;

    public TameBreedEntity(EntityType entityType, int levelToTame, int levelToBreed, int tameXp, int breedXp) {
        this.entityType = entityType;
        this.levelToTame = levelToTame;
        this.levelToBreed = levelToBreed;
        this.tameXp = tameXp;
        this.breedXp = breedXp;
    }

    public int getLevelToTame() {
        return levelToTame;
    }

    public void setLevelToTame(int levelToTame) {
        this.levelToTame = levelToTame;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public int getLevelToBreed() {
        return levelToBreed;
    }

    public void setLevelToBreed(int levelToBreed) {
        this.levelToBreed = levelToBreed;
    }

    public int getTameXp() {
        return tameXp;
    }

    public void setTameXp(int tameXp) {
        this.tameXp = tameXp;
    }

    public int getBreedXp() {
        return breedXp;
    }

    public void setBreedXp(int breedXp) {
        this.breedXp = breedXp;
    }
}
