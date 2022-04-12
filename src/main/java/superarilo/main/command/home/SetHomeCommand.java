package superarilo.main.command.home;

import org.apache.ibatis.session.SqlSession;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import superarilo.main.Main;
import superarilo.main.entity.PlayerHome;
import superarilo.main.function.FileConfigs;
import superarilo.main.mapper.PlayerHomeFunction;

import java.text.DecimalFormat;

public class SetHomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player){
            if (!s.equals("sethome")) return false;
            if (strings.length == 1){
                String playerUUID = ((Player) commandSender).getUniqueId().toString();
                PlayerHome playerHome = new PlayerHome();
                playerHome.setHomeId(strings[0]);
                playerHome.setHomeName(strings[0]);
                playerHome.setPlayerUUID(playerUUID);
                Location location = ((Player) commandSender).getLocation();
                Vector vector = location.getDirection();
                DecimalFormat decimal = new DecimalFormat("#.00");
                playerHome.setLocationX(Double.parseDouble(decimal.format(location.getX())));
                playerHome.setLocationY(Double.parseDouble(decimal.format(location.getY())));
                playerHome.setLocationZ(Double.parseDouble(decimal.format(location.getZ())));
                playerHome.setMaterial(location.getBlock().getRelative(BlockFace.DOWN).getType().name());
                playerHome.setWorld(((Player) commandSender).getWorld().getName());
                playerHome.setWorldAlias(Main.mvWorldManager.getMVWorld(((Player) commandSender).getWorld()).getAlias());
                playerHome.setVectorX(Double.parseDouble(decimal.format(vector.getX())));
                playerHome.setVectorY(Double.parseDouble(decimal.format(vector.getY())));
                playerHome.setVectorZ(Double.parseDouble(decimal.format(vector.getZ())));
                SqlSession sqlSession = Main.SQL_SESSIONS.openSession(true);
                try {
                    if (sqlSession.getMapper(PlayerHomeFunction.class).checkIsHaveHome(strings[0], playerUUID) < 1){
                        sqlSession.getMapper(PlayerHomeFunction.class).insert(playerHome);
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&',Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("sethome.success") + strings[0]));
                        Main.redisValue.del(((Player) commandSender).getUniqueId() + "_home");
                    } else {
                        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("sethome.had")));
                    }
                } catch (Exception exception){
                    sqlSession.rollback();
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("SQL.fail") + exception));
                } finally {
                    sqlSession.close();
                }
                return true;
            } else {
                command.setUsage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("commands").getString("sethome.usage")));
                return false;
            }
        } else {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.mainPlugin.getConfig().getString("prefix") + FileConfigs.fileConfigs.get("message").getString("sethome.not-player")));
            return true;
        }
    }
}
