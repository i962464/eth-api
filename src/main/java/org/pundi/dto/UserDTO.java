package org.pundi.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月10日 17:22:00
 */
@Data
public class UserDTO {

  @NotBlank(message = "参数不能为空")
  private String name;
}
