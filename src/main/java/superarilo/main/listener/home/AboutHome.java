package superarilo.main.listener.home;

import com.alibaba.fastjson.JSONObject;
import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.text.PaperComponents;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import superarilo.main.Main;
import superarilo.main.entity.PlayerHome;
import superarilo.main.function.FileConfigs;
import superarilo.main.function.FunctionTool;
import superarilo.main.function.home.EditorHomeFunction;
import superarilo.main.function.MatchHomeId;
import superarilo.main.function.TeleportThread;
import superarilo.main.function.home.HomeManager;
import superarilo.main.function.home.Impl.HomeManagerImpl;
import superarilo.main.gui.home.HomeEditor;

public class AboutHome implements Listener {

    @EventHandler
    public void homeClickFunction(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        InventoryView homeInv = player.getOpenInventory();
        if (homeInv.title().equals(FunctionTool.setTextComponent(FileConfigs.fileConfigs.get("homelist").getString("menu-settings.name", "GUI")))){
            event.setCancelled(true);
            ItemStack currentItem = event.getCurrentItem();
            if (currentItem == null) return;
            int clickIndex = event.getSlot();
            if (clickIndex > event.getInventory().getSize()) return;
            Integer inNBTSlot = currentItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.mainPlugin, FileConfigs.fileConfigs.get("homelist").getString("home-nbt.name-space", "null")), PersistentDataType.INTEGER);
            if (inNBTSlot == null) return;
            ClickType clickType = event.getClick();
            PlayerHome playerHome = JSONObject.parseArray(Main.redisValue.get(player.getUniqueId() + "_home"), PlayerHome.class).get(inNBTSlot);
            if (clickType.equals(ClickType.LEFT)){
                homeInv.close();
                new TeleportThread(player, new Location(Main.mainPlugin.getServer().getWorld(playerHome.getWorld()),playerHome.getLocationX(),playerHome.getLocationY(),playerHome.getLocationZ()).setDirection(new Vector().setX(playerHome.getVectorX()).setY(playerHome.getVectorY()).setZ(playerHome.getVectorZ())), TeleportThread.Type.POINT).teleport();
            } else if (clickType.equals(ClickType.RIGHT)){
                homeInv.close();
                HomeManager homeManager = new HomeManagerImpl(player);
                homeManager.saveEditorTempHomeOnRedis(playerHome);
                new HomeEditor(player, playerHome).open();
            }
        }
    }

    @EventHandler
    public void homeEditorEvent(InventoryClickEvent event){
        Inventory editorInv = event.getClickedInventory();
        if (editorInv == null) return;
        Player player = (Player) event.getWhoClicked();
        if (player.getOpenInventory().title().equals(FunctionTool.setTextComponent(FileConfigs.fileConfigs.get("homeEditor").getString("menu-settings.name", "GUI")))){
            if (editorInv.getType() == InventoryType.CHEST){
                event.setCancelled(true);
                ItemStack currentItem = event.getCurrentItem();
                if (currentItem == null) return;
                String type = currentItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.mainPlugin, FileConfigs.fileConfigs.get("homeEditor").getString("home-nbt.name-space", "null")), PersistentDataType.STRING);
                if (type != null){
                    new EditorHomeFunction(player, EditorHomeFunction.FunctionType.valueOf(type.toUpperCase()), editorInv, currentItem, event.getCursor(), event.getSlot()).startEditorHome();
                }
            } else if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY || event.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void homeIconDrag(InventoryDragEvent event){
        if (event.getView().title().equals(FunctionTool.setTextComponent(FileConfigs.fileConfigs.get("homeEditor").getString("menu-settings.name", "GUI")))){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void getPlayerMessage(AsyncChatEvent event){
        Player player = event.getPlayer();
        if (Main.redisValue.sismember("editor_home_player_now", player.getUniqueId().toString())){
            event.setCancelled(true);
            Main.mainPlugin.getServer().getScheduler().runTask(Main.mainPlugin, () -> Main.mainPlugin.getServer().getPluginManager().callEvent(new EditorHomeName(player, event.message())));
        }
    }

    @EventHandler
    public void getEditorToHomeName(EditorHomeName event){
        String getMessageString =  PaperComponents.plainTextSerializer().serialize(event.getPlayerSendMessage());
        Player player = event.getPlayer();
        HomeManager homeManager = new HomeManagerImpl(player);
        if (FileConfigs.fileConfigs.get("home").getInt("max-home-name-length") < getMessageString.length()) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("editor-home.name-to-long")));
        } else {
            if (MatchHomeId.isChineseEnglishNumber(getMessageString)){
                if (!homeManager.modifyHomeName(getMessageString)){
                    event.setCancelled(true);
                } else {
                    new HomeEditor(player,homeManager.getEditorTempHomeOnRedis()).open();
                }
            } else {
                event.setCancelled(true);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("editor-home.wrongful-name")));
            }
        }
        homeManager.removeNowEditorHomePlayer();
        player.resetTitle();
    }
}
