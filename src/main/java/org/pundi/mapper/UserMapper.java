package org.pundi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.pundi.entity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * ⽤户表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-05-10
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

}
