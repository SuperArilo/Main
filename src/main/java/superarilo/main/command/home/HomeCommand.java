package superarilo.main.command.home;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import superarilo.main.Main;
import superarilo.main.function.FileConfigs;
import superarilo.main.gui.home.ShowHomeList;

public class HomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player){
            if (s.equals("home") || s.equals("homes")){
                if (strings.length == 0){
                    new ShowHomeList((Player) commandSender).open();
                    return true;
                } else if (strings.length == 1){
                    System.out.println("手动传送家");
                    return true;
                } else {
                    command.setUsage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("commands").getString("home.usage")));
                    return false;
                }
            } else {
                return false;
            }
        } else {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("home.not-player")));
            return true;
        }
    }
}
