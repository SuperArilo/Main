package superarilo.main.function;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.title.Title;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import superarilo.main.Main;
import net.kyori.adventure.text.event.ClickEvent;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class FunctionTool {

    public static TextComponent setTextComponent(@NotNull String content){
        return  Component.text(ChatColor.translateAlternateColorCodes('&', content));
    }

    public static List<Component> setListTextComponent(List<String> contents){
        List<Component> components = new ArrayList<>();
        for (String s : contents){
            if (s != null) {
                components.add(setTextComponent(s));
            }
        }
        return components;
    }

    public static TextComponent createServerSendMessage(String content, Player player){
        return setTextComponent(PlaceholderAPI.setPlaceholders(player, Main.mainPlugin.getConfig().getString("prefix") + content));
    }

    public static TextComponent createClickEventTextComponent(String content, ClickEvent.Action action, String actionText){
        return setTextComponent(ChatColor.translateAlternateColorCodes('&', content)).clickEvent(ClickEvent.clickEvent(action, actionText));
    }

    public static TextComponent createHoverEventTextComponent(String content, HoverEvent.Action<String> action, String actionText){
        return setTextComponent(ChatColor.translateAlternateColorCodes('&', content)).hoverEvent(HoverEvent.hoverEvent(action, actionText));
    }

    public static TextComponent createClickAndHoverEventTextComponent(String content, ClickEvent.Action clickAction, String clickActionText, HoverEvent.Action<Component> hoverAction, String hoverActionText){
        return setTextComponent(ChatColor.translateAlternateColorCodes('&', content)).clickEvent(ClickEvent.clickEvent(clickAction, clickActionText)).hoverEvent(HoverEvent.hoverEvent(hoverAction, setTextComponent(hoverActionText)));
    }

    public static Title createPlayerTitle(@NotNull String title,@NotNull String subTitle, long fadeIn, long stay, long fadeOut){
        return Title.title(setTextComponent(title).asComponent(), setTextComponent(subTitle).asComponent(), Title.Times.times(Duration.ofSeconds(fadeIn), Duration.ofSeconds(stay), Duration.ofSeconds(fadeOut)));
    }
}
