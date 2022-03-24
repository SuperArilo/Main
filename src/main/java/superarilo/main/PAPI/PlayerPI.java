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
        if (identifier.equalsIgnoreCase("player_x")){
            return String.valueOf(player.getLocation().getBlockX());
        }
        if(identifier.equalsIgnoreCase("player_y")){
            return String.valueOf(player.getLocation().getBlockY());
        }
        if (identifier.equalsIgnoreCase("player_z")){
            return String.valueOf(player.getLocation().getBlockZ());
        }
        if (identifier.equalsIgnoreCase("player_at_world")){
            return player.getWorld().getName();
        }
        return null;
    }
}
