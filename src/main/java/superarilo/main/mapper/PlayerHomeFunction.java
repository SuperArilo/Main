package superarilo.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import superarilo.main.entity.PlayerHome;

@Mapper
public interface PlayerHomeFunction extends BaseMapper<PlayerHome> {
    @MapKey("home_id")
    Integer checkIsHaveHome(@Param("home_id") String homeId, @Param("uuid") String playerUUID);
    Integer deletePlayerHomeById(@Param("home_id") String homeId, @Param("uuid") String playerUUID);
}
