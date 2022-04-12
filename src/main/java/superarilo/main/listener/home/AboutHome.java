package superarilo.main.listener.home;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.session.SqlSession;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import superarilo.main.Main;
import superarilo.main.entity.PlayerHome;
import superarilo.main.function.FileConfigs;
import superarilo.main.function.TeleporThread;
import superarilo.main.gui.home.HomeEditor;
import superarilo.main.mapper.PlayerHomeFunction;

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
                setEditorHomeToRedis(player, playerHome);
                new HomeEditor(player, playerHome).open();
            }
        }
    }

    @EventHandler
    public void homeEditorEvent(InventoryClickEvent event){
        Inventory editorInv = event.getClickedInventory();
        if (editorInv == null) return;
        Player player = (Player) event.getWhoClicked();
        InventoryView editorView = player.getOpenInventory();
        if (editorView.getTitle().equals(FileConfigs.fileConfigs.get("homeEditor").getString("menu-settings.name"))){
            if (editorInv.getType() == InventoryType.CHEST){
                event.setCancelled(true);
                ItemStack currentItem = editorInv.getItem(event.getSlot());
                //设置图标
                if (currentItem != null && event.getSlot() == 13){
                    ItemStack cursorItem = event.getCursor();
                    if (cursorItem == null || cursorItem.getType() == Material.AIR) return;
                    SetIconEvent setIconEvent = new SetIconEvent(player, editorView, currentItem, cursorItem,event.getSlot());
                    Main.mainPlugin.getServer().getPluginManager().callEvent(setIconEvent);
                    Material changeMaterial = cursorItem.getType();
                    currentItem.setType(changeMaterial);
                    event.setCurrentItem(currentItem);
                    PlayerHome playerHome = getEditorHomeToRedis(player);
                    playerHome.setMaterial(changeMaterial.name());
                    setEditorHomeToRedis(player, playerHome);
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
    public void setHomeIconCou(SetIconEvent event) {
        System.out.println(event.getSlot());
    }

    @EventHandler
    public void closeInv(InventoryCloseEvent event){
        if (event.getView().getTitle().equals(FileConfigs.fileConfigs.get("homeEditor").getString("menu-settings.name"))){
            saveToDataBase((Player) event.getPlayer());
        }
    }

    public void saveToDataBase(Player player){
        Main.mainPlugin.getServer().getScheduler().runTaskAsynchronously(Main.mainPlugin, new Runnable() {
            @Override
            public void run() {
                SqlSession sqlSession = Main.SQL_SESSIONS.openSession(true);
                PlayerHome playerHome = getEditorHomeToRedis(player);
                try {
                    sqlSession.getMapper(PlayerHomeFunction.class).update(playerHome, new QueryWrapper<PlayerHome>().eq("home_id",playerHome.getHomeId()));
                    Main.redisValue.del(player.getUniqueId() + "_home");
                } catch (Exception exception) {
                    sqlSession.rollback();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("SQL.fail") + exception.getLocalizedMessage()));
                } finally {
                    sqlSession.close();
                    Main.redisValue.del(player.getUniqueId() + "_editor_home");
                }
            }
        });
    }

    public void setEditorHomeToRedis(Player player, PlayerHome playerHome) {
        Main.redisValue.setex(player.getUniqueId() + "_editor_home", 43200, JSONObject.toJSONString(playerHome));
    }
    public PlayerHome getEditorHomeToRedis(Player player){
        return JSONObject.parseObject(Main.redisValue.get(player.getUniqueId() + "_editor_home"), PlayerHome.class);
    }
}
