package superarilo.main.command.warp;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import superarilo.main.Main;
import superarilo.main.function.FileConfigs;
import superarilo.main.function.FunctionTool;
import superarilo.main.gui.warp.WarpMenu;

public class SetWarpCommand implements CommandExecutor {

    private final String serverPrefix = Main.mainPlugin.getConfig().getString("prefix");

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            if (!s.equals("setwarp")) return false;
            if (strings.length == 1) {
                command.setUsage(ChatColor.translateAlternateColorCodes('&', serverPrefix + FileConfigs.fileConfigs.get("commands").getString("setwarp.usage")));
                return false;
            } else if (strings.length == 0){
                new WarpMenu(((Player) commandSender).getPlayer()).open();
            }
        } else {
            commandSender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("setwarp.not-player"), null));
        }
        return true;
    }
}
