package superarilo.main.listener.player;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import superarilo.main.Main;
import superarilo.main.function.FileConfigs;
import superarilo.main.function.GetPlayerInfo;
import superarilo.main.function.TeleporThread;

@SuppressWarnings("ALL")
public class AboutPlayer implements Listener {
    @EventHandler
    public void ifPlayerDeath(PlayerDeathEvent event){
        Player player = event.getPlayer();
        TeleporThread.setPlayerBackLocation(player.getLocation(), player.getUniqueId() + "_back");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("back.death")));
    }
    @EventHandler
    public void tpaClickFunction(InventoryClickEvent event){
        InventoryView inventoryView = event.getWhoClicked().getOpenInventory();
        if (inventoryView.getTitle().equals(FileConfigs.fileConfigs.get("tpalist").getString("menu-settings.name"))){
            event.setCancelled(true);
            if (event.getRawSlot() < 0 || event.getRawSlot() > event.getInventory().getSize()) return;
            ItemStack clickItem = event.getCurrentItem();
            if (clickItem != null){
                if (clickItem.getType().equals(Material.PLAYER_HEAD)){
                    Player player = (Player) event.getWhoClicked();
                    if (event.isLeftClick()){
                        player.performCommand("tpa " + clickItem.getItemMeta().getDisplayName());
                    } else if (event.isRightClick()){
                        player.performCommand("tpahere " + clickItem.getItemMeta().getDisplayName());
                    }
                    inventoryView.close();
                } else if (clickItem.getType().equals(Material.ARROW)){
                    System.out.println(clickItem.getItemMeta().getDisplayName());
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
