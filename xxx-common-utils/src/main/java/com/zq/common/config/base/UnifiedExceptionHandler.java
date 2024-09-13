package com.zq.common.config.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.zq.common.constant.FeignHeader;
import com.zq.common.constant.SystemName;
import com.zq.common.exception.BusinessException;
import com.zq.common.utils.ThrowableUtil;
import com.zq.common.vo.ResultVo;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * API接口统一异常处理类
 * <p>
 * &#64;RestControllerAdvice = &#64;ControllerAdvice + &#64;ResponseBody
 * </p>
 *
 * @author wilmiam
 * @since 2017-12-21
 */
@ResponseStatus(HttpStatus.OK)
@RestControllerAdvice
public class UnifiedExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(UnifiedExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResultVo handleBusinessException(BusinessException ex, HttpServletRequest request) {
        log.warn(">> business exception: {}, {}, {}", request.getRequestURI(), ex.getCode(), ex.getMessage());
        String errMessage = ex.getMessage();
        // 防止空的错误信息
        if (StringUtils.isBlank(errMessage)) {
            errMessage = "服务器繁忙";
            log.warn(">> 空的业务错误信息", ex);
        }
        return ResultVo.fail(ex.getCode(), errMessage);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResultVo handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        log.warn(">> method not supported error: {} {}, expected: {}",
                ex.getMethod(), request.getRequestURI(), ex.getSupportedHttpMethods());
        return ResultVo.fail(HttpStatus.METHOD_NOT_ALLOWED.value(), "不支持此" + ex.getMethod() + "请求");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResultVo handleMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        String detail = ex.getClass().getName();
        if (CollUtil.isNotEmpty(ex.getSupportedMediaTypes())) {
            detail = MediaType.toString(ex.getSupportedMediaTypes());
            detail = "支持的content-type: " + detail;
        }
        log.warn(">> http media type not supported error: {} {}, expected: {}",
                request.getContentType(), request.getRequestURI(), detail);
        return ResultVo.fail(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "不支持的Content-Type: " + request.getContentType());
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResultVo handleMessageConversionException(HttpMessageConversionException ex, HttpServletRequest request) {
        log.warn(">> message conversion error: {} {}", request.getRequestURI(), ex.getMessage());
        return ResultVo.fail(HttpStatus.BAD_REQUEST.value(), "无法解析请求消息");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVo handleArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.warn(">> argument not valid error: {} {}", request.getRequestURI(), ex.getMessage());
        String paranmName = ex.getParameter().getParameterName();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        if (CollUtil.isNotEmpty(fieldErrors)) {
            paranmName = fieldErrors.stream().map(f -> "[" + f.getDefaultMessage() + "(" + f.getField() + ")]").collect(Collectors.joining("、"));
        }
        return ResultVo.fail(HttpStatus.BAD_REQUEST.value(), "无效的请求参数：" + paranmName);
    }

    @ExceptionHandler({MissingServletRequestPartException.class, MissingServletRequestParameterException.class, BindException.class})
    public ResultVo handleMissingServletRequestPartException(Exception ex, HttpServletRequest request) {
        log.warn(">> missing servlet request part/param error: {} {}", request.getRequestURI(), ex.getMessage());
        String paranmName = "";
        if (ex instanceof MissingServletRequestPartException) {
            paranmName = ((MissingServletRequestPartException) ex).getRequestPartName();
        } else if (ex instanceof MissingServletRequestParameterException) {
            paranmName = ((MissingServletRequestParameterException) ex).getParameterName();
        } else if (ex instanceof BindException) {
            List<FieldError> fieldErrors = ((BindException) ex).getBindingResult().getFieldErrors();
            paranmName = fieldErrors.stream().map(f -> f.getDefaultMessage() + "[" + f.getField() + "]").collect(Collectors.joining("、"));
        }
        return ResultVo.fail(HttpStatus.BAD_REQUEST.value(), "缺少请求参数：" + paranmName);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResultVo handleDataAccessException(DataAccessException ex, HttpServletRequest request) {
        log.error(">> 访问数据失败 " + request.getRequestURI(), ex);
        String header = request.getHeader(FeignHeader.SERVER_NAME);
        String error = "服务器繁忙";
        if (StringUtils.isNotBlank(header) && SystemName.API.equals(header)) {
            error = ThrowableUtil.getStackTrace(ex);
        }
        return ResultVo.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), error);
    }

    /**
     * 权限
     *
     * @param ade
     * @param request
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResultVo handleAccessDeniedException(AccessDeniedException ade, HttpServletRequest request) {
        log.warn(">> AccessDenied Exception: {}, {}", request.getRequestURI(), ade.getMessage());
        String errMessage = ade.getMessage();
        // 防止空的错误信息
        if (StringUtils.isBlank(errMessage)) {
            errMessage = "无权限";
        }
        return ResultVo.fail(HttpStatus.FORBIDDEN.value(), errMessage);
    }

    /**
     * 客户端断开的异常
     *
     * @param cae
     * @param request
     */
    @ExceptionHandler(ClientAbortException.class)
    public void handleClientAbortException(ClientAbortException cae, HttpServletRequest request) {
        log.warn(">> ClientAbort Exception: {}, {}", request.getRequestURI(), cae.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResultVo handleMaxUploadSizeExceededException(MaxUploadSizeExceededException musee, HttpServletRequest request) {
        String message = musee.getMessage();
        log.warn(">> MaxUploadSizeExceeded Exception: {}, {}", request.getRequestURI(), message);

        String errMsg = "超过最大文件上传大小";

        String str = StrUtil.subBetween(message, "The field file exceeds its maximum permitted size of ", " bytes.");
        Long maxUploadSize = Convert.toLong(str);
        if (maxUploadSize != null) {
            errMsg += "：" + (maxUploadSize / 1024 / 1024) + "MB";
        }

        return ResultVo.fail(HttpStatus.PAYLOAD_TOO_LARGE.value(), errMsg);
    }

    @ExceptionHandler(value = Exception.class)
    public ResultVo defaultErrorHandler(Exception ex, HttpServletRequest request) {
        log.error(">> 服务器内部错误 " + request.getRequestURI(), ex);
        String header = request.getHeader(FeignHeader.SERVER_NAME);
        String error = "服务器繁忙";
        if (StringUtils.isNotBlank(header) && SystemName.API.equals(header)) {
            error = ThrowableUtil.getStackTrace(ex);
        }
        return ResultVo.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), error);
    }

}
