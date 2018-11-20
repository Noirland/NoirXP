package nz.co.noirland.noirxp.database.queries;

public class GetCustomBlocksQuery extends XPQuery {

    public GetCustomBlocksQuery() {
        super("SELECT * FROM CustomBlocks;");
    }
}
