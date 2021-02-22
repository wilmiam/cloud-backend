package com.zq.common.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 基于apache httpclient的http请求帮助类
 *
 * @author wilmiam
 * @since 2019-03-14
 */
public final class HttpClientUtils {

    private static final Logger log = LoggerFactory.getLogger(HttpClientUtils.class);

    private static final int CONNECTION_TIMEOUT_MILLIS = 1000;
    private static final int SOCKET_TIMEOUT_MILLIS = 30000;

    private static final int[] SUCCESSFUL_STATUS = {
            HttpStatus.SC_OK,
            HttpStatus.SC_CREATED,
            HttpStatus.SC_ACCEPTED,
            HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION,
            HttpStatus.SC_NO_CONTENT,
            HttpStatus.SC_RESET_CONTENT,
            HttpStatus.SC_PARTIAL_CONTENT,
            HttpStatus.SC_MULTI_STATUS
    };

    private static CloseableHttpClient client;

    static {
        try {
            client = buildPoolingClient(100, 200, CONNECTION_TIMEOUT_MILLIS, SOCKET_TIMEOUT_MILLIS);
        } catch (Exception e) {
            log.error(">> 初始化httpclient失败", e);
        }
    }

    private HttpClientUtils() {
    }

    // ---------------------- starts of get request ----------------------------------

    /**
     * 发送一个http get请求
     *
     * @param url 请求url
     * @return
     */
    public static String doGet(String url) {
        return doGet(url, null);
    }

    /**
     * 发送一个http get请求
     *
     * @param url         请求url
     * @param queryParams 请求参数
     * @return
     */
    public static String doGet(String url, Map<String, String> queryParams) {
        return doGet(url, queryParams, null);
    }

    /**
     * 发送一个http get请求
     *
     * @param url         请求url
     * @param queryParams 请求参数
     * @param headers     请求头
     * @return
     */
    public static String doGet(String url, Map<String, String> queryParams, Map<String, String> headers) {
        return doGet(url, queryParams, headers, client);
    }

    /**
     * 发送一个http get请求
     *
     * @param url           请求url
     * @param queryParams   请求参数
     * @param headers       请求头
     * @param socketTimeout 请求超时时间(milliseconds)
     * @return
     */
    public static String doGet(String url, Map<String, String> queryParams, Map<String, String> headers, int socketTimeout) {
        if (socketTimeout <= 0) {
            throw new IllegalArgumentException("无效的socketTimeout,无法执行http get请求");
        }

        CloseableHttpClient httpClient = buildBasicClient(socketTimeout);
        return doGet(url, queryParams, headers, httpClient);
    }

    /**
     * 发送一个http get请求
     *
     * @param url         请求url
     * @param queryParams 请求参数
     * @param headers     请求头
     * @param httpClient  httpClient实例
     * @return
     */
    public static String doGet(String url, Map<String, String> queryParams, Map<String, String> headers, CloseableHttpClient httpClient) {
        return resp2string(url, get(url, queryParams, headers, httpClient));
    }

    public static CloseableHttpResponse get(String url) {
        return get(url, null, null, client);
    }

    public static CloseableHttpResponse get(String url, Map<String, String> queryParams) {
        return get(url, queryParams, null, client);
    }

    public static CloseableHttpResponse get(String url, Map<String, String> queryParams, Map<String, String> headers) {
        return get(url, queryParams, headers, client);
    }

    public static CloseableHttpResponse get(String url, Map<String, String> queryParams, Map<String, String> headers, CloseableHttpClient httpClient) {
        if (httpClient == null) {
            throw new IllegalArgumentException("httpClient未指定,无法执行http get请求");
        }
        HttpGet httpGet = new HttpGet(buildUri(url, queryParams));
        addHeaders(httpGet, headers);
        return executeRequest(url, httpGet, httpClient);
    }

    // ---------------------- end of get request ----------------------------------

    // ---------------------- start of post request -------------------------------

    /**
     * 发送http post请求
     *
     * @param url 请求url
     * @return
     */
    public static String doPost(String url) {
        return doPost(url, null);
    }

    /**
     * 发送http post请求
     *
     * @param url  请求url
     * @param body 请求的body内容
     * @return
     */
    public static String doPost(String url, String body) {
        return doPost(url, body, null);
    }

