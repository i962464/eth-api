package org.pundi.controller;


import javax.validation.Valid;

import org.pundi.common.Result;
import org.pundi.dto.UserDTO;
import org.pundi.entity.UserEntity;
import org.pundi.service.UserService;
import org.pundi.vo.UserVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月11日 09:08:00
 */
@Api(tags = "用户接口", value = "提供用户相关的 Rest API")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

  private final UserService userService;


  @ApiOperation(value = "创建用户")
  @PostMapping("/create")
  public Result<UserVO> createUser(@Valid @RequestBody UserDTO dto) {

    String name = dto.getName();
    UserEntity userEntity = UserEntity.builder().name(name).build();
    return Result.success(userService.saveEntity(userEntity));
  }

  @ApiOperation(value = "用户列表")
  @GetMapping("/list")
  Result<IPage<UserVO>> userList(@RequestParam(required = false) String name,
                                 @RequestParam(value = "page", defaultValue = "1") Integer page,
                                 @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {
    return Result.success(userService.userList(name, page, pageSize));
  }


}

