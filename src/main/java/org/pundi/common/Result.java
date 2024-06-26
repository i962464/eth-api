package org.pundi.common;

import java.io.Serializable;

import lombok.Data;

/**
 * @author ekko
 * @version 1.0.0
 * @Description
 * @createTime 2024年05月10日 16:10:00
 */
@Data
public class Result<T> implements Serializable {

  private static final long serialVersionUID = -3960261604605958516L;

  private int code;
  private String msg;
  private T data;

  // get set方法，限于篇幅问题，这里不写了，大家操作的时候自己生成一下。或者使用lombok
  public static <T> Result<T> success() {
    return new Result<>();
  }

  /**
   * 成功,默认状态码,返回消息,自定义返回数据
   *
   * @param data 自定义返回数据
   * @param <T>  返回类泛型,不能为String
   * @return 通用返回Result
   */
  public static <T> Result<T> success(T data) {
    return new Result<>(data);
  }

  /**
   * 成功,默认状态码,自定义返回消息,无返回数据
   *
   * @param msg 自定义返回消息
   * @param <T> 返回类泛型
   * @return 通用返回Result
   */
  public static <T> Result<T> success(String msg) {
    return new Result<>(msg);
  }

  /**
   * 成功,默认状态码,自定义返回消息,返回数据
   *
   * @param msg  自定义返回消息
   * @param data 自定义返回数据
   * @param <T>  返回类泛型
   * @return 通用返回Result
   */
  public static <T> Result<T> success(String msg, T data) {
    return new Result<>(msg, data);
  }

  /**
   * 失败,默认状态码,返回消息,无返回数据
   *
   * @param <T> 返回类泛型
   * @return 通用返回Result
   */
  public static <T> Result<T> error() {
    return new Result<>(ResultCode.ERROR);
  }

  /**
   * 失败,默认状态码,自定义返回消息,无返回数据
   *
   * @param <T> 返回类泛型
   * @return 通用返回Result
   */
  public static <T> Result<T> error(String msg) {
    return new Result<>(ResultCode.ERROR.getCode(), msg);
  }

  /**
   * 失败,自定义状态码,返回消息,无返回数据
   *
   * @param code 自定义状态码
   * @param msg  自定义返回消息
   * @param <T>  返回类泛型
   * @return 通用返回Result
   */
  public static <T> Result<T> error(int code, String msg) {
    return new Result<>(code, msg);
  }

  /**
   * 失败,使用CodeMsg状态码,返回消息,无返回数据
   *
   * @param resultCode CodeMsg,参数如下:
   *                   <p> code 状态码
   *                   <p> msg  返回消息
   * @param <T>        返回类泛型
   * @return 通用返回Result
   */
  public static <T> Result<T> error(ResultCode resultCode) {
    return new Result<>(resultCode);
  }

  /**
   * 成功构造器,无返回数据
   */
  private Result() {
    this(ResultCode.SUCCESS);
  }

  /**
   * 成功构造器,自定义返回数据
   *
   * @param data 返回数据
   */
  private Result(T data) {
    this(ResultCode.SUCCESS, data);
  }

  /**
   * 成功构造器,自定义返回消息,无返回数据
   *
   * @param msg 返回消息
   */
  private Result(String msg) {
    this(ResultCode.SUCCESS.getCode(), msg);
  }

  /**
   * 成功构造器,自定义返回信息,返回数据
   *
   * @param msg  返回信息
   * @param data 返回数据
   */
  private Result(String msg, T data) {
    this(ResultCode.SUCCESS.getCode(), msg, data);
  }

  /**
   * 构造器,自定义状态码,返回消息
   *
   * @param code 状态码
   * @param msg  返回消息
   */
  private Result(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  /**
   * 构造器,自定义状态码,返回消息,返回数据
   *
   * @param code 状态码
   * @param msg  返回消息
   * @param data 返回数据
   */
  private Result(int code, String msg, T data) {
    this(code, msg);
    this.data = data;
  }

  /**
   * 构造器,使用CodeMsg状态码与返回信息
   *
   * @param resultCode CodeMsg,参数如下:
   *                   <p> code 状态码
   *                   <p> msg  返回消息
   */
  private Result(ResultCode resultCode) {
    this(resultCode.getCode(), resultCode.getMsg());
  }

  /**
   * 构造器,使用CodeMsg状态码与返回信息,自定义返回数据
   *
   * @param resultCode CodeMsg,参数如下:
   *                   <p> code 状态码
   *                   <p> msg  返回消息
   * @param data       返回数据
   */
  private Result(ResultCode resultCode, T data) {
    this(resultCode);
    this.data = data;
  }

  public boolean hasError() {
    return this.code != ResultCode.SUCCESS.getCode();
  }
}
