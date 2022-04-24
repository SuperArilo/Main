package superarilo.main.listener.home;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class EditorHomeName extends PlayerEvent implements Cancellable {

    private final static HandlerList handlerList = new HandlerList();
    private boolean isCancelled = false;
    private final Component message;

    public EditorHomeName(@NotNull Player who, Component message) {
        super(who);
        this.message = message;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList(){
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;
    }

    public Component getPlayerSendMessage(){
        return this.message;
    }
}
