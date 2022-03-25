package superarilo.main.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import superarilo.main.entity.PlayerHome;

import java.util.List;

@Mapper
public interface PlayerFunction {
    @MapKey(value = "uid")
    Integer whitelistCheckUser(@Param("playerName") String playerName, @Param("uuid")String uuid);
    List<PlayerHome> getPlayerHome(@Param("playerName") String playerName, @Param("uuid") String uuid);
}
