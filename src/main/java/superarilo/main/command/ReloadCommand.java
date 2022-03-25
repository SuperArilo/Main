package superarilo.main.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import superarilo.main.Main;
import superarilo.main.function.FileConfigs;
import java.util.Collections;
import java.util.List;

public class ReloadCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) return false;
        if(sender instanceof Player){
            if(sender.isOp()){
                return reloadComm(sender, args);
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("reload.not-op")));
                return true;
            }
        } else {
            return reloadComm(sender, args);
        }

    }

    private boolean reloadComm(@NotNull CommandSender sender, @NotNull String[] args) {
        if(args[0].equals("reload")){
            if (args.length <= 1){
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("reload.vacancy")));
                return true;
            }
            FileConfigs fileConfigs = new FileConfigs(Main.mainPlugin);
            switch (args[1]){
                case "all":
                    Main.mainPlugin.saveDefaultConfig();
                    Main.mainPlugin.reloadConfig();
                    if(fileConfigs.reloadAllConfigs()){
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("reload.config.success")));
                    } else {
                        sender.sendMessage(Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("reload.config.fail"));
                    }
                    break;
                case "sql":
                    if(fileConfigs.reloadSQL()){
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("reload.SQL.success")));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("reload.SQL.fail")));
                    }
                    break;
                case "socket":
                    if (fileConfigs.reloadSocket()){
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("reload.socket.success")));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("reload.socket.fail")));
                    }
                    break;
                case "redis":
                    if (fileConfigs.reloadRedis()){
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("reload.redis.success")));
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("reload.redis.fail")));
                    }
            }
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("reload.vacancy")));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            if (sender.isOp()){
                if (args.length == 1){
                    return Collections.singletonList("reload");
                }
                if(args.length == 2){
                    return FileConfigs.fileConfigs.get("commands").getStringList("reload.children");
                }
            }
        } else {
            if (args.length == 1){
                return Collections.singletonList("reload");
            }
            if(args.length == 2){
                return FileConfigs.fileConfigs.get("commands").getStringList("reload.children");
            }
        }
        return null;
    }
}
