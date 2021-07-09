package com.zq.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zq.common.context.ThreadContext;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 统一的接口响应信息
 *
 * @author wilmiam
 * @since 2021-07-09 17:45
 */
@ApiModel("API响应消息")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultVo<T> implements Serializable {

    @ApiModelProperty(value = "成功标记", example = "true")
    private boolean success;

    @ApiModelProperty("错误码")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int errCode;

    @ApiModelProperty("错误信息")
    private String errMsg;

    @ApiModelProperty("响应的数据")
    private T data;

    public static ResultVo success() {
        ThreadContext.close();

        return success(null);
    }

    public static <E> ResultVo<E> success(E data) {
        ThreadContext.close();

        ResultVo<E> result = new ResultVo<>();
        result.setSuccess(true);
        result.setData(data);
        return result;
    }

    public static ResultVo fail(String errMsg) {
        ThreadContext.close();

        return fail(500, errMsg);
    }

    public static ResultVo fail(int errCode, String errMsg) {
        ThreadContext.close();

        ResultVo result = new ResultVo<>();
        result.setSuccess(false);
        result.setErrCode(errCode);
        result.setErrMsg(errMsg);
        return result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
