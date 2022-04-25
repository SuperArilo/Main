package superarilo.main.listener.warp;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import superarilo.main.Main;
import superarilo.main.function.FileConfigs;
import superarilo.main.function.FunctionTool;
import superarilo.main.function.warp.WarpListFunction;
import superarilo.main.gui.warp.WarpListMenu;

public class WarpListener implements Listener {

    @EventHandler
    public void warpMenuChoiceGui(InventoryClickEvent event){
        InventoryView view = event.getView();
        if (view.title().equals(FunctionTool.setTextComponent(FileConfigs.fileConfigs.get("menu-gui").getString("menu-settings.name", "GUI")))){
            event.setCancelled(true);
            ItemStack currentItem = event.getCurrentItem();
            if (currentItem == null) return;
            String nbtType = currentItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.mainPlugin, FileConfigs.fileConfigs.get("menu-gui").getString("menu-warp-nbt.name-space", "null")), PersistentDataType.STRING);
            if (nbtType == null || nbtType.equals("null")) return;
            new WarpListMenu((Player) event.getWhoClicked(), WarpListMenu.MenuType.valueOf(nbtType.toUpperCase())).open();
        }
    }

    @EventHandler
    public void warpListFunction(InventoryClickEvent event){
        InventoryView view = event.getView();
        if (view.title().equals(FunctionTool.setTextComponent(FileConfigs.fileConfigs.get("warp-list-gui").getString("menu-settings.name", "GUI")))){
            event.setCancelled(true);
            ItemStack currentItem = event.getCurrentItem();
            if (currentItem == null) return;
            String nbtType = currentItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.mainPlugin, FileConfigs.fileConfigs.get("warp-list-gui").getString("warp-list-nbt.name-space", "null")), PersistentDataType.STRING);
            if (nbtType == null || nbtType.equals("null")) return;
            new WarpListFunction((Player) event.getWhoClicked(), event.getSlot(), WarpListFunction.WarpListFunctionType.valueOf(nbtType.toUpperCase())).doThis();
        }
    }
}
