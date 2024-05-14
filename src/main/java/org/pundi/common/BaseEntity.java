package org.pundi.common;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月10日 16:27:00
 */
@Data
public class BaseEntity {

  /**
   * 乐观锁预留
   */
  @ApiModelProperty(hidden = true)
  @TableField("version")
  private Integer version;

  /**
   * 逻辑删除，冗余字段
   */
  @ApiModelProperty(hidden = true)
  @TableField("deleted")
  private Boolean deleted;

  @ApiModelProperty(hidden = true)
  @TableField("deleted_at")
  private LocalDateTime deletedAt;

  @ApiModelProperty(hidden = true)
  @TableField("updated_at")
  private LocalDateTime updatedAt;

  @ApiModelProperty(hidden = true)
  @TableField("created_at")
  private LocalDateTime createdAt;
}
