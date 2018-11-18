package nz.co.noirland.noirxp.customitems;

import org.bukkit.Server;

import java.util.List;

public class ItemCreator {

    public static void CreateCustomItems(Server server, List<ICustomItem> customItemList) {
        for (ICustomItem customItem : customItemList) {
            server.addRecipe(customItem.getRecipe());
        }
    }
}
