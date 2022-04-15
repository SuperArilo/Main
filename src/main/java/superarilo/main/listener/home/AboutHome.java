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
import superarilo.main.function.home.EditorHomeFunction;
import superarilo.main.function.MatchHomeId;
import superarilo.main.function.TeleporThread;
import superarilo.main.gui.home.HomeEditor;

@SuppressWarnings("ALL")
public class AboutHome implements Listener {

    @EventHandler
    public void homeClickFunction(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        InventoryView homeInv = player.getOpenInventory();
        if (homeInv.getTitle().equals(FileConfigs.fileConfigs.get("homelist").getString("menu-settings.name"))){
            event.setCancelled(true);
            ItemStack currentItem = event.getCurrentItem();
            if (currentItem == null) return;
            int clickIndex = event.getSlot();
            if (clickIndex > event.getInventory().getSize()) return;
            Integer inNBTSlot = currentItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.mainPlugin, FileConfigs.fileConfigs.get("homelist").getString("home-nbt.name-space", "null")), PersistentDataType.INTEGER);
            if (inNBTSlot == null) return;
            ClickType clickType = event.getClick();
            PlayerHome playerHome = JSONObject.parseArray(Main.redisValue.get(player.getUniqueId().toString() + "_home"), PlayerHome.class).get(inNBTSlot);
            if (clickType.equals(ClickType.LEFT)){
                homeInv.close();
                new TeleporThread(player, new Location(Main.mainPlugin.getServer().getWorld(playerHome.getWorld()),playerHome.getLocationX(),playerHome.getLocationY(),playerHome.getLocationZ()).setDirection(new Vector().setX(playerHome.getVectorX()).setY(playerHome.getVectorY()).setZ(playerHome.getVectorZ())), TeleporThread.Type.POINT).teleport();
            } else if (clickType.equals(ClickType.RIGHT)){
                homeInv.close();
                EditorHomeFunction.setEditorHomeToRedis(player, playerHome);
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
                    Main.mainPlugin.getServer().getScheduler().runTask(Main.mainPlugin, () -> new EditorHomeFunction(player, EditorHomeFunction.FunctionType.valueOf(type.toUpperCase()), editorInv, currentItem, event.getCursor(), event.getSlot()).startEditorHome());
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
        Player player = event.getPlayer();
        if (Main.redisValue.sismember("editor_home_player_now", player.getUniqueId().toString())){
            event.setCancelled(true);
            Main.mainPlugin.getServer().getScheduler().runTask(Main.mainPlugin, () -> Main.mainPlugin.getServer().getPluginManager().callEvent(new EditorHomeName(player, event.getMessage())));
        }
    }

    @EventHandler
    public void getEditorToHomeName(EditorHomeName event){
        String getMessage = event.getPlayerSendMessage();
        Player player = event.getPlayer();
        if (FileConfigs.fileConfigs.get("home").getInt("max-home-name-length") < getMessage.length()) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("editor-home.name-to-long")));
        } else {
            if (MatchHomeId.isChineseEnglishNumber(getMessage)){
                PlayerHome playerHome = EditorHomeFunction.getEditorHomeToRedis(player);
                playerHome.setHomeName(getMessage);
                EditorHomeFunction.setEditorHomeToRedis(player, playerHome);
                new HomeEditor(player, playerHome).open();
            } else {
                event.setCancelled(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("editor-home.wrongful-name")));
            }
        }
        Main.redisValue.srem("editor_home_player_now", player.getUniqueId().toString());
        player.resetTitle();
    }
}
