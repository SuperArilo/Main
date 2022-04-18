package superarilo.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import superarilo.main.entity.PlayerHome;

import java.util.List;

@Mapper
public interface PlayerHomeFunction extends BaseMapper<PlayerHome> {
    @MapKey("home_id")
    Integer checkIsHaveHome(@Param("home_id") String homeId, @Param("uuid") String playerUUID);
    Integer deletePlayerHomeById(@Param("home_id") String homeId, @Param("uuid") String playerUUID);
    Integer getHomeQuantity(@Param("uuid") String playerUUID);
    List<PlayerHome> getPlayerHome(@Param("uuid") String uuid);
}
