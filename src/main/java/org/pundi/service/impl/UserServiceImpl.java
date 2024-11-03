package org.pundi.service.impl;

import java.math.BigInteger;
import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.pundi.dto.UserDTO;
import org.pundi.entity.UserEntity;
import org.pundi.mapper.UserMapper;
import org.pundi.service.UserService;
import org.pundi.util.ObjectMappingUtil;
import org.pundi.vo.UserVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

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

  @Override
  @Transactional
  public boolean updateForUpdate(UserDTO user) {

//    LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getId, user.getId()).last("for update");
//    UserEntity one = getOne(queryWrapper);
//
//    LambdaUpdateWrapper<UserEntity> wrapper = new LambdaUpdateWrapper<>();
//    wrapper.eq(UserEntity::getId, one.getId())
//        .set(UserEntity::getName, user.getName());
//    return update(wrapper);


    // 记录开始时间
    System.out.println("Thread " + Thread.currentThread().getName() + " starts updateForUpdate at " + System.currentTimeMillis());

    // 查询并锁定行
    LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<UserEntity>()
        .eq(UserEntity::getId, user.getId())
        .last("for update");
    UserEntity one = getOne(queryWrapper);

    // 模拟长时间持有锁
    try {
      Thread.sleep(5000); // 延迟 5 秒
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // 更新操作
    LambdaUpdateWrapper<UserEntity> wrapper = new LambdaUpdateWrapper<>();
    wrapper.eq(UserEntity::getId, one.getId())
        .set(UserEntity::getName, user.getName());
    boolean result = update(wrapper);

    // 记录结束时间
    System.out.println("Thread " + Thread.currentThread().getName() + " ends updateForUpdate at " + System.currentTimeMillis());

    return result;
  }
}
