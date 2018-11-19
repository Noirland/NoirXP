package nz.co.noirland.noirxp.database.queries;

public class ItemXPDataQuery extends XPQuery {

    public ItemXPDataQuery(String blockType) {
        super(1, "SELECT * FROM CustomBlocks WHERE name = ?");

        setValue(1, blockType);
    }
}
