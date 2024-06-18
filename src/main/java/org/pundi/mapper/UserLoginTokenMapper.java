package org.pundi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.pundi.entity.UserLoginTokenEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户登陆token Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-06-17
 */
@Mapper
public interface UserLoginTokenMapper extends BaseMapper<UserLoginTokenEntity> {

}
