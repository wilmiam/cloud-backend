package com.zq.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * jackson操作json的帮助类
 *
 * @author wilmiam
 * @since 2018-01-02
 */
public final class JacksonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // 日期序列化为long
        OBJECT_MAPPER.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 反序列化时, 忽略不认识的字段, 而不是抛出异常
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    private JacksonUtils() {
    }

    /**
     * 仅读取整个json中的某个属性值,若该属性是一个json对象,则返回空字符串""
     *
     * @param jsonString json string
     * @param fieldName  field name
     * @return the field value
     */
    public static String readField(String jsonString, String fieldName) {
        try {
            JsonNode root = OBJECT_MAPPER.readTree(jsonString);
            JsonNode node = root.get(fieldName);
            return node == null ? null : node.asText();
        } catch (IOException e) {
            throw new RuntimeException("error reading json field", e);
        }
    }

    /**
     * 将json字符串转换为指定类型的java对象
     *
     * @param jsonString
     * @param clazz
     * @return
     */
    public static <T> T json2pojo(String jsonString, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(jsonString, clazz);
        } catch (IOException e) {
            throw new RuntimeException("error transfomer json to pojo", e);
        }
    }

    /**
     * 将json字符串转换为指定类型的java对象, 此方法用于目标类包含泛型的java对象.
     * <br/>
     * Example:<br/>
     * <code>    JacksonUtils.json2pojo("...", new TypeReference&lt;PageResult&lt;User&gt;&gt;(){});</code>
     *
     * @param jsonString
     * @param typeReference
     * @return
     */
    public static <T> T json2pojo(String jsonString, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(jsonString, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("error transfomer json to pojo", e);
        }
    }

    /**
     * 将json字符串转换为HashMap(json里的子对象也将转换为Map)
     *
     * @param jsonString
     * @return
     */
    public static Map<String, Object> json2map(String jsonString) {
        try {
            return OBJECT_MAPPER.readValue(jsonString, new TypeReference<HashMap<String, Object>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("error transfomer json to map", e);
        }
    }

    /**
     * 将java对象转换为json字符串
     *
     * @param pojo
     * @return
     */
    public static String pojo2json(Object pojo) {
        try {
            return OBJECT_MAPPER.writeValueAsString(pojo);
        } catch (IOException e) {
            throw new RuntimeException("error transfomer pojo to json", e);
        }
    }

    /**
     * 将java对象转换为map对象
     *
     * @param pojo
     * @return
     */
    public static Map<String, Object> pojo2map(Object pojo) {
        return json2map(pojo2json(pojo));
    }

}
