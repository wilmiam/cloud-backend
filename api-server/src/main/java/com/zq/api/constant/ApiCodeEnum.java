package com.zq.api.constant;

/**
 * API响应码
 *
 * @author wilmiam
 * @since 2021-07-14 11:37
 */
public enum ApiCodeEnum {

    /**
     * 成功
     */
    SUCCESS("200", "成功"),

    /**
     * 未知错误
     */
    UNKNOWN_ERROR("100", "未知错误"),

    /**
     * 版本号错误
     */
    VERSION_ERROR("101", "版本号错误"),

    /**
     * 调用方法不存在
     */
    METHOD_ERROR("102", "调用方法不存在"),

    /**
     * 调用方法异常
     */
    METHOD_HANDLER_ERROR("103", "调用方法异常"),

    /**
     * 传递参数异常
     */
    PARAM_ERROR("104", "传递参数异常"),

    /**
     * IP黑名单拦截
     */
    IP_BLACK("105", "IP黑名单拦截"),

    /**
     * API服务维护中
     */
    SERVER_MAINTAIN("106", "API服务维护中"),

    /**
     * 签名校验失败
     */
    CHECK_SIGN_VALID_ERROR("107", "签名校验失败"),

    /**
     * 服务不可用
     */
    SERVICE_NOT_AVAILABLE("108", "服务不可用"),

    /**
     * 业务处理失败
     */
    BUSINESS_ERROR("400", "业务处理失败"),

    /**
     * 登陆验证失败
     */
    LOGIN_VALID_ERROR("401", "登陆验证失败"),

    /**
     * 服务器繁忙
     */
    SERVER_ERROR("500", "服务器繁忙"),

    ;

    private final String code;
    private final String msg;

    ApiCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String code() {
        return code;
    }

    public String msg() {
        return msg;
    }

}
