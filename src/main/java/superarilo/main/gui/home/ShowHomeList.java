package superarilo.main.gui.home;

import com.alibaba.fastjson.JSONObject;
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
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import superarilo.main.Main;
import superarilo.main.entity.PlayerHome;
import superarilo.main.function.FileConfigs;
import superarilo.main.mapper.PlayerHomeFunction;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("deprecation")
public class ShowHomeList {

    private final Player player;
    private final Inventory inventory;
    private List<PlayerHome> playerHomeList = null;
    private final SqlSession sqlSession = Main.SQL_SESSIONS.openSession(true);
    private final FileConfiguration listCfg = FileConfigs.fileConfigs.get("homelist");

    public ShowHomeList(Player player) {
        this.player = player;
        this.inventory = Main.mainPlugin.getServer().createInventory(this.player, listCfg.getInt("menu-settings.rows") * 9, listCfg.getString("menu-settings.name", "GUI"));
    }
    public void open(){
        this.player.openInventory(inventory);
        renderMasks();
        String keyName = this.player.getUniqueId() + "_home";
        if (Main.redisValue.exists(keyName)){
            Main.mainPlugin.getServer().getScheduler().runTaskAsynchronously(Main.mainPlugin, () -> {
                setPlayerHomeList(JSONObject.parseArray(Main.redisValue.get(keyName), PlayerHome.class));
                renderHomeList();
                renderOwnerHead();
            });
        } else {
            Main.mainPlugin.getServer().getScheduler().runTaskAsynchronously(Main.mainPlugin, () -> {
                renderLoading();
                getHomeByDataBase(keyName);
            });
        }
    }

    private void renderMasks() {
        for (int index : listCfg.getIntegerList("mask.slot")){
            this.inventory.setItem(index, new ItemStack(Material.valueOf(listCfg.getString("mask.material", "Dirt").toUpperCase())));
        }
    }

    private void renderLoading(){
        ItemStack itemStack = new ItemStack(Material.valueOf(listCfg.getString("loading.material", "DIRT").toUpperCase()));
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP,1,1), true);
        potionMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', listCfg.getString("loading.name", "null")));
        potionMeta.setLore(listCfg.getStringList("loading.lore"));
        itemStack.setItemMeta(potionMeta);
        this.inventory.setItem(listCfg.getInt("loading.slot"), itemStack);
    }

    private void renderOwnerHead() {
        ItemStack itemStack = new ItemStack(Material.valueOf(listCfg.getString("player-head.material", "DIRT").toUpperCase()));
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setDisplayName(PlaceholderAPI.setPlaceholders(this.player, listCfg.getString("player-head.name")));
        skullMeta.setOwner(this.player.getName());
        itemStack.setItemMeta(skullMeta);
        this.inventory.setItem(listCfg.getInt("player-head.slot"), itemStack);
    }

    private void renderHomeList(){
        if (this.playerHomeList == null) return;
        List<Integer> indexList = listCfg.getIntegerList("player-home.slot");
        for (int index = 0;index < playerHomeList.size();index++){
            PlayerHome playerHome = playerHomeList.get(index);
            ItemStack itemStack = new ItemStack(Material.valueOf(playerHome.getMaterial().toUpperCase()));
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.GREEN + "名称: " + ChatColor.AQUA + playerHome.getHomeName());
            itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "x: " + ChatColor.WHITE + playerHome.getLocationX() + ChatColor.GRAY + " y: " + ChatColor.WHITE + playerHome.getLocationY() + ChatColor.GRAY + " z: " + ChatColor.WHITE + playerHome.getLocationZ(), ChatColor.GRAY + "世界: " + ChatColor.WHITE + playerHome.getWorldAlias(), ChatColor.GRAY + "左击传送", ChatColor.GRAY + "右击编辑"));
            //nbt 标签
            itemMeta.getPersistentDataContainer().set(new NamespacedKey(Main.mainPlugin, listCfg.getString("home-nbt.name-space", "null")), PersistentDataType.INTEGER, index);
            itemStack.setItemMeta(itemMeta);
            this.inventory.setItem(indexList.get(index), itemStack);
        }
    }
    private void getHomeByDataBase(String keyName){
        try {
            setPlayerHomeList(sqlSession.getMapper(PlayerHomeFunction.class).getPlayerHome(this.player.getUniqueId().toString()));
            Main.redisValue.setex(keyName, 43200,JSONObject.toJSONString(getPlayerHomeList()));
        } catch (Exception exception) {
            sqlSession.rollback();
            exception.printStackTrace();
            this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("SQL.fail") + exception.getCause().getMessage()));
            Main.mainPlugin.getServer().getScheduler().runTask(Main.mainPlugin, this.inventory::close);
            return;
        } finally {
            sqlSession.close();
        }
        renderOwnerHead();
        renderHomeList();
    }

    public List<PlayerHome> getPlayerHomeList() {
        return playerHomeList;
    }

    public void setPlayerHomeList(List<PlayerHome> playerHomeList) {
        this.playerHomeList = playerHomeList;
    }
}
