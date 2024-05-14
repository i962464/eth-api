package org.pundi.constant;

import lombok.Getter;

/**
 * @author ekko
 * @version 1.0.0
 * @Description 多种钱包类型，需要具体策略实现
 * @createTime 2024年05月11日 10:00:00
 */
@Getter
public enum WalletTypeEnum {


  // 自有钱包
  SELF(0, "self"),

  // todo 支持多种钱包类型，需要策略实现
  OTHER(1, "other");

  private Integer code;
  private String value;

  WalletTypeEnum(Integer code, String value) {

    this.code = code;
    this.value = value;
  }

  public static WalletTypeEnum getByCode(int code) {

    for (WalletTypeEnum e : WalletTypeEnum.values()) {
      if (e.code == code) {
        return e;
      }
    }
    throw new RuntimeException("不存的钱包类型-" + code);
  }
}
