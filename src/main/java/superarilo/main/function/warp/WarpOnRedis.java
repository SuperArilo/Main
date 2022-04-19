package superarilo.main.function.warp;

import superarilo.main.entity.PlayerHome;
import superarilo.main.entity.Warp;

import java.util.List;

public interface WarpOnRedis {
    void deleteWarpOnRedis();
    List<Warp> getWarpTempOnRedis();
    Warp getEditorTempOnRedis();
    void saveEditorTempWarpOnRedis(Warp warp);
    void setNowEditorWarpPlayer();
    void removeNowEditorWarpPlayer();
}
