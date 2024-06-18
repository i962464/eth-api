package org.pundi.common;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月10日 16:11:00
 */
@Data
@Builder
public class ResultCode implements Serializable {


  private static final long serialVersionUID = -6269841958947880397L;

  /**
   * 状态码
   */
  private int code;

  /**
   * 状态信息
   */
  private String msg;

  /**
   * 默认成功
   */
  public final static ResultCode SUCCESS = dispose(ResultCodeEnum.SUCCESS);
  /**
   * 默认失败
   */
  public final static ResultCode ERROR = dispose(ResultCodeEnum.ERROR);
  /**
   * 通用业务异常
   */
  public final static ResultCode BIZ_ERROR = dispose(ResultCodeEnum.BIZ_ERROR);
  /**
   * 文件超出最大限制
   */
  public final static ResultCode FILE_OUT_MAX = dispose(ResultCodeEnum.FILE_OUT_MAX);
  /**
   * 文件格式不正确
   */
  public final static ResultCode FILE_FORMAT_ERROR = dispose(ResultCodeEnum.FILE_FORMAT_ERROR);
  /**
   * 参数错误
   */
  public final static ResultCode PARAM_ERROR = dispose(ResultCodeEnum.PARAM_ERROR);
  /**
   * Json解析异常
   */
  public final static ResultCode JSON_FORMAT_ERROR = dispose(ResultCodeEnum.JSON_FORMAT_ERROR);
  /**
   * Sql解析异常
   */
  public final static ResultCode SQL_ERROR = dispose(ResultCodeEnum.SQL_ERROR);
  /**
   * 网络超时
   */
  public final static ResultCode NETWORK_TIMEOUT = dispose(ResultCodeEnum.NETWORK_TIMEOUT);
  /**
   * 未知的接口
   */
  public final static ResultCode UNKNOWN_INTERFACE = dispose(ResultCodeEnum.UNKNOWN_INTERFACE);
  /**
   * 请求方式不支持
   */
  public final static ResultCode REQ_MODE_NOT_SUPPORTED = dispose(ResultCodeEnum.REQ_MODE_NOT_SUPPORTED);


  /**
   * 用户不存在
   */
  public final static ResultCode USER_NOT_FOUND = dispose(ResultCodeEnum.USER_NOT_FOUND);

  /**
   * token 不存在
   */
  public final static ResultCode CURRENCY_NOT_FOUND = dispose(ResultCodeEnum.CURRENCY_NOT_FOUND);

  /**
   * 地址已存在
   */
  public final static ResultCode ADDRESS_IS_EXIST = dispose(ResultCodeEnum.ADDRESS_IS_EXIST);

  /**
   * 地址不存在
   */
  public final static ResultCode ADDRESS_NOT_EXIST = dispose(ResultCodeEnum.ADDRESS_NOT_EXIST);

  /**
   * 地址格式错误
   */
  public final static ResultCode ADDRESS_VALID_ERROR = dispose(ResultCodeEnum.ADDRESS_VALID_ERROR);


  /**
   * 推特相关
   */
  public final static ResultCode OAUTH_PREPARE_EXCEPTION = dispose(ResultCodeEnum.OAUTH_PREPARE_EXCEPTION);
  public final static ResultCode OAUTH_GRANT_EXCEPTION = dispose(ResultCodeEnum.OAUTH_GRANT_EXCEPTION);
  public final static ResultCode DATABASE_ERROR = dispose(ResultCodeEnum.DATABASE_ERROR);




  /**
   * 系统异常
   */
  public final static ResultCode SYS_ERROR = dispose(ResultCodeEnum.SYS_ERROR);

  private static ResultCode dispose(ResultCodeEnum codeEnum) {
    return ResultCode.builder().code(codeEnum.getCode()).msg(codeEnum.getMsg()).build();
  }

  public ResultCode(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  @Getter
  public enum ResultCodeEnum {

    SUCCESS(0, "操作成功"),
    ERROR(1, "操作失败"),
    BIZ_ERROR(1000, "通用业务异常"),
    FILE_OUT_MAX(9000, "文件超出最大限制"),
    FILE_FORMAT_ERROR(9001, "文件格式不正确"),
    PARAM_ERROR(9050, "参数错误"),
    JSON_FORMAT_ERROR(9051, "Json解析异常"),
    SQL_ERROR(9052, "Sql解析异常"),
    NETWORK_TIMEOUT(9510, "网络超时"),
    UNKNOWN_INTERFACE(9520, "未知的接口"),
    REQ_MODE_NOT_SUPPORTED(9530, "请求方式不支持"),

    CURRENCY_NOT_FOUND(5000, "币种不存在"),
    ADDRESS_IS_EXIST(5001, "地址已存在"),
    ADDRESS_NOT_EXIST(5002, "地址不存在"),
    ADDRESS_VALID_ERROR(5003, "地址格式错误"),
    USER_NOT_FOUND(5004, "用户不存在"),

    //twitter 相关
    OAUTH_PREPARE_EXCEPTION(60001, "Could not retrieve permission url"),
    OAUTH_GRANT_EXCEPTION(60002, "Could not retrieve user access token"),
    DATABASE_ERROR(60003, "Database exception"),

    SYS_ERROR(9999, "系统异常");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 状态信息
     */
    private final String msg;

    public int getCode() {
      return code;
    }

    public String getMsg() {
      return msg;
    }

    ResultCodeEnum(int code, String msg) {
      this.code = code;
      this.msg = msg;
    }
  }
}