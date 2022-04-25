package superarilo.main.function.warp.Impl;

import org.apache.ibatis.session.SqlSession;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import superarilo.main.Main;
import superarilo.main.entity.Warp;
import superarilo.main.function.FileConfigs;
import superarilo.main.function.GetPlayerInfo;
import superarilo.main.function.warp.WarpManager;
import superarilo.main.mapper.WarpFunction;
import java.text.DecimalFormat;

public class WarpManagerImpl extends WarpOnRedisImpl implements WarpManager {

    private final Player player;
    private final String serverPrefix = Main.mainPlugin.getConfig().getString("prefix");
    private final FileConfiguration messageCfg = FileConfigs.fileConfigs.get("message");

    public WarpManagerImpl(Player player) {
        super(player.getUniqueId().toString());
        this.player = player;
    }

    @Override
    public void createNewWarp(String warpId) {
        Main.mainPlugin.getServer().getScheduler().runTaskAsynchronously(Main.mainPlugin, () -> {
            SqlSession sqlSession = Main.SQL_SESSIONS.openSession(true);
            try {
                if (sqlSession.getMapper(WarpFunction.class).getWarpQuantity(player.getUniqueId().toString()) >= FileConfigs.fileConfigs.get("warp").getInt("warp-group." + new GetPlayerInfo(player).getPlayerGroupName(), 3)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', serverPrefix + messageCfg.getString("setwarp.max-number")));
                    sqlSession.close();
                    return;
                }
                if (sqlSession.getMapper(WarpFunction.class).checkIsHaveId(warpId) >= 1){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', serverPrefix + messageCfg.getString("setwarp.had")));
                    sqlSession.close();
                    return;
                }
            } catch (Exception exception) {
                sqlSession.rollback();
                exception.printStackTrace();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', serverPrefix + messageCfg.getString("SQL.fail") + exception.getMessage()));
                sqlSession.close();
                return;
            }
            Warp warp = new Warp();
            warp.setWarpId(warpId);
            warp.setWarpName(warpId);
            warp.setPlayerUUID(player.getUniqueId().toString());
            Location location = player.getLocation();
            Vector vector = location.getDirection();
            Material blockMaterial = location.getBlock().getRelative(BlockFace.DOWN).getType();
            if (!blockMaterial.isItem() || blockMaterial.isAir() || !blockMaterial.isBlock()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', serverPrefix + messageCfg.getString("setwarp.not-item")));
                return;
            }
            warp.setMaterial(blockMaterial.name());
            DecimalFormat decimal = new DecimalFormat("#.00");
            warp.setLocationX(Double.parseDouble(decimal.format(location.getX())));
            warp.setLocationY(Double.parseDouble(decimal.format(location.getY())));
            warp.setLocationZ(Double.parseDouble(decimal.format(location.getZ())));
            warp.setVectorX(Double.parseDouble(decimal.format(vector.getX())));
            warp.setVectorY(Double.parseDouble(decimal.format(vector.getY())));
            warp.setVectorZ(Double.parseDouble(decimal.format(vector.getZ())));
            warp.setWorld(location.getWorld().getName());
            warp.setWorldAlias(Main.mvWorldManager.getMVWorld(location.getWorld()).getAlias());
            saveToDataBase(sqlSession, warp, player, warpId);
        });
    }
    private void saveToDataBase(SqlSession sqlSession, Warp warp, Player player, String warpId){
        try {
            sqlSession.getMapper(WarpFunction.class).insert(warp);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',serverPrefix + messageCfg.getString("setwarp.success") + warpId));
        } catch (Exception exception) {
            sqlSession.rollback();
            exception.printStackTrace();
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', serverPrefix + messageCfg.getString("SQL.fail") + exception.getMessage()));
        } finally {
            sqlSession.close();
            deleteWarpOnRedis();
        }
    }

}
