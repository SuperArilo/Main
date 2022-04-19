package superarilo.main.gui.warp;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import superarilo.main.Main;
import superarilo.main.function.FileConfigs;
import superarilo.main.function.ItemStackBuilder;
import superarilo.main.gui.MainGui;

@SuppressWarnings("deprecation")
public class WarpMenu extends MainGui {

    public WarpMenu(Player player){
        super(player);
        FileConfiguration configuration = FileConfigs.fileConfigs.get("menu-gui");
        this.inventory = Main.mainPlugin.getServer().createInventory(player, configuration.getInt("menu-settings.rows") * 9, configuration.getString("menu-settings.name", "GUI"));
        Main.mainPlugin.getServer().getScheduler().runTaskAsynchronously(Main.mainPlugin, () -> {
           //Render Masks
           for (int index : configuration.getIntegerList("mask.slot")){
               this.inventory.setItem(index, new ItemStack(Material.valueOf(configuration.getString("mask.material","DIRT").toUpperCase())));
           }
           ConfigurationSection section = configuration.getConfigurationSection("function");
           if (section == null) return;
           for (String path : section.getKeys(false)){
               this.inventory.setItem(section.getInt(path + ".slot"), new ItemStackBuilder(new ItemStack(Material.valueOf(section.getString(path + ".material", "DIRT").toUpperCase()))).setDisplayName(section.getString(path + ".name")).setLore(player, section.getStringList(path + ".lore")).getItemStack());
           }
        });
    }
}
