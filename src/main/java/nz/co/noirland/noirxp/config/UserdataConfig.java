package nz.co.noirland.noirxp.config;

import nz.co.noirland.noirxp.NoirXP;
import nz.co.noirland.zephcore.Config;
import nz.co.noirland.zephcore.Debug;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class UserdataConfig extends Config {

    private static UserdataConfig inst;

    public UserdataConfig() {
        super("userdata.yml");
    }

    public static UserdataConfig inst() {
        if(inst == null) {
            inst = new UserdataConfig();
        }
        return inst;
    }

    @Override
    protected Plugin getPlugin() {
        return NoirXP.inst();
    }

    @Override
    protected Debug getDebug() {
        return NoirXP.debug();
    }

    public boolean isLevelling(UUID player) {
        return config.getBoolean(player.toString() + ".leveling", true);
    }

    public boolean isVerbose(UUID player) {
        return config.getBoolean(player.toString() + ".verbose", false);
    }

    public void setLeveling(UUID player, boolean bool) {
        config.set(player.toString() + ".leveling", bool);
        save();
    }

    public void setVerbose(UUID player, boolean bool) {
        config.set(player.toString() + ".verbose", bool);
        save();
    }


}
