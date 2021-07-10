package com.zq.common.http;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zq.common.constant.CloudConstant;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Objects;

/**
 * @author wilmiam
 * @since 2021-07-10 16:30
 */
public class HttpRequestUtils {
    private static boolean ipLocal = false;

    private static final Logger log = LoggerFactory.getLogger(HttpRequestUtils.class);

    private static final String MICROSOFT_IE = "MSIE";
    private static final int IE_URL_LIMIT = 150;

    /**
     * localhost的IP地址(ipv4)
     */
    private static final String LOCALHOST_IP_V4 = "127.0.0.1";

    /**
     * localhost的IP地址(ipv6)
     */
    private static final String LOCALHOST_IP_V6 = "0:0:0:0:0:0:0:1";

    private static final String UNKNOWN = "unknown";
    private static final String COMMA = ",";
    private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR",
            "X-Real-IP"};

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * 获取请求的客户端ip地址
     *
     * @return
     */
    public static String getClientIp() {
        return getClientIp(getRequest());
    }

    /**
     * 获取请求的客户端ip地址
     *
     * @param request
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        String ipAddress;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if ("127.0.0.1".equals(ipAddress)) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet;
                    try {
                        inet = InetAddress.getLocalHost();
                        ipAddress = inet.getHostAddress();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }

    /**
     * 获取当前URL+Parameter
     *
     * @param request
     * @return 返回完整URL
     */
    public static String getRequestUrl(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        String queryString = req.getQueryString();

        queryString = StringUtils.isBlank(queryString) ? "" : "?" + queryString;
        return req.getRequestURI() + queryString;
    }

    /**
     * 获取body内容
     *
     * @return
     */
    public static JSONObject getBodyParams() {
        return getBodyParams(getRequest());
    }

    /**
     * 获取body内容
     *
     * @return
     */
    public static JSONObject getBodyParams(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return JSON.parseObject(sb.toString());
    }

    /**
     * 判断请求是否为Ajax请求
     *
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        return request.getHeader("X-Requested-With") != null
                && "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }

    /**
     * 判断一个ip是否为localhost的ip(仅检查默认的ipv4和ipv6地址)
     *
     * @param ip
     * @return
     */
    public static boolean isLocalhostIp(String ip) {
        return LOCALHOST_IP_V6.equals(ip) || LOCALHOST_IP_V4.equals(ip);
    }

    /**
     * 根据不同浏览器将文件名中的汉字转为UTF8编码的串,以便下载时能正确显示另存的文件名.
     *
     * @param request
     * @param sourcename 原文件名
     * @return 重新编码后的文件名
     */
    public static String encodeFileName(HttpServletRequest request, final String sourcename) {
        String agent = request.getHeader("User-Agent");
        String result;
        try {
            boolean isFireFox = (agent != null && agent.toLowerCase().contains("firefox"));
            if (isFireFox) {
                result = new String(sourcename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            } else {
                result = URLEncoder.encode(sourcename, "UTF-8");
                if ((agent != null && agent.contains(MICROSOFT_IE))) {
                    // see http://support.microsoft.com/default.aspx?kbid=816868
                    if (result.length() > IE_URL_LIMIT) {
                        // 根据request的locale 得出可能的编码
                        result = new String(result.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            log.error(">> encode filename[" + sourcename + "] error", e);
            result = sourcename;
        }
        return result;
    }

    /**
     * 根据ip获取详细地址
     */
    public static String getCityInfo(String ip) {
        if (ipLocal) {
            return "";
            //     return getLocalCityInfo(ip);
        } else {
            return getHttpCityInfo(ip);
        }
    }

    /**
     * 根据ip获取详细地址
     */
    public static String getHttpCityInfo(String ip) {
        String api = String.format(CloudConstant.Url.IP_URL, ip);
        cn.hutool.json.JSONObject object = JSONUtil.parseObj(HttpUtil.get(api));
        return object.get("addr", String.class);
    }

    /**
     * 根据ip获取详细地址uu
     */
    // public static String getLocalCityInfo(String ip) {
    //     try {
    //         DataBlock dataBlock = new DbSearcher(config, file.getPath())
    //                 .binarySearch(ip);
    //         String region = dataBlock.getRegion();
    //         String address = region.replace("0|", "");
    //         char symbol = '|';
    //         if (address.charAt(address.length() - 1) == symbol) {
    //             address = address.substring(0, address.length() - 1);
    //         }
    //         return address.equals(ElAdminConstant.REGION) ? "内网IP" : address;
    //     } catch (Exception e) {
    //         log.error(e.getMessage(), e);
    //     }
    //     return "";
    // }

    /**
     * 获取浏览器
     *
     * @param request
     * @return
     */
    public static String getBrowser(HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        Browser browser = userAgent.getBrowser();
        return browser.getName();
    }

    /**
     * 获取当前机器的IP
     *
     * @return /
     */
    public static String getLocalIp() {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements(); ) {
                NetworkInterface anInterface = interfaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration<InetAddress> inetAddresses = anInterface.getInetAddresses(); inetAddresses.hasMoreElements(); ) {
                    InetAddress inetAddr = inetAddresses.nextElement();
                    // 排除loopback类型地址
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr.getHostAddress();
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress.getHostAddress();
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                return "";
            }
            return jdkSuppliedAddress.getHostAddress();
        } catch (Exception e) {
            return "";
        }
    }
}
