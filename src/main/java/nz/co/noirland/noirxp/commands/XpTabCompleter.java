package nz.co.noirland.noirxp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class XpTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command cmd, String label, String[] args) {
        List<String> validArgs = new ArrayList<String>(){{
            add("classtop");
            add("alchemy");
            add("building");
            add("mining");
            add("farming");
            add("fishing");
            add("smithing");
            add("gathering");
            add("hunting");
            add("taming");
            add("cooking");
        }};

        List<String> newArgList = new ArrayList<>();

        for (String item : validArgs) {
            if (item.toLowerCase().startsWith(args[0].toLowerCase())) {
                newArgList.add(item);
            }
        }

        return newArgList;
    }
}