    /**
     * 发送http post请求
     *
     * @param url     请求url
     * @param body    请求的body内容
     * @param headers http headers
     * @return
     */
    public static String doPost(String url, String body, Map<String, String> headers) {
        return doPost(url, null, body, headers, client);
    }

    /**
     * 发送http post请求
     *
     * @param url           请求url
     * @param queryParams   请求的url参数
     * @param body          请求的body内容
     * @param headers       http headers
     * @param socketTimeout 请求timeout时间(milliseconds)
     * @return
     */
    public static String doPost(String url, Map<String, String> queryParams, String body, Map<String, String> headers, int socketTimeout) {
        return doPost(url, queryParams, body, headers, buildBasicClient(socketTimeout));
    }

    /**
     * 发送http post请求
     *
     * @param url         请求url
     * @param queryParams 请求的url参数
     * @param body        请求的body内容
     * @param headers     http headers
     * @return
     */
    public static String doPost(String url, Map<String, String> queryParams, String body, Map<String, String> headers, CloseableHttpClient httpClient) {
        return resp2string(url, post(url, queryParams, body, headers, httpClient));
    }

    public static CloseableHttpResponse post(String url) {
        return post(url, null, null, null, client);
    }

    public static CloseableHttpResponse post(String url, String body) {
        return post(url, null, body, null, client);
    }

    public static CloseableHttpResponse post(String url, String body, Map<String, String> headers) {
        return post(url, null, body, headers, client);
    }

    public static CloseableHttpResponse post(String url, Map<String, String> queryParams, String body, Map<String, String> headers) {
        return post(url, queryParams, body, headers, client);
    }

    public static CloseableHttpResponse post(String url, Map<String, String> queryParams, String body, Map<String, String> headers, CloseableHttpClient httpClient) {
        HttpPost httpPost = new HttpPost(buildUri(url, queryParams));
        addHeaders(httpPost, headers);
        if (body != null) {
            httpPost.setEntity(new StringEntity(body, "utf-8"));
        }
        return executeRequest(url, httpPost, httpClient);
    }

    // ---------------------- end of post request -------------------------------

    // ---------------------- start of json post request -------------------------------

    public static String doJsonPost(String url, String body) {
        Map<String, String> headers = new HashMap<>(2);
        headers.put("Content-Type", "application/json;charset=utf-8");
        return doPost(url, body, headers);
    }

    // ---------------------- end of json post request -------------------------------

    // ---------------------- start of form post request -------------------------------

    public static String doFormPost(String url) {
        return doFormPost(url, null);
    }

    /**
     * 发送form表单的POST请求
     *
     * @param url        请求url
     * @param formParams form表单参数
     * @return
     */
    public static String doFormPost(String url, Map<String, String> formParams) {
        return doFormPost(url, formParams, null);
    }

    /**
     * 发送form表单的POST请求
     *
     * @param url         请求url
     * @param formParams  form表单参数
     * @param queryParams 请求的url参数
     * @return
     */
    public static String doFormPost(String url, Map<String, String> formParams, Map<String, String> queryParams) {
        return doFormPost(url, formParams, queryParams, null);
    }

    /**
     * @param url         请求url
     * @param formParams  form表单参数
     * @param queryParams 请求的url参数
     * @param headers     http headers
     * @return
     */
    public static String doFormPost(String url, Map<String, String> formParams, Map<String, String> queryParams, Map<String, String> headers) {
        return resp2string(url, postForm(url, formParams, queryParams, headers));
    }

    public static CloseableHttpResponse postForm(String url) {
        return postForm(url, null, null, null);
    }

    public static CloseableHttpResponse postForm(String url, Map<String, String> formParams) {
        return postForm(url, formParams, null, null);
    }

    public static CloseableHttpResponse postForm(String url, Map<String, String> formParams, Map<String, String> queryParams) {
        return postForm(url, formParams, queryParams, null);
    }

    public static CloseableHttpResponse postForm(String url, Map<String, String> formParams, Map<String, String> queryParams, Map<String, String> headers) {
        HttpPost httpPost = new HttpPost(buildUri(url, queryParams));
        addHeaders(httpPost, headers);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        if (formParams != null && !formParams.isEmpty()) {
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(map2params(formParams), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("构建HTTP POST表单对象失败, url=" + url, e);
            }
        }
        return executeRequest(url, httpPost, client);
    }

