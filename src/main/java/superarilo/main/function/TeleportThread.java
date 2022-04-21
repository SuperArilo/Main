package superarilo.main.function;

import com.alibaba.fastjson.JSONObject;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import superarilo.main.Main;

public class TeleportThread {

    private final Type type;
    private final Player player;
    private Player targetPlayer;
    private final Location initialLocation;
    private Location targetLocation;
    private final double initialHealth;

    public Type getType() {
        return type;
    }

    public enum Type {
        POINT,
        PLAYER,
        BACK,
        RANDOM
    }

    public TeleportThread(Player player, Location targetLocation, Type type){
        this.player = player;
        this.type = type;
        this.targetLocation = targetLocation;
        this.initialLocation = player.getLocation();
        this.initialHealth = player.getHealth();
    }

    public TeleportThread(Player player, Player targetPlayer, Type type){
        this.player = player;
        this.targetPlayer = targetPlayer;
        this.type = type;
        this.initialLocation = player.getLocation();
        this.initialHealth = player.getHealth();
    }

    public TeleportThread(Player player, Type type){
        this.player = player;
        this.type = type;
        this.initialLocation = player.getLocation();
        this.initialHealth = player.getHealth();
    }

    public void teleport() {
        final long[] timerIndex = {this.player.isOp() ? 1 : FileConfigs.fileConfigs.get("home").getLong("delay", 1) + 1};
        this.player.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("teleport.teleporting")));
        new BukkitRunnable() {
            @Override
            public void run() {
                Player threadPlayer = Main.mainPlugin.getServer().getPlayer(player.getUniqueId());
                if (threadPlayer == null) {
                    cancel();
                    return;
                }
                if (hasMoved(threadPlayer) || hasLostHealth(threadPlayer)){
                    cancel();
                    threadPlayer.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("teleport.break")));
                    return;
                }
                timerIndex[0] = timerIndex[0] - 1;
                if (timerIndex[0] == 0){
                    cancel();
                    String keyName = threadPlayer.getUniqueId() + "_back";
                    switch (getType()){
                        case POINT: {
                            threadPlayer.teleportAsync(targetLocation);
                            threadPlayer.playEffect(targetLocation, Effect.CLICK1, null);
                            setPlayerBackLocation(initialLocation, keyName);
                            threadPlayer.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("teleport.success")));
                        }
                        break;
                        case BACK: {
                            JSONObject jsonObject = getPlayerBackLocation(keyName);
                            Location backLocation = new Location(Main.mainPlugin.getServer().getWorld(jsonObject.getString("world")), jsonObject.getFloat("x"), jsonObject.getFloat("y"), jsonObject.getFloat("z"));
                            threadPlayer.teleportAsync(backLocation);
                            threadPlayer.playEffect(backLocation, Effect.CLICK1, null);
                            setPlayerBackLocation(initialLocation,keyName);
                        }
                        break;
                        case PLAYER: {
                            threadPlayer.teleport(targetPlayer);
                            threadPlayer.sendMessage(FunctionTool.createServerSendMessage(FileConfigs.fileConfigs.get("message").getString("teleport.success")));
                        }
                        break;
                    }
                }
            }
        }.runTaskTimer(Main.mainPlugin, 0, 20L);
    }

    private double makePositive(double d) {
        if (d < 0) {
            d = d * -1D;
        }
        return d;
    }

    public boolean hasLostHealth(Player p) {
        return p.getHealth() < initialHealth;
    }

    public boolean hasMoved(Player p) {
        Location currentLocation = p.getLocation();
        return makePositive(initialLocation.getX() - currentLocation.getX()) + makePositive(initialLocation.getY() - currentLocation.getY()) + makePositive(initialLocation.getZ() - currentLocation.getZ()) > 0.1;
    }
    public static void setPlayerBackLocation(Location location, String keyName){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("world", location.getWorld().getName());
        jsonObject.put("x", location.getX());
        jsonObject.put("y", location.getY());
        jsonObject.put("z", location.getZ());
        Main.redisValue.setex(keyName, 43200, jsonObject.toJSONString());
    }

    public JSONObject getPlayerBackLocation(String keyName){
        return JSONObject.parseObject(Main.redisValue.get(keyName));
    }
}
