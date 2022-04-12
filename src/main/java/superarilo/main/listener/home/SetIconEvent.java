package superarilo.main.listener.home;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SetIconEvent extends Event implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();
    private boolean isCancelled = false;
    private final Player player;
    private final InventoryView inventoryView;
    private final ItemStack beforeItem;
    private final ItemStack afterItem;
    private final int slot;

    public SetIconEvent (Player player, InventoryView view, ItemStack beforeItem, ItemStack afterItem, int slot) {
        this.player = player;
        this.inventoryView = view;
        this.beforeItem = beforeItem;
        this.afterItem = afterItem;
        this.slot = slot;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public Player getPlayer() {
        return player;
    }

    public InventoryView getInventoryView() {
        return inventoryView;
    }

    public ItemStack getBeforeItem() {
        return beforeItem;
    }

    public ItemStack getAfterItem() {
        return afterItem;
    }

    public int getSlot() {
        return slot;
    }
}
