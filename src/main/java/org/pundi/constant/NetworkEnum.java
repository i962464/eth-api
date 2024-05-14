package org.pundi.constant;

import lombok.Getter;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月13日 11:00:00
 */
@Getter
public enum NetworkEnum {

  // 以太坊
  ETH(60, "ETH"),

  OTHER(1, "other");

  private Integer code;
  private String value;

  NetworkEnum(Integer code, String value) {

    this.code = code;
    this.value = value;
  }

  public static NetworkEnum getByCode(int code) {

    for (NetworkEnum e : NetworkEnum.values()) {
      if (e.code == code) {
        return e;
      }
    }
    throw new RuntimeException("不存的网络类型-" + code);
  }
}
