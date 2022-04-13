package superarilo.main.function;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.ibatis.session.SqlSession;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import superarilo.main.Main;
import superarilo.main.entity.PlayerHome;
import superarilo.main.gui.home.ShowHomeList;
import superarilo.main.mapper.PlayerHomeFunction;

import java.text.DecimalFormat;

public class HomeFunction {

    private final Player player;
    private final FunctionType functionType;
    private final Inventory inventory;
    private final ItemStack changeItem;
    private final ItemStack cursorItem;
    private final int changeItemSlot;

    //配置文件
    private final FileConfiguration homeEditorCfg = FileConfigs.fileConfigs.get("homeEditor");
    private final FileConfiguration messageCfg = FileConfigs.fileConfigs.get("message");

    //修改图标
    public HomeFunction(Player player, FunctionType functionType, Inventory inventory, ItemStack changeItem, ItemStack cursorItem, int slot){
        this.player = player;
        this.functionType = functionType;
        this.inventory = inventory;
        this.changeItem = changeItem;
        this.cursorItem = cursorItem;
        this.changeItemSlot = slot;
    }

    public void startEditorHome(){
        switch (this.functionType){
            case CHANGEICON: {
                //判断是否是空气或者不存在
                if (cursorItem == null || cursorItem.getType() == Material.AIR) break;
                Material cursorMaterial = this.cursorItem.getType();
                this.changeItem.setType(cursorMaterial);
                this.inventory.setItem(this.changeItemSlot, this.changeItem);
                PlayerHome playerHome = getEditorHomeToRedis(player);
                if (playerHome == null) {
                    this.inventory.close();
                    this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + messageCfg.getString("editor-home.timeout")));
                    break;
                }
                playerHome.setMaterial(cursorMaterial.name());
                setEditorHomeToRedis(player, playerHome);
            }
                break;
            case CHANGENAME: {
                this.inventory.close();
                new CreatePlayerTitle(this.player, messageCfg.getString("editor-home.tips-home-title-name"), messageCfg.getString("editor-home.tips-home-subtitle-name"),10,80,20).sendToPlayer();
                Main.redisValue.sadd("editor_home_name_player_list", player.getUniqueId().toString());
            }
                break;
            case CHANGEPOSITION: {
                PlayerHome playerHome = getEditorHomeToRedis(player);
                if (playerHome == null) {
                    this.inventory.close();
                    this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + messageCfg.getString("editor-home.timeout")));
                    break;
                }
                DecimalFormat decimal = new DecimalFormat("#.00");
                Location locationNew = this.player.getLocation();
                Vector vectorNew = locationNew.getDirection();
                playerHome.setVectorX(Double.parseDouble(decimal.format(vectorNew.getX())));
                playerHome.setVectorY(Double.parseDouble(decimal.format(vectorNew.getY())));
                playerHome.setVectorZ(Double.parseDouble(decimal.format(vectorNew.getZ())));
                playerHome.setLocationX(Double.parseDouble(decimal.format(locationNew.getX())));
                playerHome.setLocationY(Double.parseDouble(decimal.format(locationNew.getY())));
                playerHome.setLocationZ(Double.parseDouble(decimal.format(locationNew.getZ())));
                setEditorHomeToRedis(player, playerHome);
                ItemMeta itemMeta = this.changeItem.getItemMeta();
                itemMeta.setLore(PlaceholderAPI.setPlaceholders(this.player, homeEditorCfg.getStringList("function.home-position.lore")));
                this.changeItem.setItemMeta(itemMeta);
                this.inventory.setItem(this.changeItemSlot, this.changeItem);
            }
                break;
            case SAVE: {
                saveToDataBase(this.player);
            }
                break;
            case SAVED: {
                saveToDataBase(this.player);
            }
                return;
            case DELETE: {

            }
                break;
            case BACK: {
                this.inventory.close();
                new ShowHomeList(this.player).open();
            }
                break;
        }
    }

    public static void setEditorHomeToRedis(Player player, PlayerHome playerHome) {
        Main.redisValue.setex(player.getUniqueId() + "_editor_home", 1800, JSONObject.toJSONString(playerHome));
    }
    public static PlayerHome getEditorHomeToRedis(Player player){
        String playerHomeJson = Main.redisValue.get(player.getUniqueId() + "_editor_home");
        return playerHomeJson != null ? JSONObject.parseObject(playerHomeJson, PlayerHome.class) : null;
    }

    private void saveToDataBase(Player player){
        setSavingItemStack();
        PlayerHome playerHome = getEditorHomeToRedis(player);
        if (playerHome == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + messageCfg.getString("editor-home.save-fail")));
            this.inventory.close();
            Main.redisValue.del(player.getUniqueId() + "_home");
            return;
        }
        Runnable runnable = () -> {
            SqlSession sqlSession = Main.SQL_SESSIONS.openSession(true);
            try {
                sqlSession.getMapper(PlayerHomeFunction.class).update(playerHome, new QueryWrapper<PlayerHome>().eq("home_id",playerHome.getHomeId()));
                setSavedItemStack();
            } catch (Exception exception) {
                sqlSession.rollback();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + messageCfg.getString("SQL.fail") + exception.getLocalizedMessage()));
            } finally {
                sqlSession.close();
                Main.redisValue.del(player.getUniqueId() + "_home");
            }
        };
        Main.mainPlugin.getServer().getScheduler().runTaskAsynchronously(Main.mainPlugin, runnable);
    }

    private void setSavingItemStack(){
        ItemStack itemStack = new ItemStack(Material.valueOf(homeEditorCfg.getString("saving.material", "DIRT").toUpperCase()));
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP,1,1), true);
        potionMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', homeEditorCfg.getString("saving.name", "null")));
        potionMeta.setLore(homeEditorCfg.getStringList("saving.lore"));
        //设置NBT标签
        potionMeta.getPersistentDataContainer().set(new NamespacedKey(Main.mainPlugin, homeEditorCfg.getString("home-nbt.name-space", "null")), PersistentDataType.STRING, homeEditorCfg.getString("saving.type", "null"));
        itemStack.setItemMeta(potionMeta);
        this.inventory.setItem(this.changeItemSlot, itemStack);
    }

    private void setSavedItemStack(){
        ItemStack itemStack = new ItemStack(Material.valueOf(homeEditorCfg.getString("saved.material", "DIRT").toUpperCase()));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', homeEditorCfg.getString("saved.name", "null")));
        itemMeta.setLore(homeEditorCfg.getStringList("saved.lore"));
        //设置NBT标签
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(Main.mainPlugin, homeEditorCfg.getString("home-nbt.name-space", "null")), PersistentDataType.STRING, homeEditorCfg.getString("saved.type", "null"));
        itemStack.setItemMeta(itemMeta);
        this.inventory.setItem(this.changeItemSlot, itemStack);
    }

    public enum FunctionType {
        CHANGEICON,
        CHANGENAME,
        CHANGEPOSITION,
        DELETE,
        SAVE,
        SAVING,
        SAVED,
        BACK
    }

}
