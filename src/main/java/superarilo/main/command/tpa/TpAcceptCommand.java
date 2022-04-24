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
import superarilo.main.function.TeleportThread;

public class TpAcceptCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player){
            if (!s.equals("tpaccept")) return false;
            if (strings.length != 0){
                Player comGetPlayer = Main.mainPlugin.getServer().getPlayer(strings[0]);
                if (comGetPlayer != null){
                    String keyNameTpa = comGetPlayer.getUniqueId() + "_tpa_" + ((Player) commandSender).getUniqueId();
                    String keyNameTpaHere = ((Player) commandSender).getUniqueId() + "_tpahere_" + comGetPlayer.getUniqueId();
                    if (Main.redisValue.exists(keyNameTpa)){
                        new TeleportThread(comGetPlayer, (Player) commandSender, TeleportThread.Type.PLAYER).teleport();
                        Main.redisValue.del(keyNameTpa);
                    } else if (Main.redisValue.exists(keyNameTpaHere)){
                        new TeleportThread((Player) commandSender, comGetPlayer, TeleportThread.Type.PLAYER).teleport();
                        Main.redisValue.del(keyNameTpaHere);
                    }else {
                        commandSender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("tpaccept.no-have"), null));
                    }
                } else {
                    commandSender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("tpaccept.unable-player"), null));
                }
                return true;
            } else {
                command.setUsage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("commands").getString("tpaccept.usage","使用方法")));
                return false;
            }
        } else {
            commandSender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("tpaccept.not-player"), null));
            return true;
        }
    }
}