    // ---------------------- end of form post request -------------------------------

    // ---------------------- start of mulitipart post request -------------------------------

    /**
     * 发送multipart-form表单的Post请求
     *
     * @param url        请求url
     * @param fileParams 文件请求参数
     * @return
     */
    public static String doMultiPartPost(String url, Map<String, File> fileParams) {
        return doMultiPartPost(url, null, fileParams);
    }

    /**
     * 发送multipart-form表单的Post请求
     *
     * @param url        请求url
     * @param params     请求参数
     * @param fileParams 文件请求参数
     * @return
     */
    public static String doMultiPartPost(String url, Map<String, String> params, Map<String, File> fileParams) {
        return doMultiPartPost(url, params, fileParams, null);
    }

    /**
     * 发送multipart-form表单的Post请求
     *
     * @param url        请求url
     * @param params     请求参数
     * @param fileParams 文件请求参数
     * @param headers    http headers
     * @return
     */
    public static String doMultiPartPost(String url, Map<String, String> params, Map<String, File> fileParams, Map<String, String> headers) {
        HttpPost httpPost = new HttpPost(url);
        addHeaders(httpPost, headers);
        try {
            httpPost.setEntity(buildFileMultipartEntity(params, fileParams));
        } catch (Exception e) {
            throw new RuntimeException("构建HTTP MULITIPART请求失败, url=" + url, e);
        }
        return request4string(url, httpPost);
    }

    /**
     * 发送multipart-form表单的Post请求
     *
     * @param url             请求url
     * @param fileBytesParams 文件请求参数
     * @return
     */
    public static String doBytesMultiPartPost(String url, Map<String, FileBytesPart> fileBytesParams) {
        return doBytesMultiPartPost(url, null, fileBytesParams);
    }

    /**
     * 发送multipart-form表单的Post请求
     *
     * @param url             请求url
     * @param params          请求参数
     * @param fileBytesParams 文件请求参数
     * @return
     */
    public static String doBytesMultiPartPost(String url, Map<String, String> params, Map<String, FileBytesPart> fileBytesParams) {
        return doBytesMultiPartPost(url, params, fileBytesParams, null);
    }

    /**
     * 发送multipart-form表单的Post请求
     *
     * @param url             请求url
     * @param params          请求参数
     * @param fileBytesParams 文件请求参数
     * @param headers         http headers
     * @return
     */
    public static String doBytesMultiPartPost(String url, Map<String, String> params, Map<String, FileBytesPart> fileBytesParams, Map<String, String> headers) {
        HttpPost httpPost = new HttpPost(url);
        addHeaders(httpPost, headers);
        try {
            httpPost.setEntity(buildBytesMultipartEntity(params, fileBytesParams));
        } catch (Exception e) {
            throw new RuntimeException("构建HTTP MULITIPART请求失败, url=" + url, e);
        }
        return request4string(url, httpPost);
    }

    /**
     * 发送multipart-form表单的Post请求
     *
     * @param url          请求url
     * @param streamParams 文件请求参数
     * @return
     */
    public static String doInputstreamMultiPartPost(String url, Map<String, FileInputStreamPart> streamParams) {
        return doInputstreamMultiPartPost(url, null, streamParams);
    }

    /**
     * 发送multipart-form表单的Post请求
     *
     * @param url          请求url
     * @param params       请求参数
     * @param streamParams 文件请求参数
     * @return
     */
    public static String doInputstreamMultiPartPost(String url, Map<String, String> params, Map<String, FileInputStreamPart> streamParams) {
        return doInputstreamMultiPartPost(url, params, streamParams, null);
    }

    /**
     * 发送multipart-form表单的Post请求
     *
     * @param url          请求url
     * @param params       请求参数
     * @param streamParams 文件请求参数
     * @param headers      http headers
     * @return
     */
    public static String doInputstreamMultiPartPost(String url, Map<String, String> params, Map<String, FileInputStreamPart> streamParams, Map<String, String> headers) {
        HttpPost httpPost = new HttpPost(url);
        addHeaders(httpPost, headers);
        try {
            httpPost.setEntity(buildInputstreamMultipartEntity(params, streamParams));
        } catch (Exception e) {
            throw new RuntimeException("构建HTTP MULITIPART请求失败, url=" + url, e);
        }
        return request4string(url, httpPost);
    }

