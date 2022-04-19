package superarilo.main.function;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;

@SuppressWarnings("deprecation")
public class ItemStackBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemStackBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemStackBuilder(ItemStack itemStack, ItemMeta itemMeta) {
        this.itemStack = itemStack;
        this.itemMeta = itemMeta;
    }

    public ItemStack getItemStack(){
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }

    public ItemStackBuilder setDisplayName(String newName){
        if (newName == null || newName.equals("")){
            return this;
        }
        this.itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', newName));
        return this;
    }

    public ItemStackBuilder setDisplayNameOnPAPI (String newName, Player player){
        if (newName == null || newName.equals("")){
            return this;
        }
        this.itemMeta.setDisplayName(PlaceholderAPI.setPlaceholders(player, newName));
        return this;
    }

    public ItemStackBuilder setLore(Player player, List<String> lore){
        if (lore == null) return this;
        this.itemMeta.setLore(PlaceholderAPI.setPlaceholders(player, lore));
        return this;
    }
    public ItemStackBuilder setAmount(int amount){
        this.itemStack.setAmount(amount);
        return this;
    }
    public ItemStackBuilder addEnchants(int level, Enchantment... enchantments){
        if (enchantments == null) return this;
        for (Enchantment e : enchantments){
            if (e != null){
                this.itemMeta.addEnchant(e, level, true);
            }
        }
        return this;
    }
    public ItemStackBuilder removeEnchants(Enchantment... enchantments){
        if (enchantments == null) return this;
        for (Enchantment e : enchantments){
            if (e != null){
                this.itemMeta.removeEnchant(e);
            }
        }
        return this;
    }
    public ItemStackBuilder setUnbreakable(boolean unbreakable){
        this.itemMeta.setUnbreakable(unbreakable);
        return this;
    }
}
