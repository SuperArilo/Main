package superarilo.main.function;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

@SuppressWarnings("deprecation")
public class SendFunctionMessage {
    private final ChatColor chatColor;
    private final String message;
    private final ClickEvent.Action clickAction;
    private final String clickCommand;
    private final HoverEvent.Action hoverAction;
    private final Text hoverText;
    private final boolean bold;
    public SendFunctionMessage(ChatColor chatColor, String message, ClickEvent.Action clickAction, String clickCommand , HoverEvent.Action hoverAction, Text hoverText, boolean bold) {
        this.chatColor = chatColor;
        this.message = message;
        this.clickAction = clickAction;
        this.clickCommand = clickCommand;
        this.hoverAction = hoverAction;
        this.hoverText = hoverText;
        this.bold = bold;
    }
    public BaseComponent getFunctionText(){
        BaseComponent baseComponent = new TextComponent(message);
        baseComponent.setBold(bold);
        baseComponent.setClickEvent(new ClickEvent(clickAction,clickCommand));
        baseComponent.setHoverEvent(new HoverEvent(hoverAction, hoverText));
        baseComponent.setColor(chatColor);
        return baseComponent;
    }
}
