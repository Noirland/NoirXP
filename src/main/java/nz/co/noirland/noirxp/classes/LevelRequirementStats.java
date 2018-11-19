package nz.co.noirland.noirxp.classes;

public class LevelRequirementStats {
    public int levelToBreak;
    public int levelToCreate;
    public int levelToPlace;
    public int placeXp;
    public int breakXp;
    public int createXp;

    public LevelRequirementStats(int levelToBreak, int levelToCreate, int levelToPlace, int placeXp, int breakXp, int createXp) {
        this.levelToBreak = levelToBreak;
        this.levelToCreate = levelToCreate;
        this.levelToPlace = levelToPlace;
        this.placeXp = placeXp;
        this.breakXp = breakXp;
        this.createXp = createXp;
    }
}
