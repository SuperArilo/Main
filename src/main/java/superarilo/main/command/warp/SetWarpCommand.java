package superarilo.main.command.warp;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import superarilo.main.Main;
import superarilo.main.function.FileConfigs;
import superarilo.main.function.FunctionTool;
import superarilo.main.function.MatchId;
import superarilo.main.function.warp.Impl.WarpManagerImpl;

public class SetWarpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            if (!s.equals("setwarp")) return false;
            if(strings.length != 1){
                command.setUsage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("commands").getString("setwarp.usage")));
                return false;
            } else {
                if (MatchId.isEnglish(strings[0])){
                    new WarpManagerImpl((Player) commandSender).createNewWarp(strings[0]);
                } else {
                    commandSender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("setwarp.illegal"), null));
                }
            }
        } else {
            commandSender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("setwarp.not-player"), null));
        }
        return true;
    }
}
