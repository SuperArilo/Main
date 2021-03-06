package superarilo.main.command.tpa;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import superarilo.main.Main;
import superarilo.main.function.FileConfigs;
import superarilo.main.function.FunctionTool;

public class TpaRefuseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player){
            if (!s.equals("tparefuse")) return false;
            if (strings.length != 0){
                Player comGetPlayer = Main.mainPlugin.getServer().getPlayer(strings[0]);
                if (comGetPlayer != null){
                    String keyNameTpa = comGetPlayer.getUniqueId() + "_tpa_" + ((Player) commandSender).getUniqueId();
                    String keyNameTpaHere = ((Player) commandSender).getUniqueId() + "_tpahere_" + comGetPlayer.getUniqueId();
                    if (Main.redisValue.exists(keyNameTpa)){
                        TpaRefuseSendMessage(commandSender, comGetPlayer, keyNameTpa);
                    } else if (Main.redisValue.exists(keyNameTpaHere)){
                        TpaRefuseSendMessage(commandSender, comGetPlayer, keyNameTpaHere);
                    }else {
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("tparefuse.no-have")));
                    }
                } else {
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("tparefuse.unable-player")));
                }
                return true;
            } else {
                command.setUsage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("commands").getString("tparefuse.usage")));
                return false;
            }
        } else {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("tparefuse.not-player")));
            return true;
        }
    }

    private void TpaRefuseSendMessage(@NotNull CommandSender commandSender, Player comGetPlayer, String keyNameTpa) {
        commandSender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("tparefuse.success"), comGetPlayer));
        comGetPlayer.sendMessage(FunctionTool.createServerSendMessage( FileConfigs.fileConfigs.get("message").getString("tparefuse.sender-refuse"), (Player) commandSender));
        Main.redisValue.del(keyNameTpa);
    }
}
