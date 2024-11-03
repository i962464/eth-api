package org.pundi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class RedisMessagePublisher {

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	private static final String HEARTBEAT_CHANNEL = "heartbeat";
	private static final String BUSINESS_CHANNEL = "task_reward";

	// 发布业务消息
	public void publishBusinessMessage(String message) {
		log.info(">>> Publishing business message to Redis channel: {}", BUSINESS_CHANNEL);
		redisTemplate.convertAndSend(BUSINESS_CHANNEL, message);
	}

	// 发布心跳消息
//	@Scheduled(fixedRate = 30000) // 每30秒发布一次心跳消息
//	public void sendHeartbeat() {
//		String heartbeatMessage = "HEARTBEAT";
//		log.info(">>> Sending heartbeat to Redis channel: {}", HEARTBEAT_CHANNEL);
//		redisTemplate.convertAndSend(HEARTBEAT_CHANNEL, heartbeatMessage);
//	}


//	@Scheduled(fixedRate = 30000) // 每30秒发布一次心跳消息
	public void sendHeartbeat() {
		String message = "{\"address\":\"0xc849bB4BF2E437f2B63338f4c0447f835d928946\",\"blockHeight\":10000,\"content\":\"我是个json\"}";

		log.info(">>> Publishing business message to Redis channel: {}", BUSINESS_CHANNEL);
		redisTemplate.convertAndSend(BUSINESS_CHANNEL, message);
	}
}
