package superarilo.main.command.tpa;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import superarilo.main.Main;
import superarilo.main.function.FileConfigs;
import superarilo.main.function.SendFunctionMessage;

@SuppressWarnings({"deprecation", "DuplicatedCode"})
public class TpaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            if (!s.equals("tpa")) return false;
            if (strings.length == 1) {
                Player bePlayer = Main.mainPlugin.getServer().getPlayer(strings[0]);
                if (bePlayer != null && !bePlayer.getName().equals(commandSender.getName())) {
                    commandSender.sendMessage(PlaceholderAPI.setPlaceholders(bePlayer, Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("tpa.send-message")));
                    bePlayer.sendMessage(PlaceholderAPI.setPlaceholders((Player) commandSender, Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("tpa.get-message")));
                    bePlayer.sendMessage(new SendFunctionMessage(net.md_5.bungee.api.ChatColor.GREEN,"[同意]", ClickEvent.Action.RUN_COMMAND,"/tpaccept " + commandSender.getName(), HoverEvent.Action.SHOW_TEXT,new Text("豪爽同意"),true).getFunctionText(),new TextComponent(" 或者 "),new SendFunctionMessage(net.md_5.bungee.api.ChatColor.RED,"[拒绝]", ClickEvent.Action.RUN_COMMAND,"/tparefuse " + commandSender.getName(), HoverEvent.Action.SHOW_TEXT,new Text("残忍拒绝"),true).getFunctionText());
                    Main.redisValue.setex(((Player) commandSender).getUniqueId() + "_tpa_" + bePlayer.getUniqueId(), 15, commandSender.getName());
                } else {
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("tpa.unable-player")));
                }
                return true;
            } else {
                command.setUsage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("commands").getString("tpa.usage")));
                return false;
            }
        } else {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("tpa.not-player")));
            return false;
        }
    }
}
