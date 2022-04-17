package superarilo.main.function.home;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.ibatis.session.SqlSession;
import org.bukkit.ChatColor;
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
import superarilo.main.Main;
import superarilo.main.entity.PlayerHome;
import superarilo.main.function.CreatePlayerTitle;
import superarilo.main.function.FileConfigs;
import superarilo.main.function.home.Impl.HomeManagerImpl;
import superarilo.main.gui.home.ShowHomeList;
import superarilo.main.mapper.PlayerHomeFunction;

@SuppressWarnings("deprecation")
public class EditorHomeFunction {

    private final Player player;
    private final FunctionType functionType;
    private final Inventory inventory;
    private final ItemStack changeItem;
    private final ItemStack cursorItem;
    private final int changeItemSlot;
    private final HomeManagerImpl homeManager;

    //配置文件
    private final FileConfiguration homeEditorCfg = FileConfigs.fileConfigs.get("homeEditor");
    private final FileConfiguration messageCfg = FileConfigs.fileConfigs.get("message");

    //修改图标
    public EditorHomeFunction(Player player, FunctionType functionType, Inventory inventory, ItemStack changeItem, ItemStack cursorItem, int slot){
        this.player = player;
        this.functionType = functionType;
        this.inventory = inventory;
        this.changeItem = changeItem;
        this.cursorItem = cursorItem;
        this.changeItemSlot = slot;
        this.homeManager = new HomeManagerImpl(player);
    }

    public void startEditorHome(){
        switch (this.functionType){
            case CHANGEICON: {
                //判断是否是空气或者不存在
                if (cursorItem == null || cursorItem.getType() == Material.AIR) break;
                Material cursorMaterial = this.cursorItem.getType();
                this.changeItem.setType(cursorMaterial);
                this.inventory.setItem(this.changeItemSlot, this.changeItem);
                if (!this.homeManager.modifyHomeIcon(cursorMaterial)) {
                    this.inventory.close();
                    return;
                }
            }
                break;
            case CHANGENAME: {
                this.inventory.close();
                new CreatePlayerTitle(this.player, messageCfg.getString("editor-home.tips-home-title-name"), messageCfg.getString("editor-home.tips-home-subtitle-name"),10,200,20).sendToPlayer();
                this.homeManager.setNowEditorHomePlayer();
            }
                break;
            case CHANGEPOSITION: {
                if (!this.homeManager.modifyLocation(this.player.getLocation())) {
                    this.inventory.close();
                    return;
                }
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
                PlayerHome playerHome = homeManager.getEditorTempHomeOnRedis();
                if (playerHome != null) {
                    this.homeManager.deleteHome(playerHome.getHomeId());
                    this.player.closeInventory();
                } else {
                    this.player.sendMessage(PlaceholderAPI.setPlaceholders(this.player, Main.mainPlugin.getConfig().getString("prefix") + messageCfg.getString("delete-home.no-have")));
                }
                homeManager.deleteHomeOnRedis();
            }
                break;
            case BACK: {
                this.inventory.close();
                new ShowHomeList(this.player).open();
            }
                break;
        }
    }

    private void saveToDataBase(Player player){
        setSavingItemStack();
        PlayerHome playerHome = homeManager.getEditorTempHomeOnRedis();
        if (playerHome == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + messageCfg.getString("editor-home.save-fail")));
            this.inventory.close();
            homeManager.deleteHomeOnRedis();
            return;
        }
        Main.mainPlugin.getServer().getScheduler().runTaskAsynchronously(Main.mainPlugin, () -> {
            SqlSession sqlSession = Main.SQL_SESSIONS.openSession(true);
            try {
                sqlSession.getMapper(PlayerHomeFunction.class).update(playerHome, new QueryWrapper<PlayerHome>().eq("id",playerHome.getId()));
                setSavedItemStack();
            } catch (Exception exception) {
                sqlSession.rollback();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + messageCfg.getString("SQL.fail") + exception.getCause().getMessage()));
            } finally {
                sqlSession.close();
                homeManager.deleteHomeOnRedis();
            }
        });
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
