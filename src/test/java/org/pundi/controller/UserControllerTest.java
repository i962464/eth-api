package org.pundi.controller;

import org.junit.jupiter.api.Test;
import org.pundi.dto.UserDTO;
import org.pundi.service.UserService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月10日 14:52:00
 */
@SpringBootTest
class UserControllerTest {

	@Resource
	private UserController userController;

	@Resource
	private UserService userService; // 替换UserService为你的服务类名

	@Test
	void update() throws ExecutionException, InterruptedException {
		// 创建一个线程池来模拟并发
		ExecutorService executorService = Executors.newFixedThreadPool(2);

		// 创建两个并发任务
		Callable<Boolean> task1 = () -> {
			// 开始一个事务并调用 for update 的方法
			return updateUserWithForUpdate(7, "Name from Task 1");
		};

		Callable<Boolean> task2 = () -> {
			// 开始一个事务并调用 for update 的方法
			return updateUserWithForUpdate(7, "Name from Task 2");
		};

		// 提交任务
		Future<Boolean> result1 = executorService.submit(task1);
		Future<Boolean> result2 = executorService.submit(task2);

		// 获取结果并验证
		assertTrue(result1.get());
		assertTrue(result2.get());

		// 关闭线程池
		executorService.shutdown();
	}

	@Transactional
	public boolean updateUserWithForUpdate(Integer userId, String newName) {
		// 创建一个 UserDTO 对象进行更新
		UserDTO userDTO = new UserDTO();
		userDTO.setId(userId);
		userDTO.setName(newName);

		// 调用服务中的 updateForUpdate 方法
		return userService.updateForUpdate(userDTO);
	}
}