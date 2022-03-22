package superarilo.main.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PlayerFunction {
    @MapKey(value = "uid")
    Integer whitelistCheckUser(@Param("playerName") String playerName, @Param("uuid")String uuid);
}
