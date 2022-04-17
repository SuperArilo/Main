package superarilo.main.gui.home;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import superarilo.main.Main;
import superarilo.main.entity.PlayerHome;
import superarilo.main.function.FileConfigs;

public class HomeEditor {

    private final Player player;
    private final Inventory inventory;

    public HomeEditor(Player player, PlayerHome playerHome) {
        this.player = player;
        FileConfiguration fileCfg = FileConfigs.fileConfigs.get("homeEditor");
        this.inventory = Main.mainPlugin.getServer().createInventory(this.player, 54, fileCfg.getString("menu-settings.name", "GUI"));
        for (int index : fileCfg.getIntegerList("mask.slot")){
            ItemStack itemStack = new ItemStack(Material.valueOf(fileCfg.getString("mask.material").toUpperCase()));
            this.inventory.setItem(index, itemStack);
        }
        ConfigurationSection secondCfg = fileCfg.getConfigurationSection("function");
        for (String pathName : secondCfg.getKeys(false)){
            String materialName = secondCfg.getString(pathName + ".material", null);
            ItemStack itemStack;
            if (materialName == null) {
                itemStack = new ItemStack(Material.valueOf(playerHome.getMaterial().toUpperCase()));
            } else {
                itemStack = new ItemStack(Material.valueOf(materialName.toUpperCase()));
            }
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', secondCfg.getString(pathName + ".name", "null")));
            itemMeta.setLore(PlaceholderAPI.setPlaceholders(player, secondCfg.getStringList(pathName + ".lore")));
            //set nbt 标签
            itemMeta.getPersistentDataContainer().set(new NamespacedKey(Main.mainPlugin, fileCfg.getString("home-nbt.name-space", "null")), PersistentDataType.STRING, secondCfg.getString(pathName + ".type", "null"));

            itemStack.setItemMeta(itemMeta);
            this.inventory.setItem(secondCfg.getInt(pathName + ".slot"), itemStack);
        }
    }
    public void open(){
        this.player.openInventory(inventory);
    }
}