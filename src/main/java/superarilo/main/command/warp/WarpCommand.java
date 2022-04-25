package superarilo.main.command.warp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import superarilo.main.function.FileConfigs;
import superarilo.main.function.FunctionTool;
import superarilo.main.gui.warp.WarpMenu;

public class WarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player){
            if (!label.equals("warp")) return false;
            if (args.length != 0){
                command.setUsage(FileConfigs.fileConfigs.get("commands").getString("warp.usage", "null"));
                return false;
            } else {
                new WarpMenu((Player) sender).open();
                return true;
            }
        } else {
            sender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("warp.not-player"), null));
            return true;
        }
    }
}
