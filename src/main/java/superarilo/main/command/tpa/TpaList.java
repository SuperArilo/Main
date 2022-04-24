package superarilo.main.command.tpa;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import superarilo.main.Main;
import superarilo.main.function.FileConfigs;
import superarilo.main.function.FunctionTool;
import superarilo.main.gui.tpa.ShowPlayerList;

public class TpaList implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player){
            if(!s.equals("tpalist")) return false;
            if (strings.length == 0){
                new ShowPlayerList((Player) commandSender).open();
                return true;
            } else {
                command.setUsage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("commands").getString("tpalist.usage","使用方法")));
                return false;
            }
        } else {
            commandSender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("tpalist.not-player"), null));
            return true;
        }
    }
}
