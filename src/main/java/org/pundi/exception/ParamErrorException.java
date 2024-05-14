package org.pundi.exception;


import org.pundi.common.ResultCode;

/**
 * 基础的业务异常
 *
 * @author ekko
 */
public class ParamErrorException extends BusinessRuntimeException {

  public ParamErrorException(String message) {

    super(ResultCode.PARAM_ERROR.getCode(), message);
  }

  public ParamErrorException(ResultCode cause) {
    super(cause);
  }

}
