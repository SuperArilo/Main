package superarilo.main.listener;

import net.kyori.adventure.text.Component;
import org.apache.ibatis.session.SqlSession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import superarilo.main.Main;
import superarilo.main.mapper.PlayerFunction;

public class WhitelistListener implements Listener {

    @EventHandler
    public void getPlayerBlock(AsyncPlayerPreLoginEvent event){
        if (Main.mainPlugin.getConfig().getBoolean("white-list")){
            SqlSession sqlSession = Main.SQL_SESSIONS.openSession(true);
            try {
                if(sqlSession.getMapper(PlayerFunction.class).whitelistCheckUser(event.getName(), event.getUniqueId().toString()) == 0){
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, Component.text("§f您尚未获得§c白名单§f。请访问 §awww.superarilo.icu §f申请"));
                } else {
                    event.allow();
                }
            } catch (Exception exception) {
                sqlSession.rollback();
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text("§c初始化连接数据库异常，请尝试重新登陆服务器！"));
            } finally {
                sqlSession.close();
            }
        }
    }

}
