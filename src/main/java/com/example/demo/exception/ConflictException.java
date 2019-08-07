package com.example.demo.exception;

/**
 * 数据冲突异常
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String messsge, Throwable cause) {
        super(messsge, cause);
    }
}
