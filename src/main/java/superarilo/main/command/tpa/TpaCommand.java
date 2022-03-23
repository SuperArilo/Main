package superarilo.main.command.tpa;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.BaseComponent;
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
import java.util.Objects;

public class TpaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            if ("tpa".equals(s)) {
                if (strings.length == 1) {
                    Player bePlayer = Main.mainPlugin.getServer().getPlayer(strings[0]);
                    if (bePlayer != null) {
                        commandSender.sendMessage(Objects.requireNonNull(PlaceholderAPI.setPlaceholders(bePlayer, FileConfigs.fileConfigs.get("message").getString("tpa.send-message"))));
                        TextComponent accept = new TextComponent(FileConfigs.fileConfigs.get("message").getString("tpa.accept"));
                        accept.setColor(net.md_5.bungee.api.ChatColor.GREEN);
                        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text("同意请求")));
                        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/tpaccept"));
                        accept.setBold(true);
                        BaseComponent refuse = new TextComponent(FileConfigs.fileConfigs.get("message").getString("tpa.refuse"));
                        refuse.setColor(net.md_5.bungee.api.ChatColor.RED);
                        refuse.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text("残忍拒绝")));
                        refuse.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/tparefuse"));
                        refuse.setBold(true);
                        bePlayer.sendMessage(Objects.requireNonNull(PlaceholderAPI.setPlaceholders((Player) commandSender, FileConfigs.fileConfigs.get("message").getString("tpa.get-message"))));
                        bePlayer.sendMessage(accept,new TextComponent(" 或者 "),refuse);
                        String keyName = bePlayer.getName() + "_tpa";
                        if(Main.redisValue.exists(keyName)){
                            Main.redisValue.del(keyName);
                        }
                        Main.redisValue.set(bePlayer.getName() + "_tpa",commandSender.getName());
                        Main.redisValue.expire(keyName,10);
                    } else {
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(FileConfigs.fileConfigs.get("message").getString("tpa.unable-player"))));
                    }
                } else {
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(FileConfigs.fileConfigs.get("message").getString("tpa.fail"))));
                }
            } else {
                commandSender.sendMessage(Objects.requireNonNull(FileConfigs.fileConfigs.get("message").getString("tpa.usage")));
            }
        } else {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(FileConfigs.fileConfigs.get("message").getString("tpa.not-player"))));
        }
        return true;
    }
}
