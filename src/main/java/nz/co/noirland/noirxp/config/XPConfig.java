package nz.co.noirland.noirxp.config;

import nz.co.noirland.noirxp.NoirXP;
import nz.co.noirland.zephcore.Config;
import nz.co.noirland.zephcore.Debug;
import org.bukkit.plugin.Plugin;

public class XPConfig extends Config {

    private static XPConfig inst;

    private XPConfig() {
        super("config.yml");
    }

    @Override
    protected Plugin getPlugin() {
        return NoirXP.inst();
    }

    @Override
    protected Debug getDebug() {
        return NoirXP.debug();
    }

    public static XPConfig inst() {
        if(inst == null) {
            inst = new XPConfig();
        }
        return inst;
    }

    public String getDBUser()     { return config.getString("username"); }
    public String getDBPassword() { return config.getString("password"); }
    public int    getDBPort()     { return config.getInt   ("port", 3306); }
    public String getDBHost()     { return config.getString("host", "localhost"); }

}
