package nz.co.noirland.noirxp.database.queries;

public class TameDataQuery extends XPQuery {

    public TameDataQuery() {
        super("SELECT entityName, levelToTame, levelToBreed, tameXp, breedXp FROM NoirLive.CustomTameBreedData;");
    }
}
