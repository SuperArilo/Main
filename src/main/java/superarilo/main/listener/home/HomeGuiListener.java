package superarilo.main.listener.home;

import com.alibaba.fastjson.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.scheduler.BukkitRunnable;
import superarilo.main.Main;
import superarilo.main.entity.PlayerHome;
import superarilo.main.function.FileConfigs;


@SuppressWarnings("ALL")
public class HomeGuiListener implements Listener {
    @EventHandler
    public void guiClickFunction(InventoryClickEvent event){
        InventoryView homeInv = event.getWhoClicked().getOpenInventory();
        if (homeInv.getTitle().equals(FileConfigs.fileConfigs.get("homelist").getString("menu-settings.name"))){
            event.setCancelled(true);
            int clickIndex = event.getRawSlot();
            if (clickIndex < 0 || clickIndex > event.getInventory().getSize()) return;
            if (clickIndex <= 8){
                Player player = (Player) event.getWhoClicked();
                PlayerHome playerHome = JSONObject.parseArray(Main.redisValue.get(player.getUniqueId() + "_home"),PlayerHome.class).get(clickIndex);
                if (playerHome == null) return;
                long delay = player.isOp() ? 1 : FileConfigs.fileConfigs.get("home").getLong("delay", 1) * 20;
                BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.teleport(new Location(Main.mainPlugin.getServer().getWorld(playerHome.getWorld()),playerHome.getLocationX(),playerHome.getLocationY(),playerHome.getLocationZ()));
                    }
                };
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("home.teleporting")));
                bukkitRunnable.runTaskLaterAsynchronously(Main.mainPlugin, delay);
            }
        }
    }
}
