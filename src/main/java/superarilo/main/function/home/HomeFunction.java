package superarilo.main.function.home;

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
import superarilo.main.mapper.PlayerHomeFunction;
import java.text.DecimalFormat;

public class HomeFunction {

    private final Player player;
    private final String homeId;
    private PlayerHome playerHome = null;
    private final FileConfiguration messageCfg = FileConfigs.fileConfigs.get("message");
    private final SqlSession sqlSession = Main.SQL_SESSIONS.openSession(true);

    public HomeFunction (Player player, String homeId){
        this.player = player;
        this.homeId = homeId;
    }

    public void setNewHome(){
        try {
            if (this.sqlSession.getMapper(PlayerHomeFunction.class).getHomeQuantity(this.player.getUniqueId().toString()) >= FileConfigs.fileConfigs.get("home").getInt("homes-group." + new GetPlayerInfo(this.player).getPlayerGroupName(), 3)) {
                this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + messageCfg.getString("sethome.max-number")));
                sqlSession.close();
                return;
            }
            if (this.sqlSession.getMapper(PlayerHomeFunction.class).checkIsHaveHome(this.homeId, this.player.getUniqueId().toString()) >= 1){
                this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + messageCfg.getString("sethome.had")));
                sqlSession.close();
                return;
            }
        } catch (Exception exception) {
            this.sqlSession.rollback();
            this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + messageCfg.getString("SQL.fail") + exception.getMessage()));
            sqlSession.close();
        }
        String playerUUID = this.player.getUniqueId().toString();
        PlayerHome playerHome = new PlayerHome();
        playerHome.setHomeId(this.homeId);
        playerHome.setHomeName(this.homeId);
        playerHome.setPlayerUUID(playerUUID);
        Location location = this.player.getLocation();
        Vector vector = location.getDirection();
        DecimalFormat decimal = new DecimalFormat("#.00");
        playerHome.setLocationX(Double.parseDouble(decimal.format(location.getX())));
        playerHome.setLocationY(Double.parseDouble(decimal.format(location.getY())));
        playerHome.setLocationZ(Double.parseDouble(decimal.format(location.getZ())));
        Material blockMaterial = location.getBlock().getRelative(BlockFace.DOWN).getType();
        if (!blockMaterial.isItem() || blockMaterial.isAir() || !blockMaterial.isBlock()) {
            this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + messageCfg.getString("sethome.not-item")));
            return;
        }
        playerHome.setMaterial(blockMaterial.name());
        playerHome.setWorld(this.player.getWorld().getName());
        playerHome.setWorldAlias(Main.mvWorldManager.getMVWorld(this.player.getWorld()).getAlias());
        playerHome.setVectorX(Double.parseDouble(decimal.format(vector.getX())));
        playerHome.setVectorY(Double.parseDouble(decimal.format(vector.getY())));
        playerHome.setVectorZ(Double.parseDouble(decimal.format(vector.getZ())));
        setPlayerHome(playerHome);
        saveToDataBase();
    }

    public void deleteHome(){
        try {
            int checkNum =  sqlSession.getMapper(PlayerHomeFunction.class).deletePlayerHomeById(this.homeId, this.player.getUniqueId().toString());
            if (checkNum == 1) {
                this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + messageCfg.getString("delete-home.success")));
            } else if (checkNum == 0) {
                this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + messageCfg.getString("delete-home.no-have")));
            } else if (checkNum > 1) {
                sqlSession.rollback();
                this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + messageCfg.getString("delete-home.error")));
            }

        } catch (Exception exception){
            this.sqlSession.rollback();
            this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + messageCfg.getString("SQL.fail") + exception.getCause().getMessage()));
        } finally {
            this.sqlSession.close();
        }
        deleteHomeOnRedis(this.player);
    }

    private void saveToDataBase(){
        Main.mainPlugin.getServer().getScheduler().runTaskAsynchronously(Main.mainPlugin, () -> {
            try {
                this.sqlSession.getMapper(PlayerHomeFunction.class).insert(playerHome);
                this.player.sendMessage(ChatColor.translateAlternateColorCodes('&',Main.mainPlugin.getConfig().getString("prefix") + messageCfg.getString("sethome.success") + this.homeId));
            } catch (Exception exception){
                this.sqlSession.rollback();
                this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + messageCfg.getString("SQL.fail") + exception.getMessage()));
            } finally {
                this.sqlSession.close();
            }
            deleteHomeOnRedis(this.player);
        });
    }

    private void setPlayerHome(PlayerHome playerHome) {
        this.playerHome = playerHome;
    }

    private void deleteHomeOnRedis(Player player){
        Main.redisValue.del(player.getUniqueId() + "_home");
    }
}
