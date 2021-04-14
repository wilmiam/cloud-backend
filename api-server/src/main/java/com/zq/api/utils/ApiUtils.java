package com.zq.api.utils;

import cn.hutool.crypto.digest.MD5;
import com.zq.api.constant.ApiCodeEnum;
import com.zq.api.form.ApiForm;
import com.zq.api.form.ApiResp;
import com.zq.api.service.IApiLogic;
import com.zq.api.service.impl.ApiV100Logic;
import com.zq.api.service.impl.ApiV101Logic;
import com.zq.common.vo.ResultVo;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Component
public class ApiUtils {

    private static final Map<String, IApiLogic> map = new HashMap<>();
    /**
     * 调试日志
     */
    public static boolean DEBUG = false;

    public ApiUtils(ApiV100Logic apiV100Logic, ApiV101Logic apiV101Logic) {
        addApi("1.0.0", apiV100Logic);
        addApi("1.0.1", apiV101Logic);
    }

    public static void addApi(String version, IApiLogic apiLogic) {
        map.put(version, apiLogic);
    }

    public static IApiLogic getApiLogic(ApiForm form) {
        return map.get(form.getVersion());
    }

    /**
     * 获取成功响应
     *
     * @param form
     * @return
     */
    public static ApiResp getSuccessResp(ApiForm form) {
        return new ApiResp(form, ApiCodeEnum.SUCCESS);
    }

    /**
     * api关闭resp
     * <p>
     * 2016年9月29日 上午11:44:38
     *
     * @return
     */
    public static ApiResp getServerMaintain(ApiForm form) {
        return new ApiResp(form, ApiCodeEnum.SERVER_MAINTAIN);
    }

    /**
     * 获取登录严重异常
     * <p>
     * 2016年9月29日 上午11:44:38
     *
     * @return
     */
    public static ApiResp getLoginValidError(ApiForm form) {
        return new ApiResp(form, ApiCodeEnum.LOGIN_VALID_ERROR);
    }

    /**
     * 版本错误返回resp
     * <p>
     * 2016年9月29日 上午11:44:38
     *
     * @return
     */
    public static ApiResp getVersionErrorResp(ApiForm form) {
        return new ApiResp(form, ApiCodeEnum.VERSION_ERROR);
    }

    /**
     * ip黑名单返回resp
     * <p>
     * 2016年9月29日 上午11:44:38
     *
     * @return
     */
    public static ApiResp getIpBlackResp(ApiForm form) {
        return new ApiResp(form, ApiCodeEnum.IP_BLACK);
    }

    /**
     * 调用方法不存在resp
     * <p>
     * 2016年9月29日 上午11:44:38
     *
     * @return
     */
    public static ApiResp getMethodError(ApiForm form) {
        return new ApiResp(form, ApiCodeEnum.METHOD_ERROR);
    }

    /**
     * 调用方法异常resp
     * <p>
     * 2016年9月29日 上午11:44:38
     *
     * @return
     */
    public static ApiResp getMethodHandlerError(ApiForm form) {
        return new ApiResp(form, ApiCodeEnum.METHOD_HANDLER_ERROR);
    }

    public static ApiResp toApiResp(ApiForm form, ResultVo resultVo) {
        ApiResp apiResp = new ApiResp(form);
        if (resultVo.isSuccess()) {
            apiResp.addData("data", resultVo.getData() == null ? "" : resultVo.getData());
        } else {
            return apiResp.setCode(String.valueOf(resultVo.getErrCode())).setMsg(resultVo.getErrMsg());
        }
        return apiResp;
    }

    /**
     * 解码
     * <p>
     * 2017年3月15日 下午1:49:09
     *
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String decode(String params) throws UnsupportedEncodingException {
        params = URLDecoder.decode(params, "utf-8");
        params = new String(Base64.decodeBase64(params.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        return params;
    }

    /**
     * 编码
     * <p>
     * 2017年3月15日 下午1:49:09
     *
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encode(String params) throws UnsupportedEncodingException {
        params = new String(Base64.encodeBase64(params.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        params = URLEncoder.encode(params, "utf-8");
        return params;
    }

    /**
     * 获取验证sign
     * <p>
     * 2017年3月15日 下午3:14:27
     *
     * @param paramMaps
     * @return
     */
    public static String getSign(TreeMap<String, String> paramMaps) {
        String nonce = "";
        // 原始请求串
        StringBuilder src = new StringBuilder();
        for (Map.Entry<String, String> entry : paramMaps.entrySet()) {
            if (entry.getKey().equals("nonce")) {
                nonce = entry.getValue();
                continue;
            }
            src.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        // 待加密串
        src.append("nonce=").append(nonce == null ? "" : nonce);
        System.out.println("签名：" + src.toString());
        return MD5.create().digestHex(src.toString());
    }
}
