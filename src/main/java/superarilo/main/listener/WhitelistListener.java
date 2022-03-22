package superarilo.main.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import superarilo.main.Main;
import superarilo.main.mapper.PlayerFunction;

public class WhitelistListener implements Listener {

    @EventHandler
    public void getPlayerBlock(AsyncPlayerPreLoginEvent event){
        if (Main.mainPlugin.getConfig().getBoolean("white-list")){
            if(Main.SQL_SESSIONS.openSession().getMapper(PlayerFunction.class).whitelistCheckUser(event.getName(), event.getUniqueId().toString()) == 0){
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, Component.text("§f您尚未获得§c白名单§f。请访问 §awww.superarilo.icu §f申请"));
            } else {
                event.allow();
            }
        }
    }

}
