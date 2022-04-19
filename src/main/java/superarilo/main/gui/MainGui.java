package superarilo.main.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MainGui {

    protected final Player player;
    protected Inventory inventory;

    public MainGui(Player player) {
        this.player = player;
    }

    public void open(){
        this.player.openInventory(inventory);
    }
}
