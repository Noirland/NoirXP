package nz.co.noirland.noirxp.constants;

import java.util.HashSet;

public class PlayerClassList {
    public static HashSet<String> playerClassList = new HashSet<>();

    static {
        playerClassList.add("alchemy");
        playerClassList.add("building");
        playerClassList.add("cooking");
        playerClassList.add("farming");
        playerClassList.add("fishing");
        playerClassList.add("gathering");
        playerClassList.add("hunting");
        playerClassList.add("mining");
        playerClassList.add("smithing");
        playerClassList.add("taming");
    }
}
