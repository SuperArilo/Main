package superarilo.main.function;

import io.papermc.paper.text.PaperComponents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import superarilo.main.Main;
import net.kyori.adventure.text.event.ClickEvent;
import java.util.ArrayList;
import java.util.List;

public class FunctionTool {

    public static TextComponent setTextComponent(@NotNull String content){
        return PaperComponents.plainTextSerializer().deserialize(ChatColor.translateAlternateColorCodes('&', content));
    }

    public static List<Component> setListTextComponent(List<String> contents){
        List<Component> components = new ArrayList<>();
        for (String s : contents){
            if (s != null) {
                components.add(PaperComponents.plainTextSerializer().deserialize(ChatColor.translateAlternateColorCodes('&', s)));
            }
        }
        return components;
    }

    public static TextComponent createServerSendMessage(String content){
        return PaperComponents.plainTextSerializer().deserialize(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + content));
    }

    public static TextComponent createClickEventTextComponent(String content, ClickEvent.Action action, String actionText){
        return PaperComponents.plainTextSerializer().deserialize(ChatColor.translateAlternateColorCodes('&', content)).clickEvent(ClickEvent.clickEvent(action, actionText));
    }

    public static TextComponent createHoverEventTextComponent(String content, HoverEvent.Action<String> action, String actionText){
        return PaperComponents.plainTextSerializer().deserialize(ChatColor.translateAlternateColorCodes('&', content)).hoverEvent(HoverEvent.hoverEvent(action, actionText));
    }

    public static TextComponent createClickAndHoverEventTextComponent(String content, ClickEvent.Action clickAction, String clickActionText, HoverEvent.Action<Component> hoverAction, String hoverActionText){
        return PaperComponents.plainTextSerializer().deserialize(ChatColor.translateAlternateColorCodes('&', content)).clickEvent(ClickEvent.clickEvent(clickAction, clickActionText)).hoverEvent(HoverEvent.hoverEvent(hoverAction, setTextComponent(hoverActionText)));
    }
}
