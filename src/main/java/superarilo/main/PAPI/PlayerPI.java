package superarilo.main.PAPI;

import com.alibaba.fastjson.JSONObject;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import superarilo.main.Main;
import superarilo.main.entity.PlayerHome;

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
        if(identifier.equalsIgnoreCase("player_name")){
            return player.getName();
        }
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
        if (identifier.equalsIgnoreCase("home_name")){
            String keyName = player.getUniqueId() + "_editor_home";
            return Main.redisValue.exists(keyName) ? JSONObject.parseObject(Main.redisValue.get(keyName), PlayerHome.class).getHomeName() : null;
        }
        if (identifier.equalsIgnoreCase("home_x")){
            String keyName = player.getUniqueId() + "_editor_home";
            return String.valueOf(Main.redisValue.exists(keyName) ? JSONObject.parseObject(Main.redisValue.get(keyName), PlayerHome.class).getLocationX() : null);
        }
        if (identifier.equalsIgnoreCase("home_y")){
            String keyName = player.getUniqueId() + "_editor_home";
            return String.valueOf(Main.redisValue.exists(keyName) ? JSONObject.parseObject(Main.redisValue.get(keyName), PlayerHome.class).getLocationY() : null);
        }
        if (identifier.equalsIgnoreCase("home_z")){
            String keyName = player.getUniqueId() + "_editor_home";
            return String.valueOf(Main.redisValue.exists(keyName) ? JSONObject.parseObject(Main.redisValue.get(keyName), PlayerHome.class).getLocationZ() : null);
        }
        return null;
    }

}
