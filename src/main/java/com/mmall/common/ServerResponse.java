package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * 高服用的响应对象
 * @param <T>
 */
//保证序列化json的时候，null值不会传
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {
    private int status;
    private String msg;
    private T data;

    /**
     * 响应status必须构造
     * @param status
     */
    private ServerResponse(int status){
        this.status=status;
    }

    private ServerResponse(int status, T t) {
        this.status = status;
        this.data = t;
    }

    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ServerResponse(int status, String msg, T t) {
        this.status = status;
        this.msg = msg;
        this.data = t;
    }

    /**
     * 对外开放的方法
     * @return
     */
    @JsonIgnore
    public boolean isSuccess(){
        return this.status==ResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    /**
     * 失败的时候不返回data
     * @return
     */
    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 构造
     * @param <T>
     * @return
     */
    public static <T> ServerResponse<T> creatBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> creatBySuccess(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }

    public static <T> ServerResponse<T> creatBySuccess(T t){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),t);
    }

    public static <T> ServerResponse<T> creatBySuccess(String msg,T t){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,t);
    }

    /**
     * 失败的响应
     * @param <T>
     * @return
     */
    public static <T> ServerResponse<T> creatByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> creatByErrorMessage(String errotMessage){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errotMessage);
    }

    public static <T> ServerResponse<T> creatByErrorCodeMessage(int errorCode,String errorMessage){
        return new ServerResponse<T>(errorCode,errorMessage);
    }
}
