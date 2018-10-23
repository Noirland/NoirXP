package NoirLeveling.Classes;

import NoirLeveling.Callbacks.PlayerCallbacks;
import NoirLeveling.Interfaces.INoirProfession;

public class Fishing implements INoirProfession {
    private int level = 1;
    private int xp;
    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
        this.xp = PlayerCallbacks.GetXpFromLevel(this.level);
    }

    @Override
    public int getXp() {
        return this.xp;
    }

    @Override
    public void setXp(int xp) {
        this.xp = xp;
        this.level = PlayerCallbacks.GetLevelFromXp(this.xp);
    }

    @Override
    public void addXp(int xp) {
        this.xp += xp;
        this.level = PlayerCallbacks.GetLevelFromXp(this.xp);
    }

    @Override
    public boolean isLevelUp(int oldxp, int newxp) {
        int oldLevel = PlayerCallbacks.GetLevelFromXp(oldxp);
        int newLevel = PlayerCallbacks.GetLevelFromXp(newxp);

        if (newLevel > oldLevel) {
            return true;
        }
        return false;
    }

    @Override
    public String getLowercaseName() {
        return "fishing";
    }

    @Override
    public String getNameTitle() {
        return "Fisherman";
    }

    @Override
    public String getFirstLetterUppercase() {
        return "Fishing";
    }
}
