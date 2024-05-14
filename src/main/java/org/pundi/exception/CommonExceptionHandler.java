package org.pundi.exception;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.pundi.common.Result;
import org.pundi.common.ResultCode;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月10日 20:15:00
 */
@Slf4j
@ControllerAdvice
public class CommonExceptionHandler {

  /**
   * 业务异常处理
   *
   * @param ex 异常实体
   * @return 返回值
   */
  @ExceptionHandler(value = {BusinessException.class})
  @ResponseBody
  public Result<Object> bizException(BusinessException ex) {

    log.error(ex.getMessage(), ex);
    return Result.error(ex.getCode(), ex.getMessage());
  }


  /**
   * 参数异常处理
   *
   * @param ex 异常实体
   * @return 返回值
   */
  @ExceptionHandler(value = {ParamErrorException.class})
  @ResponseBody
  public Result<Object> paramErrorException(ParamErrorException ex) {

    log.warn("参数异常 msg:{}", ex.getMessage(), ex);
    return Result.error(ex.getCode(), ex.getMessage());
  }

  /**
   * 用来处理bean validation异常
   *
   * @param ex 异常实体
   * @return 返回值
   */
  @ExceptionHandler({ConstraintViolationException.class, InvalidParameterException.class})
  @ResponseBody
  public Result<Object> resolveConstraintViolationException(ConstraintViolationException ex) {

    Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
    if (!CollectionUtils.isEmpty(constraintViolations)) {
      StringBuilder msgBuilder = new StringBuilder();
      for (ConstraintViolation constraintViolation : constraintViolations) {
        msgBuilder.append(constraintViolation.getMessage()).append(",");
      }
      String errorMessage = msgBuilder.toString();
      if (errorMessage.length() > 1) {
        errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
      }
      log.warn("参数校验错误, {}", errorMessage);
      ResultCode cause = ResultCode.PARAM_ERROR;

      return Result.error(cause.getCode(), errorMessage);
    }
    return Result.error(-1, "参数校验错误");
  }


  /**
   * 处理方法参数校验失败异常
   *
   * @param ex 异常实体
   * @return 返回值
   */
  @ExceptionHandler(value = {MethodArgumentNotValidException.class})
  @ResponseBody
  public Result<Object> resolveMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

    List<ObjectError> objectErrors = ex.getBindingResult().getAllErrors();

    return getObjectApiResponse(objectErrors);
  }

  private Result<Object> getObjectApiResponse(List<ObjectError> objectErrors) {

    if (!CollectionUtils.isEmpty(objectErrors)) {
      StringBuilder msgBuilder = new StringBuilder();
      for (ObjectError objectError : objectErrors) {
        if (objectError instanceof FieldError) {
          msgBuilder.append(((FieldError) objectError).getField()).append(" ");
        }
        msgBuilder.append(objectError.getDefaultMessage()).append(",");
      }
      String errorMessage = msgBuilder.toString();
      if (errorMessage.length() > 1) {
        errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
      }
      log.warn("参数校验错误, {}", errorMessage);
      ResultCode cause = ResultCode.PARAM_ERROR;

      return Result.error(cause.getCode(), errorMessage);
    }
    return Result.error(-1, "参数校验错误");
  }
}
