package nz.co.noirland.noirxp.database.queries;

import nz.co.noirland.noirxp.database.XPDatabase;
import nz.co.noirland.zephcore.database.mysql.MySQLDatabase;
import nz.co.noirland.zephcore.database.mysql.MySQLQuery;

public class XPQuery extends MySQLQuery {

    public XPQuery(int nargs, String query) {
        super(nargs, query);
    }

    public XPQuery(String query) {
        super(query);
    }

    public XPQuery(Object[] values, String query) {
        super(values, query);
    }

    @Override
    protected MySQLDatabase getDB() {
        return XPDatabase.inst();
    }
}
