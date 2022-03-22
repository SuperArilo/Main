package superarilo.main.PAPI;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlayerPI extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "vs_player";
    }

    @Override
    public String getAuthor() {
        return null;
    }

    @Override
    public String getVersion() {
        return null;
    }
    public String onPlaceholderRequest(Player player, String identifier){
        if(identifier.equalsIgnoreCase("sender")){
            return null;
        }
        return null;
    }
}
