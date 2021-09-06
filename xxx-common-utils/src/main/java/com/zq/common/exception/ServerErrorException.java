package com.zq.common.exception;

/**
 * 服务器错误
 *
 * @author wilmiam
 * @since 2021-07-09 17:59
 */
public class ServerErrorException extends RuntimeException {

    public ServerErrorException() {
    }

    public ServerErrorException(String message) {
        super(message);
    }

    public ServerErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerErrorException(Throwable cause) {
        super(cause);
    }
}
