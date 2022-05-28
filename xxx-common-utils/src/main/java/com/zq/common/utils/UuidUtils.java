package com.zq.common.utils;

import java.util.UUID;

/**
 * Utility class that handles the uuid stuff.
 *
 * @author wilmiam
 * @since 2021-07-09 18:05
 */
public class UuidUtils {

    private static final String URN_UUID_REGEX = "^[a-f0-9]{8}(-[a-f0-9]{4}){3}-[a-f0-9]{12}$";
    private static final String NODASH_UUID_REGEX = "^[a-f0-9]{32}$";

    /**
     * Don't let anyone instantiate this class
     */
    private UuidUtils() {
    }

    /**
     * 返回一个随机的带有分隔符"-"的36位UUID字符串
     *
     * @return
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 返回一个随机的没有分隔符"-"的32位UUID字符串
     *
     * @return
     */
    public static String uuidNoDash() {
        return uuid().replaceAll("-", "");
    }

    /**
     * 判断一个字符串是否是一个UUID字符串
     *
     * @param str 要进行判断的字符串
     * @return
     */
    public static boolean isUuid(String str) {
        return ValidateUtil.isNotBlank(str) && str.matches(URN_UUID_REGEX);
    }

    /**
     * 判断一个字符串是否是一个没有分隔符的uuid字符串
     *
     * @param str 要进行判断的字符串
     * @return
     */
    public static boolean isNoDashUuid(String str) {
        return ValidateUtil.isNotBlank(str) && str.matches(NODASH_UUID_REGEX);
    }

}
