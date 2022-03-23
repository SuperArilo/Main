package superarilo.main.command.tpa;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import superarilo.main.Main;
import superarilo.main.function.FileConfigs;

public class TpAcceptCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player){
            if (s.equals("tpaccept")){
                String keyName = commandSender.getName() + "_tpa";
                if(Main.redisValue.exists(keyName)){
                    Player playerSender = Main.mainPlugin.getServer().getPlayer(Main.redisValue.get(keyName));
                    if (playerSender != null && !playerSender.getName().equals(commandSender.getName())){
                        String publicMessage = ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("tpaccept.success"));
                        commandSender.sendMessage(publicMessage);
                        playerSender.sendMessage(publicMessage);
                        playerSender.teleport(((Player) commandSender).getLocation());
                    } else {
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("tpaccept.unable-player")));
                    }
                    Main.redisValue.del(keyName);
                } else {
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("tpaccept.no-have")));
                }
            } else {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("commands").getString("tpaccept.usage")));
            }
        } else {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("tpaccept.not-player")));
        }
        return true;
    }
}
