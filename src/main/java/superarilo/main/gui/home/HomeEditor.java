package superarilo.main.gui.home;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import superarilo.main.Main;
import superarilo.main.entity.PlayerHome;
import superarilo.main.function.FileConfigs;
import superarilo.main.gui.MainGui;

@SuppressWarnings("deprecation")
public class HomeEditor extends MainGui {

    public HomeEditor(Player player, PlayerHome playerHome) {
        super(player);
        FileConfiguration fileCfg = FileConfigs.fileConfigs.get("homeEditor");
        this.inventory = Main.mainPlugin.getServer().createInventory(player, 54, fileCfg.getString("menu-settings.name", "GUI"));
        Main.mainPlugin.getServer().getScheduler().runTaskAsynchronously(Main.mainPlugin, () -> {
            for (int index : fileCfg.getIntegerList("mask.slot")){
                ItemStack itemStack = new ItemStack(Material.valueOf(fileCfg.getString("mask.material", "DIRT").toUpperCase()));
                this.inventory.setItem(index, itemStack);
            }
            ConfigurationSection secondCfg = fileCfg.getConfigurationSection("function");
            if (secondCfg == null) return;
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
        });

    }
}
