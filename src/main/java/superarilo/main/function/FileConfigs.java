package superarilo.main.function;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import superarilo.main.Main;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileConfigs {
    public static Map<String,FileConfiguration> fileConfigs = new HashMap<>();
    private final JavaPlugin plugin;

    public FileConfigs(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    public void checkFiles(){
        for (Map<?, ?> map : plugin.getConfig().getMapList("files-path")){
            String publicPath = map.get("path").toString();
            File fileGet = new File(plugin.getDataFolder(),publicPath);
            if (!fileGet.exists()){
                plugin.saveResource(publicPath, false);
            }
            fileConfigs.put(map.get("name").toString(), YamlConfiguration.loadConfiguration(fileGet));
        }
    }
    public boolean reloadAllConfigs(){
        reloadSQL(); //重启sql
        reloadSocket(); //重启socket链接
        reloadRedis(); //重启redis连接
        fileConfigs.clear(); //清空配置文件集合，然后重新写入
        checkFiles();
        return true;
    }
    public boolean reloadSQL(){
        if(Main.SQL_SESSIONS != null){
            plugin.getLogger().info("正在关闭数据库连接...");
            Main.SQL_SESSIONS.openSession().close();
            Main.SQL_SESSIONS = null;
        }
        Main.startSQL();
        return true;
    }
    public boolean reloadSocket(){
        if (Main.socketClient != null){
            Main.socketClient.close();
            plugin.getLogger().info("socket关闭完成！");
            Main.socketClient = null;
        }
        if(plugin.getConfig().getBoolean("online-talk.enable")){
            plugin.getLogger().info("重新socket连接中...");
            Main.startSocket();
        }
        return true;
    }
    public boolean reloadRedis(){
        if(Main.redisValue != null){
            Main.redisValue.close();
            Main.redisValue = null;
            plugin.getLogger().info("redis关闭完成！");
        }
        if(plugin.getConfig().getBoolean("redis.enable")){
            plugin.getLogger().info("重启redis连接中...");
            Main.startRedis();
        }
        return true;
    }

}
