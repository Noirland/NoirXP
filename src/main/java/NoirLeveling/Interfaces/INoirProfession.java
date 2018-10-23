package NoirLeveling.Interfaces;

public interface INoirProfession {
    int getLevel();
    void setLevel(int level);
    int getXp();
    void setXp(int xp);
    void addXp(int xp);
    boolean isLevelUp(int oldxp, int newxp);
    String getLowercaseName();
    String getNameTitle();
    String getFirstLetterUppercase();
}
