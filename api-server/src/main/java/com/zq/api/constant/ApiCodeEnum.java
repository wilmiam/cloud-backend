package com.zq.api.constant;

/**
 * API响应码
 */
public enum ApiCodeEnum {

    SUCCESS("200", "成功"),

    UNKNOWN_ERROR("100", "未知错误"),

    LOGIN_VALID_ERROR("101", "登陆验证失败"),

    VERSION_ERROR("102", "版本号错误"),

    METHOD_ERROR("103", "调用方法不存在"),

    METHOD_HANDLER_ERROR("104", "调用方法异常"),

    PARAM_ERROR("105", "传递参数异常"),

    IP_BLACK("106", "IP黑名单拦截"),

    SERVER_MAINTAIN("107", "API服务维护中"),

    CHECK_SIGN_VALID_ERROR("108", "签名校验失败"),

    BUSINESS_ERROR("400", "业务处理失败"),

    SERVER_ERROR("500", "服务器繁忙"),

    ;

    private String code;
    private String msg;

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
