package superarilo.main.command.home;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import superarilo.main.Main;
import superarilo.main.function.FileConfigs;
import superarilo.main.function.FunctionTool;
import superarilo.main.function.MatchHomeId;
import superarilo.main.function.home.Impl.HomeManagerImpl;

public class SetHomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player){
            if (!s.equals("sethome")) return false;
            if (strings.length == 1){
                if (MatchHomeId.isEnglish(strings[0])) {
                    if (!FileConfigs.fileConfigs.get("home").getStringList("disable-world").contains(((Player) commandSender).getWorld().getName())) {
                        new HomeManagerImpl((Player) commandSender).createNewHome(strings[0]);
                    } else {
                        commandSender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("sethome.not-allow")));
                    }
                } else {
                    commandSender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("sethome.illegal")));
                }
                return true;
            } else {
                command.setUsage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("commands").getString("sethome.usage")));
                return false;
            }
        } else {
            commandSender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("sethome.not-player")));
            return true;
        }
    }
}
