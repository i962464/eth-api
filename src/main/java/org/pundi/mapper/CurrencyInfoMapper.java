package org.pundi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.pundi.entity.CurrencyInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 网络-币种映射表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-05-11
 */
@Mapper
public interface CurrencyInfoMapper extends BaseMapper<CurrencyInfoEntity> {

}
