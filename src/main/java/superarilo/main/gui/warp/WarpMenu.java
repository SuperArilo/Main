package superarilo.main.gui.warp;

import io.papermc.paper.text.PaperComponents;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import superarilo.main.Main;
import superarilo.main.function.FileConfigs;
import superarilo.main.function.ItemStackBuilder;
import superarilo.main.gui.MainGui;

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
           this.inventory.setItem(configuration.getInt("function.server.slot"), new ItemStackBuilder(new ItemStack(Material.valueOf(configuration.getString("function.server.material", "Dirt").toUpperCase()))).setDisplayName(configuration.getString("function.server.name")).setLore(player, configuration.getStringList("function.server.lore")).getItemStack());
           ItemStack itemStack = new ItemStack(Material.valueOf(configuration.getString("function.player.material","DIRT").toUpperCase()));
           SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
           skullMeta.setOwningPlayer(player);
           skullMeta.displayName(PaperComponents.plainTextSerializer().deserialize(ChatColor.translateAlternateColorCodes('&', configuration.getString("function.player.name", "null"))));
           skullMeta.setLore(PlaceholderAPI.setPlaceholders(player, configuration.getStringList("function.player.lore")));
           itemStack.setItemMeta(skullMeta);
           this.inventory.setItem(configuration.getInt("function.player.slot"), itemStack);
        });
    }
}
