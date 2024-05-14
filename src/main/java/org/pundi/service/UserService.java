package org.pundi.service;

import org.pundi.entity.UserEntity;
import org.pundi.vo.UserVO;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * ⽤户表 服务类
 * </p>
 *
 * @author
 * @since 2024-05-10
 */
public interface UserService extends IService<UserEntity> {

  /**
   * 创建用户
   * @param userEntity
   * @return
   */
  UserVO saveEntity(UserEntity userEntity);

  /**
   * 用户分页查询
   * @param name
   * @param page
   * @param pageSize
   * @return
   */
  IPage<UserVO> userList(String name, Integer page, Integer pageSize);
}
