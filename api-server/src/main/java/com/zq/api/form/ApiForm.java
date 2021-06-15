package com.zq.api.form;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zq.api.config.ConfigCache;
import com.zq.api.utils.ApiUtils;
import com.zq.api.utils.NumberUtils;
import com.zq.common.encrypt.EncryptUtils;
import com.zq.common.encrypt.RsaUtils;
import com.zq.common.vo.ApiTokenVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.TreeMap;

/**
 * api 基础form
 * <p>
 * 2016年9月29日 上午10:29:27
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
    private String bizContent; // 请求业务参数
    private JSONObject bizContentJson; // 请求业务的json对象
    private ApiTokenVo apiTokenVo;

    public boolean parseBizContent() {
        try {
            boolean flag = ConfigCache.getValueToBoolean("API.PARAM.ENCRYPT"); // API参数是否加密
            if (StrUtil.isNotBlank(bizContent) && flag) {
                bizContent = ApiUtils.decode(bizContent);
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
    public double getDouble(String key) {
        return NumberUtils.parseDbl(get(key));
    }

    /**
     * 获取参数
     * <p>
     * 2016年10月3日 下午9:02:45
     *
     * @param key
     * @return
     */
    public long getLong(String key) {
        return NumberUtils.parseLong(get(key));
    }

    public float getFloat(String key) {
        return NumberUtils.parseFloat(get(key));
    }

    /**
     * 获取参数
     * <p>
     * 2016年10月3日 下午9:02:45
     *
     * @param key
     * @return
     */
    public int getInt(String key) {
        return NumberUtils.parseInt(get(key));
    }

    /**
     * 获取参数
     * <p>
     * 2016年10月3日 下午9:02:45
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return getContentJson().getString(key);
    }

    /**
     * 获取参数
     *
     * @param key
     * @return
     */
    public Boolean getBoolean(String key) {
        return getContentJson().getBoolean(key);
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
        treeMap.put("timestamp", this.timestamp);
        treeMap.put("nonce", this.nonce);
        treeMap.put("method", this.method);
        treeMap.put("version", this.version);
        String bizContent = StrUtil.isBlank(this.bizContent) ? "" : this.bizContent;
        treeMap.put("bizContent", bizContent);
        return treeMap;
    }

}
