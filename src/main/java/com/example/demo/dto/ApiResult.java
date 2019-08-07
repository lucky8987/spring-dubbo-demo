package com.example.demo.dto;

import java.io.Serializable;

/**
 * dubbo 请求统一返回ApiResult<T>
 * @param <T>
 */
public class ApiResult<T> implements Serializable{

    private Integer status = 200;

    private String message = "success";

    private T t;

    public ApiResult(){}

    public ApiResult(T t) {
        this.t = t;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return t;
    }

    public void setData(T t) {
        this.t = t;
    }
}
