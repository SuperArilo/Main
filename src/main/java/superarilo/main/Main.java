package superarilo.main;

import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import superarilo.main.PAPI.PlayerPI;
import superarilo.main.command.tpa.TpaCommand;
import superarilo.main.function.SocketClient;
import superarilo.main.command.ReloadCommand;
import superarilo.main.function.FileConfigs;
import superarilo.main.listener.WhitelistListener;
import superarilo.main.listener.cutree.CutreeListener;
import superarilo.main.listener.onlinetalk.OnlineTalkListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import redis.clients.jedis.Jedis;

public final class Main extends JavaPlugin {

    public static JavaPlugin mainPlugin;
    public static SqlSessionFactory SQL_SESSIONS = null;
    public static SocketClient socketClient = null;
    public static Jedis redisValue = null;

    @Override
    public void onLoad(){
        saveDefaultConfig();
        new FileConfigs(this).checkFiles();
    }

    @Override
    public void onEnable() {
        mainPlugin = this;
        if(getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")){
            new PlayerPI().register();
        }
        startSQL(); //开始连接数据库
        startRedis(); //开始连接redis
        startSocket(); //开始连接Socket

        registerEvents();
        registerCommands();
    }

    @Override
    public void onDisable() {
        saveConfig(); //保存数据
        //关闭数据库链接
        SQL_SESSIONS.openSession().close();
        SQL_SESSIONS = null;
        //关闭socket链接
        if(getConfig().getBoolean("online-talk.enable")){
            socketClient.close();
            socketClient = null;
        }
        //关闭redis链接
        if(getConfig().getBoolean("redis.enable")){
            redisValue.close();
            redisValue = null;
        }
    }
    private void registerCommands(){
        getServer().getPluginCommand("versailles").setExecutor(new ReloadCommand());
        getServer().getPluginCommand("versailles").setTabCompleter(new ReloadCommand());
        getServer().getPluginCommand("tpa").setExecutor(new TpaCommand());
        getServer().getPluginCommand("tpa").setTabCompleter(new TpaCommand());
    }
    private void registerEvents(){
        getServer().getPluginManager().registerEvents(new WhitelistListener(), this);
        getServer().getPluginManager().registerEvents(new CutreeListener(),this);
        getServer().getPluginManager().registerEvents(new OnlineTalkListener(),this);
    }

    public static void startSQL(){
        try {
            SQL_SESSIONS = new MybatisSqlSessionFactoryBuilder().build(Resources.getResourceAsStream("MybatisPlus.xml"));
            mainPlugin.getLogger().info("数据库初始化成功！");
        }
        catch(IOException e) {
            mainPlugin.getLogger().warning("Mybatis配置文件错误！");
        }
        catch(Exception e) {
            mainPlugin.getLogger().warning("SQL出错！");
        }
    }
    public static void startSocket(){
        if (mainPlugin.getConfig().getBoolean("online-talk.enable")){
            try {
                socketClient = new SocketClient(new URI(mainPlugin.getConfig().getString("online-talk.url")));
                mainPlugin.getLogger().info(ChatColor.GREEN  + "已成功连接到服务器后台！");
                socketClient.connect();
            } catch (URISyntaxException e) {
                mainPlugin.getLogger().warning("在线聊天：无法连接到服务器后台！正在关闭链接！");
                socketClient.close();
            }
        }
    }
    public static void startRedis(){
        if(mainPlugin.getConfig().getBoolean("redis.enable")){
            redisValue = new Jedis(mainPlugin.getConfig().getString("redis.host"), mainPlugin.getConfig().getInt("redis.port"));
            redisValue.auth(mainPlugin.getConfig().getString("redis.password"));
            redisValue.connect();
            if(redisValue.ping().equals("PONG")){
                mainPlugin.getServer().getLogger().info("redis初始化连接成功！");
            } else {
                mainPlugin.getServer().getLogger().warning("redis初始化连接失败！！！！");
            }
        }
    }
}
