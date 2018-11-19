package nz.co.noirland.noirxp.struct;

import nz.co.noirland.noirxp.constants.PlayerClass;
import org.bukkit.Material;

public class ItemXPData {

    public final String blockType;
    public final int levelToBreak;
    public final int levelToPlace;
    public final int levelToCreate;
    public final int levelToUse;
    public final int placeXP;
    public final int breakXP;
    public final int createXP;
    public final PlayerClass type;

    public ItemXPData(String blockType, int levelToBreak, int levelToPlace, int levelToCreate, int levelToUse, int placeXP, int breakXP, int createXP,
            PlayerClass type) {
        this.blockType = blockType;
        this.levelToBreak = levelToBreak;
        this.levelToPlace = levelToPlace;
        this.levelToCreate = levelToCreate;
        this.levelToUse = levelToUse;
        this.placeXP = placeXP;
        this.breakXP = breakXP;
        this.createXP = createXP;
        this.type = type;
    }

    public Material material() {
        return Material.getMaterial(blockType);
    }
}
