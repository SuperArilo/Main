package superarilo.main.function;

import com.alibaba.fastjson.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;

public class SocketClient extends WebSocketClient {
    public SocketClient(URI serverUri){
        super(serverUri);
    }
    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        Bukkit.getLogger().info(ChatColor.GREEN + "已经成功连接到服务器后台！");
    }

    @Override
    public void onMessage(String message) {
        JSONObject object = JSONObject.parseObject(message);
        String type = object.getString("type");
        if (type.equals("web")){
            Bukkit.broadcastMessage(ChatColor.GOLD + "[" + type + "]" + ChatColor.YELLOW + object.getString("nickname") + ChatColor.GRAY + "(" + object.getString("mcJavaId") + " )" + ChatColor.GOLD + "说：" + ChatColor.YELLOW + object.getString("message"));

        }
    }
    @Override
    public void onClose(int i, String s, boolean b) {
    }
    @Override
    public void onError(Exception e) {
        Bukkit.getLogger().warning(ChatColor.RED + "在线聊天接口连接出错！ " + e);
    }
}
