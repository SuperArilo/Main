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
                sender.sendMessage("抱歉，你没有权限执行此命令！");
                return false;
            }
        } else {
            return reloadComm(sender, args);
        }

    }

    private boolean reloadComm(@NotNull CommandSender sender, @NotNull String[] args) {
        if(args[0].equals("reload")){
            Main.mainPlugin.reloadConfig();
            if (args.length <= 1){
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("reload.vacancy")));
                return true;
            }
            String argsTwo = args[1];
            FileConfigs fileConfigs = new FileConfigs(Main.mainPlugin);
            switch (argsTwo){
                case "all":
                    Main.mainPlugin.reloadConfig();
                    if(fileConfigs.reloadAllConfigs()){
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',FileConfigs.fileConfigs.get("message").getString("reload.config.success")));
                    } else {
                        sender.sendMessage(FileConfigs.fileConfigs.get("message").getString("reload.config.fail"));
                    }
                    break;
                case "sql":
                    if(fileConfigs.reloadSQL()){
                        sender.sendMessage(ChatColor.GREEN + "SQL已重启成功！");
                    } else {
                        sender.sendMessage(ChatColor.RED + "SQL重启失败，请检查控制台！");
                    }
                    break;
                case "socket":
                    if (fileConfigs.reloadSocket()){
                        sender.sendMessage(ChatColor.GREEN + "socket已重启成功！");
                    } else {
                        sender.sendMessage(ChatColor.RED + "socket重启失败，请检查控制台！");
                    }
                    break;
                case "redis":
                    if (fileConfigs.reloadRedis()){
                        sender.sendMessage(ChatColor.GREEN + "redis已重启成功！");
                    } else {
                        sender.sendMessage(ChatColor.RED + "redis重启失败，请检查控制台！");
                    }
            }
            return true;
        } else {
            sender.sendMessage("你输入的指令有误，请检查！");
            return false;
        }
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
