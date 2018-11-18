package nz.co.noirland.noirxp.classes;

public class LevelRequirementStats {
    public int levelToBreak = 0;
    public int levelToCreate = 0;
    public int levelToPlace = 0;
    public int placeXp = 0;
    public int breakXp = 0;
    public int createXp = 0;

    public LevelRequirementStats(int levelToBreak, int levelToCreate, int levelToPlace, int placeXp, int breakXp, int createXp) {
        this.levelToBreak = levelToBreak;
        this.levelToCreate = levelToCreate;
        this.levelToPlace = levelToPlace;
        this.placeXp = placeXp;
        this.breakXp = breakXp;
        this.createXp = createXp;
    }
}
