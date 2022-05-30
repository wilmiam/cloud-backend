package com.zq.api.form;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zq.api.config.ConfigCache;
import com.zq.api.utils.ApiUtils;
import com.zq.common.vo.ApiTokenVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.TreeMap;

/**
 * api 基础form
 *
 * @author wilmiam
 * @since 2021-07-13 09:59
 */
@Slf4j
@Data
public class ApiForm {

    private String appId;
    private String token;
    private String userId;
    private String method; // 请求方法
    private String charset; // 编码
    private String format; // 返回参数格式
    private String signType; // 签名类型
    private String sign; // 签名
    private String timestamp; // 时间戳, 单位: 毫秒
    private String nonce; // 随机字串(建议使用UUID)
    private String version; // 接口版本
    private String apiNo; // 接口码
    private Integer type; // 1-app；2-api，内部定
    private String clientType; // 客户端类型 xcx-小程序，H5，api
    private String bizContent; // 请求业务参数
    private JSONObject bizContentJson; // 请求业务的json对象
    private MultipartFile file; // 上传文件用
    private ApiTokenVo apiTokenVo;

    public boolean parseBizContent() {
        try {
            if (type == 1) {
                // API参数是否加密
                boolean flag = ConfigCache.getValueToBoolean("API.PARAM.ENCRYPT");
                if (StrUtil.isNotBlank(bizContent) && flag) {
                    bizContent = ApiUtils.decode(bizContent, "");
                }
            } else {
                if (StrUtil.isNotBlank(bizContent)) {
                    bizContent = ApiUtils.decode(bizContent, "BASE64");
                }
            }

            if (bizContent == null) {
                bizContent = "";
            }
            bizContentJson = JSON.parseObject(bizContent);
            if (bizContentJson == null) {
                bizContentJson = new JSONObject();
            }
            return true;
        } catch (Exception e) {
            log.error("bizContent解析失败：{}", e.getMessage());
            return false;
        }
    }

    public JSONObject getContentJson() {
        if (bizContentJson != null) {
            return bizContentJson;
        }

        parseBizContent();
        return bizContentJson;
    }

    /**
     * 获取参数
     * <p>
     * 2016年10月3日 下午9:02:45
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        String value = getContentJson().getString(key);
        return value == null ? "" : value;
    }

    /**
     * 获取参数
     * <p>
     * 2016年10月3日 下午9:02:45
     *
     * @param key
     * @return
     */
    public JSONObject getJSONObject(String key) {
        return getContentJson().getJSONObject(key);
    }

    /**
     * 获取参数
     * <p>
     * 2016年10月3日 下午9:02:45
     *
     * @param key
     * @return
     */
    public JSONArray getJSONArray(String key) {
        return getContentJson().getJSONArray(key);
    }

    /**
     * 获取参数
     *
     * @return
     */
    public Map<String, Object> getParamsMap() {
        return getParamsMap(false);
    }

    /**
     * 获取参数
     *
     * @return
     */
    public Map<String, Object> getParamsMap(boolean isSetUserId) {
        return getParamsMap(isSetUserId, null);
    }

    /**
     * 获取参数
     *
     * @return
     */
    public Map<String, Object> getParamsMap(boolean isSetUserId, String key) {
        JSONObject json = getContentJson();
        Map<String, Object> innerMap = json.getInnerMap();
        innerMap.put("token", getToken());
        if (isSetUserId) {
            if (StringUtils.isBlank(key)) {
                innerMap.put("userId", userId);
            } else {
                innerMap.put(key, userId);
            }
        }
        return innerMap;
    }

    public TreeMap<String, String> getSignTreeMap() {
        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("appId", this.appId);
        treeMap.put("apiNo", this.apiNo);
        treeMap.put("timestamp", this.timestamp);
        treeMap.put("method", this.method);
        treeMap.put("version", this.version);
        String bizContent = StrUtil.isBlank(this.bizContent) ? "" : this.bizContent;
        treeMap.put("bizContent", bizContent);
        return treeMap;
    }

    public String getSignStr(String key) {
        TreeMap<String, String> treeMap = getSignTreeMap();

        // 原始请求串
        StringBuilder src = new StringBuilder();
        for (Map.Entry<String, String> entry : treeMap.entrySet()) {
            src.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        src.append("key=").append(key);

        return src.toString();
    }

}
