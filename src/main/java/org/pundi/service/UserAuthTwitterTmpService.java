package org.pundi.service;

import org.pundi.entity.UserAuthTwitterTmpEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * <p>
 * twitter授权信息临时表 服务类
 * </p>
 *
 * @author 
 * @since 2024-06-17
 */
public interface UserAuthTwitterTmpService extends IService<UserAuthTwitterTmpEntity> {

	UserAuthTwitterTmpEntity findByOauthToken(String oauthToken);

	UserAuthTwitterTmpEntity findByAccessToken(String userToken);

	boolean updateAuthTokenAndSecretById(Integer id, String userToken, String userTokenSecret);

	List<UserAuthTwitterTmpEntity> findByUserTwitterIdAndStatus(String userId, int status);

	boolean updateActiveStatus(Set<Integer> ids, int status, int oldStatus);

}
