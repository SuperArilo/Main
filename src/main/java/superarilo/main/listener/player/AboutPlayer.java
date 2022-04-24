package superarilo.main.listener.player;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import superarilo.main.Main;
import superarilo.main.function.FileConfigs;
import superarilo.main.function.FunctionTool;
import superarilo.main.function.TeleportThread;

public class AboutPlayer implements Listener {
    @EventHandler
    public void ifPlayerDeath(PlayerDeathEvent event){
        Player player = event.getPlayer();
        TeleportThread.setPlayerBackLocation(player.getLocation(), player.getUniqueId() + "_back");
        player.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("back.death"), null));
    }
    @EventHandler
    public void tpaClickFunction(InventoryClickEvent event){
        InventoryView inventoryView = event.getWhoClicked().getOpenInventory();
        if (inventoryView.title().equals(FunctionTool.setTextComponent(FileConfigs.fileConfigs.get("tpalist").getString("menu-settings.name", "GUI")))){
            event.setCancelled(true);
            if (event.getRawSlot() < 0 || event.getRawSlot() > event.getInventory().getSize()) return;
            ItemStack clickItem = event.getCurrentItem();
            if (clickItem != null){
                if (clickItem.getType().equals(Material.PLAYER_HEAD)){
                    Player player = (Player) event.getWhoClicked();
                    if (event.isLeftClick()){
                        player.performCommand("tpa " + clickItem.getItemMeta().displayName());
                    } else if (event.isRightClick()){
                        player.performCommand("tpahere " + clickItem.getItemMeta().displayName());
                    }
                    inventoryView.close();
                } else if (clickItem.getType().equals(Material.ARROW)){
                    System.out.println(clickItem.getItemMeta().displayName());
                } else if (clickItem.getType().equals(Material.BARRIER)){
                    inventoryView.close();
                }
            }
        }
    }

    @EventHandler
    public void whenPlayerExit(PlayerQuitEvent event){
        Main.redisValue.srem("editor_home_player_now", event.getPlayer().getUniqueId().toString());
    }

}
