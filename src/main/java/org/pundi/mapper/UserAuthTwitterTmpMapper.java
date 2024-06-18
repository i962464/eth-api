package org.pundi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.pundi.entity.UserAuthTwitterTmpEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * twitter授权信息临时表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-06-17
 */
@Mapper
public interface UserAuthTwitterTmpMapper extends BaseMapper<UserAuthTwitterTmpEntity> {

}
