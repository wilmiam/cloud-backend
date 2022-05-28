package com.zq.common.exception;

/**
 * 权限验证错误
 *
 * @author wilmiam
 * @since 2021-07-09 17:59
 */
public class AuthorizationException extends RuntimeException {

    public AuthorizationException() {
    }

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorizationException(Throwable cause) {
        super(cause);
    }

}
