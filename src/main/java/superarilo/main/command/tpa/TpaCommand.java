package superarilo.main.command.tpa;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import superarilo.main.Main;
import superarilo.main.function.FileConfigs;

import java.util.List;

public class TpaCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        switch (s){
            case "tpa":
                if (commandSender instanceof Player){
                    if (strings.length == 1){
                        Player bePlayer = Main.mainPlugin.getServer().getPlayer(strings[0]);
                        if (bePlayer != null){
                            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',FileConfigs.fileConfigs.get("message").getString("tpa.send-message")));
                            BaseComponent baseComponent = new TextComponent("1111233344555");
                            baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/tpaccept"));
                            bePlayer.sendMessage(baseComponent);
                        } else {
                            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',FileConfigs.fileConfigs.get("message").getString("tpa.unable-player")));
                        }
                    } else {
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', FileConfigs.fileConfigs.get("message").getString("tpa.fail")));
                    }
                } else {
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',FileConfigs.fileConfigs.get("message").getString("tpa.not-player")));
                }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
