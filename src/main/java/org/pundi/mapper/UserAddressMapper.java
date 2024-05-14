package org.pundi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.pundi.entity.UserAddressEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 钱包充值地址表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-05-11
 */
@Mapper
public interface UserAddressMapper extends BaseMapper<UserAddressEntity> {

}
