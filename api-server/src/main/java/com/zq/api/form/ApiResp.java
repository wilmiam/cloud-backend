package com.zq.api.form;

import com.zq.api.constant.ApiCodeEnum;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ApiResp {

    private String apiNo = "";
    private String code = ApiCodeEnum.SUCCESS.code();
    private String msg = ApiCodeEnum.SUCCESS.msg();
    private Map<String, Map<String, Object>> data = new HashMap<>();

    public ApiResp(ApiForm from) {
        setFrom(from);
    }

    public ApiResp(ApiForm from, Map<String, Object> map) {
        setFrom(from);
        data.put("data", map);
    }

    public ApiResp(ApiForm from, ApiCodeEnum apiCodeEnum) {
        setFrom(from);
        this.code = apiCodeEnum.code();
        this.msg = apiCodeEnum.msg();
    }

    public ApiResp(ApiCodeEnum apiCodeEnum) {
        this.code = apiCodeEnum.code();
        this.msg = apiCodeEnum.msg();
    }

    public ApiResp setFrom(ApiForm from) {
        if (from != null) {
            this.apiNo = from.getApiNo();
        }
        return this;
    }

    public String getApiNo() {
        return apiNo;
    }

    public ApiResp setApiNo(String apiNo) {
        this.apiNo = apiNo;
        return this;
    }

    public String getCode() {
        return code;
    }

    public ApiResp setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ApiResp setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Map<String, Object> getData() {
        return this.data.get("data");
    }

    public ApiResp setData(Map<String, Object> dataMap) {
        this.data.put("data", dataMap);
        return this;
    }

    public ApiResp addData(String key, Object value) {
        Map<String, Object> dataMap = getData();
        if (dataMap == null) {
            dataMap = new HashMap<>();
        }
        dataMap.put(key, value);
        this.data.put("data", dataMap);
        return this;
    }

    @Override
    public String toString() {
        return "[apiNo=" + this.apiNo + "]"
                + "[code=" + this.code + "]"
                + "[msg=" + this.msg + "]"
                + "[data=" + this.data + "]";
    }

}
