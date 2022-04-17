package superarilo.main.function.home.Impl;

import com.alibaba.fastjson.JSONObject;
import superarilo.main.Main;
import superarilo.main.entity.PlayerHome;
import superarilo.main.function.home.HomeOnRedis;

import java.util.List;

public class HomeOnRedisImpl implements HomeOnRedis {

    private final String playerUUID;

    public HomeOnRedisImpl(String playerUUID) {
        this.playerUUID = playerUUID;
    }

    @Override
    public void deleteHomeOnRedis() {

        Main.redisValue.del(this.playerUUID + "_home");
    }


    @Override
    public List<PlayerHome> getHomeTempOnRedis() {
        return JSONObject.parseArray(this.playerUUID + "_home", PlayerHome.class);
    }

    @Override
    public PlayerHome getEditorTempHomeOnRedis() {
        String playerHomeJson = Main.redisValue.get(this.playerUUID + "_editor_home");
        return playerHomeJson != null ? JSONObject.parseObject(playerHomeJson, PlayerHome.class) : null;
    }

    @Override
    public  void saveEditorTempHomeOnRedis(PlayerHome playerHome) {
        Main.redisValue.setex(this.playerUUID + "_editor_home", 10, JSONObject.toJSONString(playerHome));
    }

    @Override
    public void setNowEditorHomePlayer() {
        Main.redisValue.sadd("editor_home_player_now", playerUUID);
    }

    @Override
    public void removeNowEditorHomePlayer() {
        Main.redisValue.srem("editor_home_player_now", playerUUID);
    }
}
