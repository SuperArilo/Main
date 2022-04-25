package superarilo.main.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import superarilo.main.entity.Warp;

import java.util.List;

public interface WarpFunction extends BaseMapper<Warp> {
    @MapKey(value = "id")
    Integer checkIsHaveId(@Param("warpId") String warpId);
    Integer getWarpQuantity(@Param("playerUUID") String playerUUID);
    List<Warp> getWarpList(@Param("pageNumber")Integer pageNumber, @Param("pageSize")Integer pageSize, @Param("warpClass") String warpClass);
}
