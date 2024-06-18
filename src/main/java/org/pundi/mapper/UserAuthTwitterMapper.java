package org.pundi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.pundi.entity.UserAuthTwitterEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户twitter授权信息 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-06-17
 */
@Mapper
public interface UserAuthTwitterMapper extends BaseMapper<UserAuthTwitterEntity> {

}
