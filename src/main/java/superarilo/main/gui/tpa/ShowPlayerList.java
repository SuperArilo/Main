package superarilo.main.gui.tpa;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import superarilo.main.Main;

import java.util.List;

public class ShowPlayerList {

    private Inventory inventory;
    private Player player;

    public ShowPlayerList(Player player) {
        this.player = player;
        List<Player> playerList = (List<Player>) Main.mainPlugin.getServer().getOnlinePlayers();
    }
    public void open(){
        this.player.openInventory(inventory);
    }
}
