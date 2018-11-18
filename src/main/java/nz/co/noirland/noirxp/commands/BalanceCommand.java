package nz.co.noirland.noirxp.commands;

import nz.co.noirland.noirxp.NoirXP;
import net.ess3.api.IEssentials;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BalanceCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            return false;
        }
        if (!args[0].equalsIgnoreCase("avg")) {
            return false;
        }

        IEssentials essentials = (IEssentials) NoirXP.server.getPluginManager().getPlugin("Essentials");
        if (essentials == null) {
            return false;
        }

        List<BigDecimal> moneyList = new ArrayList<>();
        File userDataFolder = new File(essentials.getDataFolder(), "userdata");
        for (File file : userDataFolder.listFiles()) {
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            if (configuration.contains("money")) {
                BigDecimal money = new BigDecimal(configuration.getString("money"));
                moneyList.add(money);
            }

        }
        Double result = moneyList.stream().mapToDouble(BigDecimal::doubleValue).average().orElse(0.0);

        sender.sendMessage(result.toString());
        return true;


    }
}
