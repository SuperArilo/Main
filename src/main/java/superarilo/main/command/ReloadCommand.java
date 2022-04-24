package superarilo.main.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import superarilo.main.function.FileConfigs;
import superarilo.main.function.FunctionTool;
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
                sender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("reload.not-op"), null));
                return true;
            }
        } else {
            return reloadComm(sender, args);
        }

    }

    private boolean reloadComm(@NotNull CommandSender sender, @NotNull String[] args) {
        if(args[0].equals("reload")){
            if (args.length <= 1){
                sender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("reload.vacancy"), null));
                return true;
            }
            FileConfigs fileConfigs = new FileConfigs();
            switch (args[1]){
                case "all": {
                    if (fileConfigs.reloadAllConfigs()) {
                        sender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("reload.config.success"), null));
                    } else {
                        sender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("reload.config.fail"), null));
                    }
                }
                    break;
                case "sql": {
                    if (fileConfigs.reloadSQL()) {
                        sender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("reload.SQL.success"), null));
                    } else {
                        sender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("reload.SQL.fail"), null));
                    }
                }
                    break;
                case "socket":
                    if (fileConfigs.reloadSocket()){
                        sender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("reload.socket.success"), null));
                    } else {
                        sender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("reload.socket.fail"), null));
                    }
                    break;
                case "redis":
                    if (fileConfigs.reloadRedis()){
                        sender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("reload.redis.success"), null));
                    } else {
                        sender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("reload.redis.fail"), null));
                    }
            }
        } else {
            sender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("reload.vacancy"), null));
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
