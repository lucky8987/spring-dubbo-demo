package com.example.demo.exception;

/**
 * 异常码(同status)
 */
public enum ExceptionCode {

    REQEUST_EXC(400, "错误请求"),
    UNAUTHORIZED(401, "身份验证错误"),
    FORBIDDEN(403,"请求被拒绝"),
    NOT_FOUND(410,"资源不存在"),
    REQUEST_TIME_OUT(408, "请求超时"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用");

    private int code;

    private String msgHead;

    ExceptionCode(int code, String msgHead) {
        this.code = code;
        this.msgHead = msgHead;
    }

    public int getCode() {
        return code;
    }


    public String getMsgHead() {
        return msgHead;
    }
}
