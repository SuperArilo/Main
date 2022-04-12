package superarilo.main.gui.home;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.SqlSession;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import superarilo.main.Main;
import superarilo.main.entity.PlayerHome;
import superarilo.main.function.FileConfigs;
import superarilo.main.mapper.PlayerFunction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("ALL")
public class ShowHomeList {

    private final Player player;
    private Inventory inventory;

    public ShowHomeList(Player player) {
        this.player = player;
        String keyName = this.player.getUniqueId() + "_home";
        List<PlayerHome> playerHomeList = new ArrayList<>();
        if (!Main.redisValue.exists(keyName)){
            SqlSession sqlSession = Main.SQL_SESSIONS.openSession(true);
            try {
                playerHomeList = sqlSession.getMapper(PlayerFunction.class).getPlayerHome(this.player.getUniqueId().toString());
                Main.redisValue.set(this.player.getUniqueId() + "_home", JSONObject.toJSONString(playerHomeList));
                Main.redisValue.expire(keyName,43200);
            } catch (Exception exception){
                sqlSession.rollback();
                player.closeInventory();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("SQL.fail") + exception));
                return;
            } finally {
                sqlSession.close();
            }
        } else {
            playerHomeList = JSONObject.parseArray(Main.redisValue.get(keyName),PlayerHome.class);
        }
        FileConfiguration fileConfiguration = FileConfigs.fileConfigs.get("homelist");
        int rows = fileConfiguration.getInt("menu-settings.rows");
        this.inventory = Main.mainPlugin.getServer().createInventory(this.player, rows * 9,fileConfiguration.getString("menu-settings.name", "GUI"));
        if (playerHomeList.size() != 0){
            int index = 0;
            for (PlayerHome playerHome : playerHomeList){
                ItemStack itemStack = new ItemStack(Objects.requireNonNull(Material.getMaterial(playerHome.getMaterial())));
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(ChatColor.AQUA + playerHome.getHomeName());
                itemMeta.setLore(Arrays.asList(ChatColor.GRAY + "x: " + ChatColor.WHITE + playerHome.getLocationX() + ChatColor.GRAY + " y: " + ChatColor.WHITE + playerHome.getLocationY() + ChatColor.GRAY + " z: " + ChatColor.WHITE + playerHome.getLocationZ(), ChatColor.GRAY + "世界: " + ChatColor.WHITE + Main.mvWorldManager.getMVWorld(playerHome.getWorld()).getAlias(), "", ChatColor.GRAY + "左击传送", ChatColor.GRAY + "右击编辑"));
                itemStack.setItemMeta(itemMeta);
                this.inventory.setItem(index,itemStack);
                index++;
            }
        }
    }
    public void open(){
        this.player.openInventory(inventory);
    }
}
