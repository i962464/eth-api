package org.pundi.exception;


import org.pundi.common.ResultCode;

/**
 * 基础的业务异常
 *
 * @author ekko
 */
public class BusinessException extends Exception {

  private int code;

  public BusinessException() {
    super();
    this.code = 0;
  }

  public BusinessException(String message) {
    super(message);
    this.code = 0;
  }

  public BusinessException(int code) {
    super();
    this.code = code;
  }

  /**
   * @param code    异常标识码
   * @param message 异常说明
   */
  public BusinessException(int code, String message) {
    super(message);
    this.code = code;
  }

  public BusinessException(String message, Throwable e) {
    super(message, e);
  }

  /**
   * @param code    异常标识码
   * @param message 异常说明
   * @param e       原始异常
   */
  public BusinessException(int code, String message, Throwable e) {
    super(message, e);
    this.code = code;
  }

  public BusinessException(ResultCode cause) {
    super(cause.getMsg());
    this.code = cause.getCode();
  }

  public BusinessException(ResultCode cause, Throwable e) {
    super(cause.getMsg(), e);
    this.code = cause.getCode();
  }

  public int getCode() {
    return code;
  }

}
