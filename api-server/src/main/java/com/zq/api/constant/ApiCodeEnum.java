package com.zq.api.constant;

/**
 * API响应码
 */
public enum ApiCodeEnum {

    SUCCESS("00000", "成功"),

    UNKNOWN_ERROR("10000", "未知错误"),

    LOGIN_VALID_ERROR("10001", "登陆验证失败"),

    VERSION_ERROR("10002", "版本号错误"),

    METHOD_ERROR("10003", "调用方法不存在"),

    METHOD_HANDLER_ERROR("10004", "调用方法异常"),

    PARAM_ERROR("10005", "传递参数异常"),

    IP_BLACK("10006", "IP黑名单拦截"),

    SERVER_MAINTAIN("10007", "API服务维护中"),

    CHECK_SIGN_VALID_ERROR("10008", "签名校验失败"),

    BUSINESS_ERROR("50000", "业务处理异常"),

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
