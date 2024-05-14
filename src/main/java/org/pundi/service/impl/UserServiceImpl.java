package org.pundi.service.impl;

import java.math.BigInteger;
import java.util.Objects;

import org.pundi.entity.UserEntity;
import org.pundi.mapper.UserMapper;
import org.pundi.service.UserService;
import org.pundi.util.ObjectMappingUtil;
import org.pundi.vo.UserVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * ⽤户表 服务实现类
 * </p>
 *
 * @author
 * @since 2024-05-10
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {


  @Value("${pundi.eth.tx-scan.default-block}")
  private BigInteger defaultScanBlock;

  @Override
  public UserVO saveEntity(UserEntity userEntity) {
    boolean bool = save(userEntity);
    if (bool) {
      return ObjectMappingUtil.map(userEntity, UserVO.class);
    }
    return null;
  }

  @Override
  public IPage<UserVO> userList(String name, Integer page, Integer pageSize) {

    LambdaQueryWrapper<UserEntity> wrapper = Wrappers.<UserEntity>lambdaQuery()
        .eq(Objects.nonNull(name), UserEntity::getName, name);
    IPage<UserEntity> pageResult = page(new Page<>(page, pageSize), wrapper);
    return pageResult.convert(e -> ObjectMappingUtil.map(e, UserVO.class));
  }
}
