package superarilo.main.function.home;

import superarilo.main.entity.PlayerHome;

import java.util.List;

public interface HomeOnRedis {
    void deleteHomeOnRedis();
    List<PlayerHome> getHomeTempOnRedis();
    PlayerHome getEditorTempHomeOnRedis();
    void saveEditorTempHomeOnRedis(PlayerHome playerHome);
    void setNowEditorHomePlayer();
    void removeNowEditorHomePlayer();
}
