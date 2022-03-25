package superarilo.main.gui.home;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import superarilo.main.Main;
import superarilo.main.entity.PlayerHome;
import superarilo.main.function.FileConfigs;
import superarilo.main.mapper.PlayerFunction;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class ShowHomeList {
    private final Player player;
    private final Inventory inventory;
    public ShowHomeList(Player player) {
        this.player = player;
        ConfigurationSection fileConfiguration = FileConfigs.fileConfigs.get("homelist");
        int rows = fileConfiguration.getInt("menu-settings.rows");
        this.inventory = Main.mainPlugin.getServer().createInventory(this.player,rows * 9,fileConfiguration.getString("menu-settings.name", "GUI"));
        List<PlayerHome> playerHomeList = Main.SQL_SESSIONS.openSession().getMapper(PlayerFunction.class).getPlayerHome(this.player.getName(),this.player.getUniqueId().toString());
        if (playerHomeList.size() != 0){
            int index = 0;
            for (PlayerHome playerHome : playerHomeList){
                ItemStack itemStack = new ItemStack(Material.getMaterial(playerHome.getMaterial()));
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(ChatColor.WHITE + playerHome.getHomeName());
                String location = ChatColor.GREEN + "x: " + ChatColor.WHITE + playerHome.getLocationX() + ChatColor.GREEN + " y: " + ChatColor.WHITE + playerHome.getLocationY() + ChatColor.GREEN + " z: " + ChatColor.WHITE + playerHome.getLocationZ();
                String world = ChatColor.GREEN + "世界:" + ChatColor.WHITE + playerHome.getWorld();
                List<String> loreList = new ArrayList<>();
                loreList.add(location);
                loreList.add(world);
                itemMeta.setLore(loreList);
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
