package superarilo.main.listener.home;

import com.alibaba.fastjson.JSONObject;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import superarilo.main.Main;
import superarilo.main.entity.PlayerHome;
import superarilo.main.function.FileConfigs;
import superarilo.main.function.HomeFunction;
import superarilo.main.function.TeleporThread;
import superarilo.main.gui.home.HomeEditor;

@SuppressWarnings("ALL")
public class AboutHome implements Listener {

    @EventHandler
    public void homeClickFunction(InventoryClickEvent event){
        InventoryView homeInv = event.getWhoClicked().getOpenInventory();
        if (homeInv.getTitle().equals(FileConfigs.fileConfigs.get("homelist").getString("menu-settings.name"))){
            event.setCancelled(true);
            Inventory checkInv = event.getClickedInventory();
            if (checkInv == null || checkInv.getType() == InventoryType.PLAYER) return;
            Player player = (Player) event.getWhoClicked();
            int clickIndex = event.getRawSlot();
            if (event.getCurrentItem() == null) return;
            if (clickIndex > event.getInventory().getSize()) return;
            PlayerHome playerHome = JSONObject.parseArray(Main.redisValue.get(player.getUniqueId() + "_home"),PlayerHome.class).get(clickIndex);
            if (playerHome == null) return;
            if (event.getClick().equals(ClickType.LEFT)){
                if (clickIndex <= 8){
                    if (homeInv.getItem(clickIndex) == null) return;
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("home.teleporting")));
                    homeInv.close();
                    new TeleporThread(player, new Location(Main.mainPlugin.getServer().getWorld(playerHome.getWorld()),playerHome.getLocationX(),playerHome.getLocationY(),playerHome.getLocationZ()).setDirection(new Vector().setX(playerHome.getVectorX()).setY(playerHome.getVectorY()).setZ(playerHome.getVectorZ())), TeleporThread.Type.POINT).teleport();
                }
            } else if (event.getClick().equals(ClickType.RIGHT)){
                homeInv.close();
                HomeFunction.setEditorHomeToRedis(player, playerHome);
                new HomeEditor(player, playerHome).open();
            }
        }
    }

    @EventHandler
    public void homeEditorEvent(InventoryClickEvent event){
        Inventory editorInv = event.getClickedInventory();
        if (editorInv == null) return;
        Player player = (Player) event.getWhoClicked();
        if (player.getOpenInventory().getTitle().equals(FileConfigs.fileConfigs.get("homeEditor").getString("menu-settings.name"))){
            if (editorInv.getType() == InventoryType.CHEST){
                event.setCancelled(true);
                ItemStack currentItem = event.getCurrentItem();
                if (currentItem == null) return;
                String type = currentItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.mainPlugin, FileConfigs.fileConfigs.get("homeEditor").getString("home-nbt.name-space", "null")), PersistentDataType.STRING);
                if (type != null){
                    Main.mainPlugin.getServer().getScheduler().runTask(Main.mainPlugin, () -> new HomeFunction(player, HomeFunction.FunctionType.valueOf(type.toUpperCase()), editorInv, currentItem, event.getCursor(), event.getSlot()).startEditorHome());
                    return;
                }
            } else if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY || event.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void homeIconDrag(InventoryDragEvent event){
        if (event.getView().getTitle().equals(FileConfigs.fileConfigs.get("homeEditor").getString("menu-settings.name"))){
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void getPlayerMessage(AsyncPlayerChatEvent event){

    }
}
