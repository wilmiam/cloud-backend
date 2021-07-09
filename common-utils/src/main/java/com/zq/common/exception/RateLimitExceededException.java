package com.zq.common.exception;

/**
 * @author wilmiam
 * @since 2021-07-09 17:56
 */
public class RateLimitExceededException extends RuntimeException {

    public RateLimitExceededException() {
    }

    public RateLimitExceededException(String message) {
        super(message);
    }

    public RateLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }

    public RateLimitExceededException(Throwable cause) {
        super(cause);
    }

}
