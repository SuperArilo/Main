package superarilo.main.command.tpa;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import superarilo.main.Main;
import superarilo.main.function.FileConfigs;

import java.util.Objects;

public class TpAcceptCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player){
            if (s.equals("tpaccept")){
                String keyName = commandSender.getName() + "_tpa";
                String playerSenderName = Main.redisValue.get(keyName);
                if(playerSenderName != null){
                    Player playerSender = Main.mainPlugin.getServer().getPlayer(playerSenderName);
                    if (playerSender != null){
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(FileConfigs.fileConfigs.get("message").getString("tpaccept.success"))));
                        playerSender.teleport(((Player) commandSender).getLocation());
                        Main.redisValue.del(keyName);
                    } else {
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(FileConfigs.fileConfigs.get("message").getString("tpaccept.unable-player"))));
                    }
                } else {
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(FileConfigs.fileConfigs.get("message").getString("tpaccept.no-have"))));
                }
            } else {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(FileConfigs.fileConfigs.get("message").getString("tpaccept.usage"))));
            }
        } else {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(FileConfigs.fileConfigs.get("message").getString("tpaccept.not-player"))));
        }
        return true;
    }
}
