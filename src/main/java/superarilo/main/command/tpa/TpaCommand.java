package superarilo.main.command.tpa;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import superarilo.main.Main;
import superarilo.main.function.FileConfigs;
import superarilo.main.function.FunctionTool;

public class TpaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            if (!s.equals("tpa")) return false;
            if (strings.length == 1) {
                Player bePlayer = Main.mainPlugin.getServer().getPlayer(strings[0]);
                String commandName = commandSender.getName();
                if (bePlayer != null && !bePlayer.getName().equals(commandName)) {
                    Main.mainPlugin.getServer().getScheduler().runTaskAsynchronously(Main.mainPlugin, () -> {
                        commandSender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("tpa.send-message"), bePlayer));
                        bePlayer.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("tpa.get-message"), (Player) commandSender));
                        bePlayer.sendMessage(Component.empty().append(FunctionTool.createClickAndHoverEventTextComponent("&a[同意]", ClickEvent.Action.RUN_COMMAND, "/tpaccept " + commandName, HoverEvent.Action.SHOW_TEXT, "豪爽同意")).append(Component.text(" 或者 ")).append(FunctionTool.createClickAndHoverEventTextComponent("&c[拒绝]", ClickEvent.Action.RUN_COMMAND, "/tparefuse " + commandName, HoverEvent.Action.SHOW_TEXT, "残忍拒绝")));
                        Main.redisValue.setex(((Player) commandSender).getUniqueId() + "_tpa_" + bePlayer.getUniqueId(), 15, commandName);
                    });
                } else {
                    commandSender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("tpa.unable-player"), null));
                }
                return true;
            } else {
                command.setUsage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("commands").getString("tpa.usage")));
                return false;
            }
        } else {
            commandSender.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("tpa.not-player"), null));
            return false;
        }
    }
}
