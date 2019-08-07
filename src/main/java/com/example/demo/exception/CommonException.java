package com.example.demo.exception;

/**
 * 通用异常类: 业务异常的基类
 */
public class CommonException extends RuntimeException {

    private ExceptionCode exceptionCode;

    public CommonException(ExceptionCode exceptionCode, String message){
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public CommonException(ExceptionCode exceptionCode, String message, Throwable cause) {
        super(message, cause);
        this.exceptionCode = exceptionCode;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }
}
