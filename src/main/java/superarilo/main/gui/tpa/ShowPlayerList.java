package superarilo.main.gui.tpa;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import superarilo.main.Main;
import superarilo.main.function.FileConfigs;
import superarilo.main.function.FunctionTool;
import superarilo.main.gui.MainGui;

public class ShowPlayerList extends MainGui {

    public ShowPlayerList(Player player) {
        super(player);
        FileConfiguration fileConfiguration = FileConfigs.fileConfigs.get("tpalist");
        int rows = fileConfiguration.getInt("menu-settings.rows");
        this.inventory = Main.mainPlugin.getServer().createInventory(this.player, rows * 9, FunctionTool.setTextComponent(fileConfiguration.getString("menu-settings.name","GUI")));
        Main.mainPlugin.getServer().getScheduler().runTaskAsynchronously(Main.mainPlugin, () -> {
            int index = 0;
            String ownerName = player.getName();
            for (Player onlinePlayer : Main.mainPlugin.getServer().getOnlinePlayers()){
                if (!onlinePlayer.getName().equals(ownerName)){
                    if (index <= (rows - 1) * 9){
                        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
                        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
                        skullMeta.displayName(FunctionTool.setTextComponent(onlinePlayer.getName()));
                        skullMeta.lore(FunctionTool.setListTextComponent(PlaceholderAPI.setPlaceholders(onlinePlayer, fileConfiguration.getStringList("menu-settings.player-head-lore"))));
                        skullMeta.setOwningPlayer(onlinePlayer);
                        itemStack.setItemMeta(skullMeta);
                        this.inventory.setItem(index,itemStack);
                        index++;
                    }
                }
            }
        });
    }
}
