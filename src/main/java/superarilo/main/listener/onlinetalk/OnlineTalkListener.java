package superarilo.main.listener.onlinetalk;

import com.alibaba.fastjson.JSONObject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import superarilo.main.Main;

public class OnlineTalkListener implements Listener {

    @EventHandler
    public void getPlayerMessage(AsyncPlayerChatEvent event){
        if(Main.mainPlugin.getConfig().getBoolean("online-talk.enable")){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mcJavaId", event.getPlayer().getName());
            jsonObject.put("message", event.getMessage());
            jsonObject.put("type", "minecraft");
            jsonObject.put("mc_uuid", event.getPlayer().getUniqueId());
            Main.socketClient.send(jsonObject.toJSONString());
        }
    }
}
