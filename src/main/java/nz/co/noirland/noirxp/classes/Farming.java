package nz.co.noirland.noirxp.classes;

import nz.co.noirland.noirxp.callbacks.PlayerCallbacks;
import nz.co.noirland.noirxp.interfaces.INoirProfession;

public class Farming implements INoirProfession {
    private int level = 1;
    private int xp = 0;
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
        this.level = PlayerCallbacks.getLevelFromXp(this.xp);
    }

    @Override
    public void addXp(int xp) {
        this.xp += xp;
        this.level = PlayerCallbacks.getLevelFromXp(this.xp);
    }

    @Override
    public boolean isLevelUp(int oldxp, int newxp) {
        int oldLevel = PlayerCallbacks.getLevelFromXp(oldxp);
        int newLevel = PlayerCallbacks.getLevelFromXp(newxp);

        return newLevel > oldLevel;
    }

    @Override
    public String getLowercaseName() {
        return "farming";
    }

    @Override
    public String getNameTitle() {
        return "Farmer";
    }

    @Override
    public String getFirstLetterUppercase() {
        return "Farming";
    }
}
