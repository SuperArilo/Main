package superarilo.main.listener.cutree;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import superarilo.main.function.FileConfigs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CutreeListener implements Listener {
    private final List<String> position = Arrays.asList("UP","DOWN","NORTH","EAST","SOUTH","WEST","NORTH_EAST","NORTH_WEST","SOUTH_EAST","SOUTH_WEST");
    @EventHandler
    public void breakTree(BlockBreakEvent event){
        FileConfiguration fileConfiguration = FileConfigs.fileConfigs.get("cutree");
        Block block = event.getBlock();
        if(fileConfiguration.getStringList("disable-worlds").contains(block.getWorld().getName())) return;
        List<String> logList = fileConfiguration.getStringList("logs-list");
        if(!logList.contains(block.getType().name())) return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!fileConfiguration.getStringList("tools-list").contains(item.getType().name())) return;
        List<String> leafList = new ArrayList<>();
        if (fileConfiguration.getBoolean("cut-leaf")){
            leafList.addAll(fileConfiguration.getStringList("leafs-list"));
        }
        breakBlock(block, item ,logList , leafList);
    }
    private void breakBlock(Block block, ItemStack itemStack, List<String> logList, List<String> leafList){
        for (String positionNow : position){
            Block blockRelative = block.getRelative(BlockFace.valueOf(positionNow));
            if (logList.contains(blockRelative.getType().name())){
                setDamage(itemStack, logList, leafList, blockRelative);
            } else if(leafList.contains(blockRelative.getType().name())){
                for (int x = -3;x <= 3;x++){
                    for (int z = -3;z <= 3;z++){
                        Block checkTwice = blockRelative.getRelative(x,0,z);
                        if (logList.contains(checkTwice.getType().name())){
                            setDamage(itemStack, logList, leafList, checkTwice);
                        } else if (leafList.contains(checkTwice.getType().name())){
                            checkTwice.breakNaturally(itemStack);
                        }
                    }
                }
            }
        }
    }
    private void setDamage(ItemStack itemStack, List<String> logList, List<String> leafList, Block checkTwice) {
        Damageable damageable = (Damageable) itemStack.getItemMeta();
        short damageValue = (short) (damageable.getDamage() + 1);
        if (damageValue <= itemStack.getType().getMaxDurability()){
            damageable.setDamage(damageValue);
            itemStack.setItemMeta(damageable);
            checkTwice.breakNaturally(itemStack);
            breakBlock(checkTwice,itemStack,logList, leafList);
        }
    }
}
