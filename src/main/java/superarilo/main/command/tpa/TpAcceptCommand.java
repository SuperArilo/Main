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
                if (strings.length != 0){
                    Player comGetPlayer = Main.mainPlugin.getServer().getPlayer(strings[0]);
                    if (comGetPlayer != null){
                        String keyNameTpa = comGetPlayer.getUniqueId() + "_tpa_" + ((Player) commandSender).getUniqueId();
                        String keyNameTpaHere = ((Player) commandSender).getUniqueId() + "_tpahere_" + comGetPlayer.getUniqueId();
                        if (Main.redisValue.exists(keyNameTpa)){
                            String publicMessage = ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("tpaccept.success"));
                            commandSender.sendMessage(publicMessage);
                            comGetPlayer.sendMessage(publicMessage);
                            comGetPlayer.teleport(((Player) commandSender).getLocation());
                            Main.redisValue.del(keyNameTpa);
                        } else if (Main.redisValue.exists(keyNameTpaHere)){
                            String publicMessage = ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("tpahere.success"));
                            commandSender.sendMessage(publicMessage);
                            comGetPlayer.sendMessage(publicMessage);
                            ((Player) commandSender).teleport(comGetPlayer.getLocation());
                            Main.redisValue.del(keyNameTpaHere);
                        }else {
                            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("tpaccept.no-have")));
                        }
                    } else {
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("tpaccept.unable-player")));
                    }
                } else {
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("tpaccept.fail")));
                }
            }
        } else {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("tpaccept.not-player")));
        }
        return true;
    }
}
