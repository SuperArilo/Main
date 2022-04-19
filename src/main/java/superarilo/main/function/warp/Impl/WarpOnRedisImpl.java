package superarilo.main.function.warp.Impl;

import superarilo.main.Main;
import superarilo.main.entity.Warp;
import superarilo.main.function.warp.WarpOnRedis;

import java.util.List;

public class WarpOnRedisImpl implements WarpOnRedis {

    private final String playerUUID;

    public WarpOnRedisImpl(String playerUUID) {
        this.playerUUID = playerUUID;
    }

    @Override
    public void deleteWarpOnRedis() {
        Main.redisValue.del(this.playerUUID + "_warp");
    }

    @Override
    public List<Warp> getWarpTempOnRedis() {
        return null;
    }

    @Override
    public Warp getEditorTempOnRedis() {
        return null;
    }

    @Override
    public void saveEditorTempWarpOnRedis(Warp warp) {

    }

    @Override
    public void setNowEditorWarpPlayer() {

    }

    @Override
    public void removeNowEditorWarpPlayer() {

    }
}
