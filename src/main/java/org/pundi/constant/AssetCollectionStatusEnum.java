package org.pundi.constant;

import lombok.Getter;

/**
 * @author ekko
 * @version 1.0.0
 * @Description 资产归集状态
 * @createTime 2024年05月15日 17:27:00
 */
@Getter
public enum AssetCollectionStatusEnum {


  /**
   * uncollected
   */
  UNCOLLECTED(0, "未归集"),
  /**
   * collecting
   */
  COLLECTING(1, "归集中"),
  /**
   * collected
   */
  COLLECTED(2, "归集完成"),
  ;

  private Integer code;
  private String value;

  AssetCollectionStatusEnum(Integer code, String value) {

    this.code = code;
    this.value = value;
  }

  public static AssetCollectionStatusEnum getByCode(int code) {

    for (AssetCollectionStatusEnum e : AssetCollectionStatusEnum.values()) {
      if (e.code == code) {
        return e;
      }
    }
    throw new RuntimeException("不存在的归集状态-" + code);
  }
}
