package org.pundi.entity;

import org.pundi.common.BaseEntity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * <p>
 * ⽤户表
 * </p>
 *
 * @author
 * @since 2024-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user")
@ToString
@Builder
public class UserEntity extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(name = "用户id")
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;

  /**
   * ⽤户名
   */
  @ApiModelProperty(name = "用户name")
  @TableField("name")
  private String name;

}
