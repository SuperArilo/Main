package superarilo.main.function.home.Impl;

import org.apache.ibatis.session.SqlSession;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import superarilo.main.Main;
import superarilo.main.entity.PlayerHome;
import superarilo.main.function.FileConfigs;
import superarilo.main.function.GetPlayerInfo;
import superarilo.main.function.home.HomeManager;
import superarilo.main.mapper.PlayerHomeFunction;
import java.text.DecimalFormat;

public class HomeManagerImpl extends HomeOnRedisImpl implements HomeManager {

    private final Player player;
    private final String serverPrefix = Main.mainPlugin.getConfig().getString("prefix");
    private final FileConfiguration messageCfg = FileConfigs.fileConfigs.get("message");

    public HomeManagerImpl(Player player) {
        super(player.getUniqueId().toString());
        this.player = player;
    }

    @Override
    public void createNewHome(String homeId) {
        Main.mainPlugin.getServer().getScheduler().runTaskAsynchronously(Main.mainPlugin, () -> {
            SqlSession sqlSession = Main.SQL_SESSIONS.openSession(true);
            try {
                if (sqlSession.getMapper(PlayerHomeFunction.class).getHomeQuantity(player.getUniqueId().toString()) >= FileConfigs.fileConfigs.get("home").getInt("homes-group." + new GetPlayerInfo(player).getPlayerGroupName(), 3)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', serverPrefix + messageCfg.getString("sethome.max-number")));
                    sqlSession.close();
                    return;
                }
                if (sqlSession.getMapper(PlayerHomeFunction.class).checkIsHaveHome(homeId, player.getUniqueId().toString()) >= 1){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', serverPrefix + messageCfg.getString("sethome.had")));
                    sqlSession.close();
                    return;
                }
            } catch (Exception exception) {
                sqlSession.rollback();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', serverPrefix + messageCfg.getString("SQL.fail") + exception.getMessage()));
                sqlSession.close();
                return;
            }
            PlayerHome playerHome = new PlayerHome();
            playerHome.setHomeId(homeId);
            playerHome.setHomeName(homeId);
            playerHome.setPlayerUUID(player.getUniqueId().toString());
            Location location = player.getLocation();
            Vector vector = location.getDirection();
            DecimalFormat decimal = new DecimalFormat("#.00");
            playerHome.setLocationX(Double.parseDouble(decimal.format(location.getX())));
            playerHome.setLocationY(Double.parseDouble(decimal.format(location.getY())));
            playerHome.setLocationZ(Double.parseDouble(decimal.format(location.getZ())));
            Material blockMaterial = location.getBlock().getRelative(BlockFace.DOWN).getType();
            if (!blockMaterial.isItem() || blockMaterial.isAir() || !blockMaterial.isBlock()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', serverPrefix + messageCfg.getString("sethome.not-item")));
                return;
            }
            playerHome.setMaterial(blockMaterial.name());
            playerHome.setWorld(player.getWorld().getName());
            playerHome.setWorldAlias(Main.mvWorldManager.getMVWorld(player.getWorld()).getAlias());
            playerHome.setVectorX(Double.parseDouble(decimal.format(vector.getX())));
            playerHome.setVectorY(Double.parseDouble(decimal.format(vector.getY())));
            playerHome.setVectorZ(Double.parseDouble(decimal.format(vector.getZ())));
            saveToDataBase(sqlSession,playerHome, player, homeId);
        });
    }

    @Override
    public void deleteHome(String homeId) {
        Main.mainPlugin.getServer().getScheduler().runTaskAsynchronously(Main.mainPlugin, () -> {
            SqlSession sqlSession = Main.SQL_SESSIONS.openSession(true);
            try {
                int checkNum =  sqlSession.getMapper(PlayerHomeFunction.class).deletePlayerHomeById(homeId, player.getUniqueId().toString());
                if (checkNum == 1) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', serverPrefix + messageCfg.getString("delete-home.success")));
                } else if (checkNum == 0) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', serverPrefix + messageCfg.getString("delete-home.no-have")));
                } else if (checkNum > 1) {
                    sqlSession.rollback();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', serverPrefix + messageCfg.getString("delete-home.error")));
                }
            } catch (Exception exception){
                sqlSession.rollback();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', serverPrefix + messageCfg.getString("SQL.fail") + exception.getCause().getMessage()));
            } finally {
                sqlSession.close();
            }
        });
    }

    @Override
    public boolean modifyHomeName(String newName) {
        PlayerHome playerHome = getEditorTempHomeOnRedis();
        if (playerHome == null) {
            this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', serverPrefix + messageCfg.getString("editor-home.timeout")));
            return false;
        }
        playerHome.setHomeName(newName);
        saveEditorTempHomeOnRedis(playerHome);
        return true;
    }

    @Override
    public boolean modifyHomeIcon(Material newMaterial) {
        PlayerHome playerHome = getEditorTempHomeOnRedis();
        if (playerHome == null) {
            this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', serverPrefix + messageCfg.getString("editor-home.timeout")));
            return false;
        }
        playerHome.setMaterial(newMaterial.name());
        saveEditorTempHomeOnRedis(playerHome);
        return true;
    }

    @Override
    public boolean modifyLocation(Location newLocation) {
        PlayerHome playerHome = getEditorTempHomeOnRedis();
        if (playerHome == null) {
            this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', serverPrefix + messageCfg.getString("editor-home.timeout")));
            return false;
        }
        DecimalFormat decimal = new DecimalFormat("#.00");
        Vector vectorNew = newLocation.getDirection();
        playerHome.setVectorX(Double.parseDouble(decimal.format(vectorNew.getX())));
        playerHome.setVectorY(Double.parseDouble(decimal.format(vectorNew.getY())));
        playerHome.setVectorZ(Double.parseDouble(decimal.format(vectorNew.getZ())));
        playerHome.setLocationX(Double.parseDouble(decimal.format(newLocation.getX())));
        playerHome.setLocationY(Double.parseDouble(decimal.format(newLocation.getY())));
        playerHome.setLocationZ(Double.parseDouble(decimal.format(newLocation.getZ())));
        saveEditorTempHomeOnRedis(playerHome);
        return true;
    }

    private void saveToDataBase(SqlSession sqlSession, PlayerHome playerHome, Player player, String homeId) {
        try {
            sqlSession.getMapper(PlayerHomeFunction.class).insert(playerHome);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',serverPrefix + messageCfg.getString("sethome.success") + homeId));
        } catch (Exception exception){
            sqlSession.rollback();
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', serverPrefix + messageCfg.getString("SQL.fail") + exception.getMessage()));
        } finally {
            sqlSession.close();
            new HomeOnRedisImpl(player.getUniqueId().toString()).deleteHomeOnRedis();
        }
    }
}
