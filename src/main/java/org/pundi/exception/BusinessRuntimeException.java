package org.pundi.exception;


import org.pundi.common.ResultCode;

/**
 * 基础的业务异常
 *
 * @author ekko
 */
public class BusinessRuntimeException extends RuntimeException {

  private int code;

  public BusinessRuntimeException() {
    super();
    this.code = 0;
  }

  public BusinessRuntimeException(String message) {
    super(message);
    this.code = 0;
  }

  public BusinessRuntimeException(int code) {
    super();
    this.code = code;
  }

  /**
   * @param code    异常标识码
   * @param message 异常说明
   */
  public BusinessRuntimeException(int code, String message) {
    super(message);
    this.code = code;
  }

  public BusinessRuntimeException(String message, Throwable e) {
    super(message, e);
  }

  /**
   * @param code    异常标识码
   * @param message 异常说明
   * @param e       原始异常
   */
  public BusinessRuntimeException(int code, String message, Throwable e) {
    super(message, e);
    this.code = code;
  }

  public BusinessRuntimeException(ResultCode cause) {
    super(cause.getMsg());
    this.code = cause.getCode();
  }

  public BusinessRuntimeException(ResultCode cause, Throwable e) {
    super(cause.getMsg(), e);
    this.code = cause.getCode();
  }

  public int getCode() {
    return code;
  }

}
