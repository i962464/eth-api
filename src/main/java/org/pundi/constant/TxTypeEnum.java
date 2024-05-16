package org.pundi.constant;

import lombok.Getter;

/**
 * @author ekko
 * @version 1.0.0
 * @Description 交易类型
 * @createTime 2024年05月15日 17:24:00
 */
@Getter
public enum TxTypeEnum {

  DEPOSIT(1, "充币"),

  WITHDRAW(2, "提币");

  private Integer code;
  private String value;

  TxTypeEnum(Integer code, String value) {

    this.code = code;
    this.value = value;
  }

  public static TxTypeEnum getByCode(int code) {

    for (TxTypeEnum e : TxTypeEnum.values()) {
      if (e.code == code) {
        return e;
      }
    }
    throw new RuntimeException("不存在的交易类型-" + code);
  }
}
