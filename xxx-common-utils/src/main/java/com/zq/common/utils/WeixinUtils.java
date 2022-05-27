package com.zq.common.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.weixin.sdk.kit.PaymentKit;
import com.jfinal.wxaapp.WxaConfig;
import com.jfinal.wxaapp.WxaConfigKit;
import com.jfinal.wxaapp.api.WxaAccessToken;
import com.jfinal.wxaapp.api.WxaAccessTokenApi;
import com.zq.common.vo.weixin.GenerateSchemeVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信工具类
 *
 * @author wilmiam
 * @since 2021-07-09 18:05
 */
@Slf4j
@Data
public class WeixinUtils {

    /**
     * 小程序发送红包的请求
     */
    private String sendminiprogramhb = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendminiprogramhb";

    /**
     * 企业向个人用户付款
     */
    private String transfers = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

    /**
     * 获取小程序码，适用于需要的码数量极多的业务场景。通过该接口生成的小程序码，永久有效，数量暂无限制。 更多用法详见 获取二维码。
     * https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/qr-code/wxacode.getUnlimited.html
     *
     * @param accessToken
     * @param scene
     * @param params
     * @return
     */
    public static String getwxacodeunlimit(String accessToken, String scene, Map<String, Object> params) {
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;

        JSONObject param = new JSONObject();
        param.put("scene", scene);
        param.put("page", "pages/index/index");
        if (CollUtil.isNotEmpty(params)) {
            param.putAll(params);
        }

        Map<String, String> header = new HashMap<>();
        header.put("Content-type", "application/json; charset=utf-8");
        header.put("Accept", "application/json");

        byte[] bytes = HttpRequest.post(url)
                .body(param.toString())
                .headerMap(header, true)
                .execute().bodyBytes();
        return "data:image/png;base64," + Base64.encode(bytes);
    }

