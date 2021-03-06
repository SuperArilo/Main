package superarilo.main;

import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import net.luckperms.api.LuckPerms;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import superarilo.main.PAPI.PlayerPI;
import superarilo.main.command.back.BackCommand;
import superarilo.main.command.home.DeleteHomeCommand;
import superarilo.main.command.home.HomeCommand;
import superarilo.main.command.home.SetHomeCommand;
import superarilo.main.command.tpa.*;
import superarilo.main.function.SocketClient;
import superarilo.main.command.ReloadCommand;
import superarilo.main.function.FileConfigs;
import superarilo.main.listener.home.AboutHome;
import superarilo.main.listener.player.AboutPlayer;
import superarilo.main.listener.WhitelistListener;
import superarilo.main.listener.cutree.CutreeListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import redis.clients.jedis.Jedis;

public final class Main extends JavaPlugin {

    public static JavaPlugin mainPlugin = null;
    public static SqlSessionFactory SQL_SESSIONS = null;
    public static SocketClient socketClient = null;
    public static Jedis redisValue = null;
    public static MVWorldManager mvWorldManager = null;
    public static LuckPerms luckPerms = null;

    @Override
    public void onLoad(){
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        mainPlugin = this;
        getLogger().info("加载配置文件");
        new FileConfigs().checkFiles();
        if(getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")){
            new PlayerPI().register();

        }
        RegisteredServiceProvider<LuckPerms> provider = getServer().getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();

        } else {
            getLogger().warning("权限插件加载失败！");
        }
        MultiverseCore core = (MultiverseCore) getServer().getPluginManager().getPlugin("Multiverse-Core");
        if (core != null){
            mvWorldManager = core.getMVWorldManager();
        } else {
            getLogger().warning("多世界插件加载失败！！");
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
        SQL_SESSIONS = null;
        //关闭socket链接
        if(getConfig().getBoolean("online-talk.enable")){
            socketClient.close();
            socketClient = null;
        }
        //关闭redis链接
        if(getConfig().getBoolean("redis.enable")){
            if (redisValue != null){
                redisValue.close();
                redisValue = null;
            }
        }
        getLogger().info("关闭所有任务");
        getServer().getScheduler().cancelTasks(this);
    }
    private void registerCommands(){
        Objects.requireNonNull(getServer().getPluginCommand("versailles")).setExecutor(new ReloadCommand());
        Objects.requireNonNull(getServer().getPluginCommand("versailles")).setTabCompleter(new ReloadCommand());
        Objects.requireNonNull(getServer().getPluginCommand("tpa")).setExecutor(new TpaCommand());
        Objects.requireNonNull(getServer().getPluginCommand("tpaccept")).setExecutor(new TpAcceptCommand());
        Objects.requireNonNull(getServer().getPluginCommand("tparefuse")).setExecutor(new TpaRefuseCommand());
        Objects.requireNonNull(getServer().getPluginCommand("tpahere")).setExecutor(new TpaHereCommand());
        Objects.requireNonNull(getServer().getPluginCommand("tpalist")).setExecutor(new TpaList());
        Objects.requireNonNull(getServer().getPluginCommand("home")).setExecutor(new HomeCommand());
        Objects.requireNonNull(getServer().getPluginCommand("back")).setExecutor(new BackCommand());
        Objects.requireNonNull(getServer().getPluginCommand("sethome")).setExecutor(new SetHomeCommand());
        Objects.requireNonNull(getServer().getPluginCommand("delhome")).setExecutor(new DeleteHomeCommand());
//        Objects.requireNonNull(getServer().getPluginCommand("warp")).setExecutor(new WarpCommand());
//        Objects.requireNonNull(getServer().getPluginCommand("setwarp")).setExecutor(new SetWarpCommand());
    }
    private void registerEvents(){
        getServer().getPluginManager().registerEvents(new WhitelistListener(), this);
        getServer().getPluginManager().registerEvents(new CutreeListener(),this);
        getServer().getPluginManager().registerEvents(new AboutPlayer(), this);
        getServer().getPluginManager().registerEvents(new AboutHome(), this);
//        getServer().getPluginManager().registerEvents(new WarpListener(), this);
    }

    public static void startSQL(){
        try {
            SQL_SESSIONS = new MybatisSqlSessionFactoryBuilder().build(Resources.getResourceAsStream("MybatisPlus.xml"));
            mainPlugin.getLogger().info("数据库工厂初始化成功！");
        }
        catch(IOException e) {
            mainPlugin.getLogger().warning("Mybatis配置文件错误！");
        }
        catch(Exception e) {
            e.printStackTrace();
            mainPlugin.getLogger().warning("SQL出错！");
        }
    }
    public static void startSocket(){
        if (mainPlugin.getConfig().getBoolean("online-talk.enable")){
            try {
                socketClient = new SocketClient(new URI(mainPlugin.getConfig().getString("online-talk.url", "bull")));
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
                mainPlugin.getLogger().info("redis初始化连接成功！");
            } else {
                mainPlugin.getLogger().warning("redis初始化连接失败！！！！");
            }
        }
    }
}