    // ---------------------- end of multipart post request -------------------------------

    public static String request4string(String url, HttpRequestBase httpRequest) {
        return resp2string(url, executeRequest(url, httpRequest, client));
    }

    public static String request4string(String url, HttpRequestBase httpRequest, CloseableHttpClient httpClient) {
        return resp2string(url, executeRequest(url, httpRequest, httpClient));
    }

    public static CloseableHttpResponse executeRequest(String url, HttpRequestBase httpRequest) {
        return executeRequest(url, httpRequest, client);
    }

    /**
     * 执行http请求
     *
     * @param url
     * @param httpRequest
     * @param httpClient
     * @return
     */
    public static CloseableHttpResponse executeRequest(String url, HttpRequestBase httpRequest, CloseableHttpClient httpClient) {
        try {
            // 执行http请求
            long start = System.nanoTime();
            CloseableHttpResponse response = httpClient.execute(httpRequest);
            long end = System.nanoTime();

            if (log.isDebugEnabled()) {
                long spent = TimeUnit.NANOSECONDS.toMillis(end - start);
               log.debug(">> HTTP {} response {} in {}ms, url: {}", httpRequest.getMethod(), response.getStatusLine().getStatusCode(), spent, url);
            }
            return response;
        } catch (IOException e) {
            throw new RuntimeException("HTTP " + httpRequest.getMethod() + "请求失败, url: " + url, e);
        }
    }

    /**
     * 根据参数构建一个url
     *
     * @param url         url
     * @param queryParams url参数
     * @return
     */
    public static URI buildUri(String url, Map<String, String> queryParams) {
        URIBuilder uriBuilder;
        try {
            if (StringUtils.isNotBlank(url)) {
                uriBuilder = new URIBuilder(url);
            } else {
                uriBuilder = new URIBuilder();
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException("构建HTTP请求错误，无效的url: " + url);
        }

        if (queryParams != null && !queryParams.isEmpty()) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                uriBuilder.addParameter(entry.getKey(), entry.getValue());
            }
        }