    /**
     * 调用微信开放接口msgSecCheck检测文字内容
     * 频率限制:单个appid调用上限为2000次/分钟，1,000,000次/天
     *
     * @param msg         要检测的文字内容
     * @param accessToken 小程序全局唯一后台接口调用凭据
     * @return false:含敏感信息 true：正常
     */
    public static Boolean checkMsg(String msg, String accessToken) {
        String url = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + accessToken;
        //创建客户端
        HttpClient httpclient = HttpClients.createDefault();
        //创建一个post请求
        HttpPost request = new HttpPost(url);
        //设置响应头
        request.setHeader("Content-Type", "application/json;charset=UTF-8");
        //通过fastJson设置json数据
        JSONObject postData = new JSONObject();
        //设置要检测的内容
        postData.put("content", msg);
        String jsonString = postData.toString();
        request.setEntity(new StringEntity(jsonString, "utf-8"));
        //  由客户端执行(发送)请求
        try {
            HttpResponse response = httpclient.execute(request);
            // 从响应模型中获取响应实体
            HttpEntity entity = response.getEntity();
            //得到响应结果
            String result = EntityUtils.toString(entity, "utf-8");
            //打印检测结果
            log.debug("检测结果: {}", result);
            //将响应结果变成json
            JSONObject resultJsonObject = JSONObject.parseObject(result);

            String errcode = resultJsonObject.getString("errcode");
            // 当content内含有敏感信息，则返回87014
            if ("87014".equals(errcode)) {
                log.warn("包含敏感词，请重新输入！");
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检测图片是否含有违法违规内容
     * 频率限制:单个appid调用上限为1000次/分钟，100,000次/天
     *
     * @param inputStream 图片文件  流multipartFile.getInputStream()
     * @param contentType 图片文件类型  multipartFile.getContentType()
     * @return true:含敏感信息   false：正常
     * media 要检测的图片文件，格式支持PNGJPEGJPGGIF, 像素不超过750x1334
     */
    public static Boolean checkImg(InputStream inputStream, String contentType, String accessToken) {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();

            HttpPost request = new HttpPost("https://api.weixin.qq.com/wxa/img_sec_check?access_token=" + accessToken);
            request.addHeader("Content-Type", "application/octet-stream");
            byte[] byt = new byte[inputStream.available()];
            inputStream.read(byt);
            request.setEntity(new ByteArrayEntity(byt, ContentType.create(contentType)));

            CloseableHttpResponse response = httpclient.execute(request);
            HttpEntity httpEntity = response.getEntity();
            String result = EntityUtils.toString(httpEntity, "UTF-8");

            //打印检测结果
            log.debug("检测结果: {}", result);

            JSONObject jso = JSONObject.parseObject(result);

            String errcode = jso.getString("errcode");
            // 当content内含有敏感信息，则返回87014
            if ("87014".equals(errcode)) {
                log.warn("图片内容违规，请重新上传！");
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取访问凭证
     * 使用jfinal自带的方式缓存在内存中
     *
     * @param appId
     * @param appSecret
     * @return
     */
    public static String getAccessToken(String appId, String appSecret) {
        WxaConfig apiConfig = new WxaConfig();
        apiConfig.setAppId(appId);
        apiConfig.setAppSecret(appSecret);
        WxaConfigKit.setWxaConfig(apiConfig);

        WxaAccessToken accessToken = WxaAccessTokenApi.getAccessToken();

        return accessToken.getAccessToken();
    }

    /**
     * 商户支付请求方法
     *
     * @param payMchId 商户号id
     * @param certFile 证书
     * @param params   传入参数
     * @return
     */
    public static String postSsl(String requestUrl, String payMchId, byte[] certFile, Map<String, String> params) {
        StringBuilder message = new StringBuilder();
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new ByteArrayInputStream(certFile), payMchId.toCharArray());
            SSLContext sslcontext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, payMchId.toCharArray())
                    .build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,
                    new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());

            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();

            HttpPost httpPost = new HttpPost(requestUrl);
            httpPost.addHeader("Connection", "keep-alive");
            httpPost.addHeader("Accept", "*/*");
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpPost.addHeader("Host", "api.mch.weixin.qq.com");
            httpPost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpPost.addHeader("Cache-Control", "max-age=0");
            httpPost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpPost.setEntity(new StringEntity(PaymentKit.toXml(params), "UTF-8"));

            CloseableHttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8));
                String text;
                while ((text = bufferedReader.readLine()) != null) {
                    message.append(text);
                }
            }
            EntityUtils.consume(entity);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return message.toString();
    }

    /**
     * 商户支付零钱参数
     *
     * @param appId        APPID
     * @param mchId        商户ID
     * @param paySecretKey 商户支付密钥
     * @param openId       收款用户微信openId
     * @param tradeNo      商户交易编号
     * @param amount       商户支付金额, 单位: 分
     * @param desc         付款备注
     * @return
     */
    public Map<String, String> getMchPayParams(String appId, String mchId, String paySecretKey, String openId, String tradeNo, String amount, String desc) {
        Map<String, String> packageParams = new HashMap<>();
        // 商户账号appId
        packageParams.put("mch_appid", appId);
        // 商户号
        packageParams.put("mchid", mchId);
        // 用户openId
        packageParams.put("openid", openId);
        // 商户订单号
        packageParams.put("partner_trade_no", tradeNo);
        // 企业付款金额，单位为分
        packageParams.put("amount", amount);
        // 企业付款备注
        packageParams.put("desc", desc);
        // 随机32位字符串
        packageParams.put("nonce_str", IdUtil.simpleUUID());
        // 校验用户姓名选项 (NO_CHECK：不校验真实姓名 FORCE_CHECK：强校验真实姓名)
        packageParams.put("check_name", "NO_CHECK");
        // 收款用户姓名 校验姓名
        // packageParams.put("re_user_name", "收款人姓名");

        // 获取签名
        String sign = PaymentKit.createSign(packageParams, paySecretKey);
        packageParams.put("sign", sign);
        return packageParams;
    }

    /**
     * 普通小程序或服务商小程序发红包参数
     *
     * @param appId        APPID
     * @param mchId        商户ID
     * @param mchName      商户名称
     * @param paySecretKey 商户支付秘钥
     * @param openId       收款用户微信openId
     * @param amount       商户支付金额
     * @param actName      活动名称
     * @param wishing      祝福语
     * @param remark       备注
     * @return
     */
    public Map<String, String> getSendRedPacketParams(String appId, String mchId, String mchName, String paySecretKey, String openId, String amount, String actName, String wishing, String remark) {
        Map<String, String> params = new HashMap<>();
        //公众账号appid
        params.put("wxappid", appId);
        //商户号
        params.put("mch_id", mchId);
        //商户名称
        params.put("send_name", mchName);
        //商户订单号
        // 组成： mch_id + yyyymmdd + 10位一天内不能重复的数字。
        params.put("mch_billno", mchId + DateUtil.format(DateUtil.date(), DatePattern.PURE_DATE_PATTERN) + (System.currentTimeMillis() / 1000 + "").substring(0, 9));
        //用户openid
        params.put("re_openid", openId);
        //付款金额
        params.put("total_amount", amount);
        // 随机32位字符串
        params.put("nonce_str", IdUtil.simpleUUID());
        //活动名称
        params.put("act_name", actName);
        //通知用户形式
        params.put("notify_way", "MINI_PROGRAM_JSAPI");
        //备注
        params.put("remark", remark);
        //红包发放总人数
        params.put("total_num", "1");
        //红包祝福语
        params.put("wishing", wishing);

        //签名
        String sign = PaymentKit.createSign(params, paySecretKey);
        params.put("sign", sign);

        return params;
    }

    /**
     * 获取跳转微信小程序的链接
     *
     * @return
     */
    public static String generateScheme(String accessToken, GenerateSchemeVo vo) {
        String url = "https://api.weixin.qq.com/wxa/generatescheme?access_token=" + accessToken;
        Map<String, Object> params = new HashMap<>();
        params.put("jump_wxa", vo.getJumpWxa());
        params.put("expire_type", vo.getExpireType());
        params.put("expire_time", vo.getExpireTime());
        params.put("expire_interval", vo.getExpireInterval());

        HttpRequest request = HttpUtil.createPost(url)
                .body(JSONUtil.toJsonStr(params));

        log.info("获取跳转微信小程序的链接请求：{}", request.toString());

        cn.hutool.http.HttpResponse response = request.execute();

        String body = response.body();
        log.debug("获取跳转微信小程序的链接响应 {} => {}", response.getStatus(), body);

        cn.hutool.json.JSONObject object = JSONUtil.parseObj(body);
        Integer errcode = object.getInt("errcode");
        if (errcode == null || errcode != 0) {
            log.error("获取跳转连接失败：{}", object.getStr("errmsg"));
        }

        return object.getStr("openlink");
    }

}
