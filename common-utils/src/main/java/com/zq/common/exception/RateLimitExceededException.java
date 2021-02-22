package com.zq.common.exception;

/**
 * @author wilmiam
 * @since 2019-03-21
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
