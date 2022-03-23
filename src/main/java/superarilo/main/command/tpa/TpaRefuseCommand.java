package superarilo.main.command.tpa;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import superarilo.main.Main;
import superarilo.main.function.FileConfigs;

public class TpaRefuseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player){
            if (s.equals("tparefuse")){
                String keyName = commandSender.getName() + "_tpa";
                if (Main.redisValue.exists(keyName)){
                    Player playerSender = Main.mainPlugin.getServer().getPlayer(Main.redisValue.get(keyName));
                    if (playerSender != null && !playerSender.getName().equals(commandSender.getName())){
                        commandSender.sendMessage(PlaceholderAPI.setPlaceholders(playerSender, Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("tparefuse.success")));
                        playerSender.sendMessage(PlaceholderAPI.setPlaceholders((Player) commandSender, Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("tparefuse.sender-refuse")));
                    } else {
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("tparefuse.unable-player")));
                    }
                    Main.redisValue.del(keyName);
                } else {
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("tparefuse.no-have")));
                }
            } else {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("commands").getString("tparefuse.usage")));
            }
        } else {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("tparefuse.not-player")));
        }
        return true;
    }
}
