package superarilo.main.gui.warp;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import superarilo.main.Main;
import superarilo.main.function.FileConfigs;
import superarilo.main.function.FunctionTool;
import superarilo.main.gui.MainGui;

public class WarpMenu extends MainGui {

    public WarpMenu(Player player){
        super(player);
        FileConfiguration configuration = FileConfigs.fileConfigs.get("menu-gui");
        this.inventory = Main.mainPlugin.getServer().createInventory(player, configuration.getInt("menu-settings.rows") * 9, FunctionTool.setTextComponent(configuration.getString("menu-settings.name", "GUI")));
        Main.mainPlugin.getServer().getScheduler().runTaskAsynchronously(Main.mainPlugin, () -> {
           //Render Masks
            String materialName = configuration.getString("mask.material","DIRT").toUpperCase();
           for (int index : configuration.getIntegerList("mask.slot")){
               this.inventory.setItem(index, new ItemStack(Material.valueOf(materialName)));
           }
           //Render ServerIcon
           ItemStack serverStack = new ItemStack(Material.valueOf(configuration.getString("function.server.material", "DIRT").toUpperCase()));
           ItemMeta serverMeta = serverStack.getItemMeta();
           serverMeta.displayName(FunctionTool.setTextComponent(configuration.getString("function.server.name", "null")));
           serverMeta.lore(FunctionTool.setListTextComponent(PlaceholderAPI.setPlaceholders(player, configuration.getStringList("function.server.lore"))));
           //Set NBT
           serverMeta.getPersistentDataContainer().set(new NamespacedKey(Main.mainPlugin, configuration.getString("menu-warp-nbt.name-space", "null")), PersistentDataType.STRING, configuration.getString("function.server.type","null"));
           serverStack.setItemMeta(serverMeta);
           this.inventory.setItem(configuration.getInt("function.server.slot"), serverStack);

           //Render PlayerHead
           ItemStack itemStack = new ItemStack(Material.valueOf(configuration.getString("function.player.material","DIRT").toUpperCase()));
           SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
           skullMeta.setOwningPlayer(player);
           skullMeta.displayName(FunctionTool.setTextComponent(configuration.getString("function.player.name", "null")));
           skullMeta.lore(FunctionTool.setListTextComponent(PlaceholderAPI.setPlaceholders(player, configuration.getStringList("function.player.lore"))));
           //Ser NBT
           skullMeta.getPersistentDataContainer().set(new NamespacedKey(Main.mainPlugin, configuration.getString("menu-warp-nbt.name-space", "null")), PersistentDataType.STRING, configuration.getString("function.player.type","null"));
           itemStack.setItemMeta(skullMeta);
           this.inventory.setItem(configuration.getInt("function.player.slot"), itemStack);
        });
    }
}
