package superarilo.main.function.warp;

public interface WarpManager extends WarpOnRedis {
    void createNewWarp(String homeId);
}