        uriBuilder.setCharset(StandardCharsets.UTF_8);
        try {
            return uriBuilder.build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("构建HTTP请求错误, 无法根据参数构建URI, url: " + url);
        }
    }

    /**
     * 解析url, 获取url参数（注意：若url里包含数组参数请勿使用此方法）
     *
     * @param url
     * @param charset
     * @return
     */
    public static Map<String, String> parseUrlQueryParams(String url, String charset) {
        List<NameValuePair> pairs = URLEncodedUtils.parse(url, Charset.forName(charset));
        if (pairs == null || pairs.isEmpty()) {
            return new HashMap<>(1);
        }
        Map<String, String> params = new HashMap<>(pairs.size());
        for (NameValuePair pair : pairs) {
            params.put(pair.getName(), pair.getValue());
        }
        return params;
    }


    /**
     * 根据参数生成一个pooling http client实例
     *
     * @param maxPerRoute
     * @param maxTotal
     * @param connectionTimeout
     * @param socketTimeout
     * @return
     */
    public static CloseableHttpClient buildPoolingClient(int maxPerRoute, int maxTotal, int connectionTimeout, int socketTimeout) {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(trustAllFactoryRegistry());
        connectionManager.setDefaultMaxPerRoute(maxPerRoute);
        connectionManager.setMaxTotal(maxTotal);

        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectionTimeout)
                .setSocketTimeout(socketTimeout).build();

        return HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig).build();
    }

    /**
     * 根据参数生成一个http client实例
     *
     * @param socketTimeout
     * @return
     */
    public static CloseableHttpClient buildBasicClient(int socketTimeout) {
        BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(trustAllFactoryRegistry());
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).build();

        return HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig).build();
    }

    /**
     * 判断http响应码是否为一个2xx的请求成功响应码
     *
     * @param status
     * @return
     */
    public static boolean is2xxSuccessfulStatus(int status) {
        return Arrays.stream(SUCCESSFUL_STATUS).anyMatch(code -> code == status);
    }

    /**
     * 将响应内容转换为字符串，并判断响应是否为2xx成功的响应
     *
     * @param response 响应
     * @return
     */
    public static String resp2string(String url, CloseableHttpResponse response) {
        if (response == null) {
            return null;
        }
        // 将响应内容转换为字符串
        int status = response.getStatusLine().getStatusCode();
        String bodyString;
        try {
            bodyString = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException("读取响应内容为字符串失败, url: " + url, e);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                log.warn(">> error closing CloseableHttpResponse, url: {}", url, e);
            }
        }

        // 根据http code判断响应是否成功
        if (is2xxSuccessfulStatus(status)) {
            return bodyString;
        } else {
            throw new RuntimeException("请求失败, 响应码" + status + ", url: " + url + " [response body]: " + bodyString);
        }
    }

    private static HttpEntity buildInputstreamMultipartEntity(Map<String, String> params, Map<String, FileInputStreamPart> inputStreampParams) {
        try {
            MultipartEntityBuilder entityBuilder = initMultipartBuilder(params);
            if (inputStreampParams != null && !inputStreampParams.isEmpty()) {
                for (Map.Entry<String, FileInputStreamPart> entry : inputStreampParams.entrySet()) {
                    FileInputStreamPart insBody = entry.getValue();
                    entityBuilder.addBinaryBody(entry.getKey(), insBody.getData(), ContentType.DEFAULT_BINARY, insBody.getFilename());
                }
            }
            return entityBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException("构建HTTP请求错误，无法根据参数构建multipart/form-data请求", e);
        }
    }

    private static HttpEntity buildBytesMultipartEntity(Map<String, String> params, Map<String, FileBytesPart> byteFileParams) {
        try {
            MultipartEntityBuilder entityBuilder = initMultipartBuilder(params);
            if (byteFileParams != null && !byteFileParams.isEmpty()) {
                for (Map.Entry<String, FileBytesPart> entry : byteFileParams.entrySet()) {
                    FileBytesPart bytesBody = entry.getValue();
                    entityBuilder.addBinaryBody(entry.getKey(), bytesBody.getData(), ContentType.DEFAULT_BINARY, bytesBody.getFilename());
                }
            }
            return entityBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException("构建HTTP请求错误，无法根据参数构建multipart/form-data请求", e);
        }
    }

    private static HttpEntity buildFileMultipartEntity(Map<String, String> params, Map<String, File> fileParams) {
        try {
            MultipartEntityBuilder entityBuilder = initMultipartBuilder(params);
            if (fileParams != null && !fileParams.isEmpty()) {
                for (Map.Entry<String, File> entry : fileParams.entrySet()) {
                    entityBuilder.addBinaryBody(entry.getKey(), entry.getValue());
                }
            }
            return entityBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException("构建HTTP请求错误，无法根据参数构建multipart/form-data请求", e);
        }
    }

    private static MultipartEntityBuilder initMultipartBuilder(Map<String, String> params) {
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create()
                .setCharset(Consts.UTF_8)
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                entityBuilder.addPart(entry.getKey(), new StringBody(entry.getValue(), ContentType.create("text/plain", Consts.UTF_8)));
            }
        }
        return entityBuilder;
    }

    private static List<NameValuePair> map2params(Map<String, String> map) {
        List<NameValuePair> params = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return params;
    }

    private static void addHeaders(HttpRequestBase method, Map<String, String> headers) {
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                method.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    private static Registry<ConnectionSocketFactory> trustAllFactoryRegistry() {
        // setup a Trust Strategy that allows all certificates.
        try {
            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(null, ((chain, authType) -> true))
                    .build();
            // don't check Hostnames, either.
            //      -- use SSLConnectionSocketFactory.getDefaultHostnameVerifier(), if you don't want to weaken

            // create an SSL Socket Factory, to use our weakened "trust strategy"
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

            return RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslsf)
                    .build();
        } catch (GeneralSecurityException e) {
            log.error(">> 初始化SSLContext出错", e);
            throw new RuntimeException("初始化http请求失败");
        }
    }

}
