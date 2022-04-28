package superarilo.main.gui.warp;

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
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import superarilo.main.Main;
import superarilo.main.entity.Warp;
import superarilo.main.function.FileConfigs;
import superarilo.main.function.FunctionTool;
import superarilo.main.mapper.WarpFunction;
import java.util.Arrays;
import java.util.List;

public class WarpListMenu {

    private final Player player;
    private final MenuType menuType;
    private final Inventory inventory;
    private final FileConfiguration configuration = FileConfigs.fileConfigs.get("warp-list-gui");
    private List<Warp> warpList = null;
    private Integer warpTotal = null;


    public WarpListMenu(Player player, @NotNull MenuType menuType){
        this.player = player;
        this.menuType = menuType;
        this.inventory = Main.mainPlugin.getServer().createInventory(player, configuration.getInt("menu-settings.rows", 5) * 9, FunctionTool.setTextComponent(configuration.getString("menu-settings.name", "GUI")));
    }

    public void open(){
        this.player.openInventory(this.inventory);
        Main.mainPlugin.getServer().getScheduler().runTaskAsynchronously(Main.mainPlugin, () -> {
            renderFunctionMasks();
            renderLoading();
            getDateBase();
        });
    }

    private void renderFunctionMasks(){
        //Render Masks
        String materialName = configuration.getString("mask.material", "DIRT").toUpperCase();
        for (int index : configuration.getIntegerList("mask.slot")){
            ItemStack itemStack = new ItemStack(Material.valueOf(materialName));
            this.inventory.setItem(index, itemStack);
        }
        //Render Back
        ItemStack backItem = new ItemStack(Material.valueOf(configuration.getString("function.back.material", "DIRT").toUpperCase()));
        ItemMeta itemMeta = backItem.getItemMeta();
        itemMeta.displayName(FunctionTool.setTextComponent(configuration.getString("function.back.name", "null")));
        itemMeta.lore(FunctionTool.setListTextComponent(configuration.getStringList("function.back.lore")));
        //Set NBT
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(Main.mainPlugin, configuration.getString("warp-list-nbt.name-space", "null")), PersistentDataType.STRING, configuration.getString("function.back.type","null"));
        backItem.setItemMeta(itemMeta);
        this.inventory.setItem(configuration.getInt("function.back.slot"), backItem);
    }

    private void renderLoading(){
        ItemStack itemStack = new ItemStack(Material.valueOf(configuration.getString("function.loading.material", "DIRT").toUpperCase()));
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP,1,1), true);
        potionMeta.displayName(FunctionTool.setTextComponent(configuration.getString("function.loading.name", "null")));
        potionMeta.lore(FunctionTool.setListTextComponent(configuration.getStringList("function.loading.lore")));
        itemStack.setItemMeta(potionMeta);
        this.inventory.setItem(configuration.getInt("function.loading.slot"), itemStack);
    }

    private void getDateBase(){
        SqlSession sqlSession = Main.SQL_SESSIONS.openSession(true);
        String className;
        if (this.menuType == MenuType.SERVERWARP){
            className = "server";
        } else {
            className = "player";
        }
        try {
            this.warpList = sqlSession.getMapper(WarpFunction.class).getWarpList(0, 36, className);
            this.warpTotal = sqlSession.getMapper(WarpFunction.class).getWarpCount(className);
        } catch (Exception exception){
            sqlSession.rollback();
            exception.printStackTrace();
            Main.mainPlugin.getServer().getScheduler().runTask(Main.mainPlugin, this.inventory::close);
            return;
        } finally {
            sqlSession.close();
        }
        renderWarpList();
        renderDone();
    }
    private void renderWarpList(){
        List<Integer> indexList = configuration.getIntegerList("warp-item.slot");
        if (warpList.size() == 0) return;
        for (int index = 0;index < warpList.size();index++){
            Warp warp = warpList.get(index);
            ItemStack itemStack = new ItemStack(Material.valueOf(warp.getMaterial().toUpperCase()));
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.displayName(FunctionTool.setTextComponent("&a名称: " + "&b" + warp.getWarpName()));
            if (warp.getWarpName().equals("server")){
                itemMeta.lore(FunctionTool.setListTextComponent(Arrays.asList(ChatColor.GRAY + "x: " + ChatColor.WHITE + warp.getLocationX() + ChatColor.GRAY + " y: " + ChatColor.WHITE + warp.getLocationY() + ChatColor.GRAY + " z: " + ChatColor.WHITE + warp.getLocationZ(), ChatColor.GRAY + "世界: " + ChatColor.WHITE + warp.getWorldAlias(), ChatColor.GRAY + "左击传送", ChatColor.GRAY + "右击编辑（管理员）")));
            } else {
                itemMeta.lore(FunctionTool.setListTextComponent(Arrays.asList(ChatColor.GRAY + "x: " + ChatColor.WHITE + warp.getLocationX() + ChatColor.GRAY + " y: " + ChatColor.WHITE + warp.getLocationY() + ChatColor.GRAY + " z: " + ChatColor.WHITE + warp.getLocationZ(), ChatColor.GRAY + "世界: " + ChatColor.WHITE + warp.getWorldAlias(), ChatColor.GRAY + "左击传送", ChatColor.GRAY + "右击编辑")));
            }
            itemMeta.getPersistentDataContainer().set(new NamespacedKey(Main.mainPlugin, configuration.getString("warp-list-nbt.name-space", "null")), PersistentDataType.STRING, configuration.getString("warp-item.type","null"));
            itemStack.setItemMeta(itemMeta);
            this.inventory.setItem(indexList.get(index), itemStack);
        }
    }

    private void renderDone(){
        ItemStack itemStack = new ItemStack(Material.valueOf(configuration.getString("mask.material", "DIRT").toUpperCase()));
        int chestCount = configuration.getInt("menu-settings.rows", 5) * 9;
        this.inventory.setItem(configuration.getInt("function.loading.slot"), itemStack);
        if (this.warpTotal < chestCount) {
            renderNoNextBarrier();
            renderNoPrevBarrier();
        } else if (this.warpList.size() <= chestCount && this.warpTotal > chestCount){
            renderPrevPage();
            renderNoNextBarrier();
        } else if (this.warpList.size() == chestCount || this.warpTotal > chestCount){
            renderPrevPage();
            renderNextPage();
        }
    }

    private void renderPrevPage(){
        //Render Previous Page
        ItemStack preItem = new ItemStack(Material.valueOf(configuration.getString("function.prev.material","DIRT").toUpperCase()));
        ItemMeta preMeta = preItem.getItemMeta();
        preMeta.displayName(FunctionTool.setTextComponent(configuration.getString("function.prev.name", "null")));
        preMeta.lore(FunctionTool.setListTextComponent(configuration.getStringList("function.prev.lore")));
        //Set NBT
        preMeta.getPersistentDataContainer().set(new NamespacedKey(Main.mainPlugin, configuration.getString("warp-list-nbt.name-space", "null")), PersistentDataType.STRING, configuration.getString("function.prev.type","null"));
        preItem.setItemMeta(preMeta);
        this.inventory.setItem(configuration.getInt("function.prev.slot"), preItem);
    }

    private void renderNextPage(){
        //Render Next Page
        ItemStack nextItem = new ItemStack(Material.valueOf(configuration.getString("function.next.material","DIRT").toUpperCase()));
        ItemMeta nextMeta = nextItem.getItemMeta();
        nextMeta.displayName(FunctionTool.setTextComponent(configuration.getString("function.next.name", "null")));
        nextMeta.lore(FunctionTool.setListTextComponent(configuration.getStringList("function.next.lore")));
        //Set NBT
        nextMeta.getPersistentDataContainer().set(new NamespacedKey(Main.mainPlugin, configuration.getString("warp-list-nbt.name-space", "null")), PersistentDataType.STRING, configuration.getString("function.next.type","null"));
        nextItem.setItemMeta(nextMeta);
        this.inventory.setItem(configuration.getInt("function.next.slot"), nextItem);
    }

    private void renderNoPrevBarrier(){
        ItemStack barrierItem = new ItemStack(Material.valueOf(configuration.getString("function.noprev.material","DIRT").toUpperCase()));
        ItemMeta barrierMeta = barrierItem.getItemMeta();
        barrierMeta.displayName(FunctionTool.setTextComponent(configuration.getString("function.noprev.name", "null")));
        barrierMeta.lore(FunctionTool.setListTextComponent(configuration.getStringList("function.noprev.lore")));
        barrierItem.setItemMeta(barrierMeta);
        this.inventory.setItem(configuration.getInt("function.prev.slot"), barrierItem);
    }

    private void renderNoNextBarrier(){
        ItemStack barrierItem = new ItemStack(Material.valueOf(configuration.getString("function.nonext.material","DIRT").toUpperCase()));
        ItemMeta barrierMeta = barrierItem.getItemMeta();
        barrierMeta.displayName(FunctionTool.setTextComponent(configuration.getString("function.nonext.name", "null")));
        barrierMeta.lore(FunctionTool.setListTextComponent(configuration.getStringList("function.nonext.lore")));
        barrierItem.setItemMeta(barrierMeta);
        this.inventory.setItem(configuration.getInt("function.nonext.slot"), barrierItem);
    }

    public enum MenuType{
        SERVERWARP,
        PLAYERWARP
    }
}
