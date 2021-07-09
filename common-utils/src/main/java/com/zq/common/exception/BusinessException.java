package com.zq.common.exception;

/**
 * 业务错误
 *
 * @author wilmiam
 * @since 2021-07-09 17:58
 */
public class BusinessException extends RuntimeException {

    private int code = 400;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}