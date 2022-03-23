package superarilo.main.PAPI;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerPI extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "vs";
    }

    @Override
    public String getAuthor() {
        return null;
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
    public String onPlaceholderRequest(Player player, String identifier){
        if(identifier.equalsIgnoreCase("tpa_sender")){
            return player.getName();
        }
        if (identifier.equalsIgnoreCase("tpa_be_player")){
            return player.getName();
        }
        return null;
    }
}
