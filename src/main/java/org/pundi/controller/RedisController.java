//package org.pundi.controller;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//
///**
// * @author ekko
// * @version 1.0.0
// * @Description
// * @createTime 2024年10月11日 15:28:00
// */
//@RestController
//@RequestMapping("/api/redis")
//public class RedisController {
//
//	@Resource
//	private RedisMessagePublisher redisMessagePublisher;
//
//	@GetMapping("/publish")
//	public ResponseEntity<String> publishMessage() {
//		String message = "{\"address\":\"0x00003\",\"blockHeight\":10000,\"content\":\"我是个json\"}";
//		redisMessagePublisher.publishBusinessMessage(message);
//		return ResponseEntity.ok("Message published");
//	}
//}
