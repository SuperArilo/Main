package superarilo.main.command.back;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import superarilo.main.Main;
import superarilo.main.function.FileConfigs;
import superarilo.main.function.TeleporThread;

public class BackCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player){
            if (!s.equals("back")) return false;
            if (strings.length == 0){
                if (Main.redisValue.exists(((Player) commandSender).getUniqueId() + "_back")) {
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("teleport.teleporting")));
                    new TeleporThread((Player) commandSender, TeleporThread.Type.BACK).teleport();
                } else {
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("back.no-back")));
                }
                return true;
            } else {
                command.setUsage(ChatColor.translateAlternateColorCodes('&',Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("commands").getString("back.usage")));
                return false;
            }
        } else {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("back.not-player")));
            return true;
        }
    }
}