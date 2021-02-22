package com.zq.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 产生随机字符串的帮助类
 * <p>
 * org.apache.commons:commons-lang3提供了类RandomStringUtils来产生随机字符串，但是默认使用的是Random, 其产生的随机值会有时序性，
 * 容易受到时序攻击(timing attack)。这里使用SecureRandom来避免时序问题。
 * </p>
 *
 * @author wilmiam
 * @since 2018-04-13
 */
public class SecureRandomStringUtils {

    private static final Logger log = LoggerFactory.getLogger(RandomStringUtils.class);

    private static final String ALPHABETIC_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMERIC_CHARS = "0123456789";
    private static final String DEFAULT_CHARS = ALPHABETIC_CHARS + NUMERIC_CHARS;

    /**
     * An instance of secure random to ensure randomness is secure.
     */
    private static final SecureRandom RANDOMIZER = getNativeInstance();

    /**
     * 随机对象帮助类
     * Get strong enough SecureRandom instance and of the checked exception.
     * TODO Try {@code NativePRNGNonBlocking} and failover to default SHA1PRNG until Java 9.
     *
     * @return the strong instance
     */
    public static SecureRandom getNativeInstance() {
        try {
            return SecureRandom.getInstance("NativePRNGNonBlocking");
        } catch (final NoSuchAlgorithmException e) {
            log.warn(e.toString());
            return new SecureRandom();
        }
    }

    /**
     * 生成指定长度的随机字符串,仅包含数字
     *
     * @param length 随机字符串长度
     * @return
     */
    public static String randomNumeric(int length) {
        return random(length, NUMERIC_CHARS);
    }

    /**
     * 生成指定长度的随机字符串,可能包含小写字母和大写字母
     *
     * @param length 随机字符串长度
     * @return
     */
    public static String randomAlphabetic(int length) {
        return random(length, ALPHABETIC_CHARS);
    }

    /**
     * 生成指定长度的随机字符串,可能包含小写字母+大写字母+数字
     *
     * @param length 随机字符串长度
     * @return
     */
    public static String randomAlphanumeric(int length) {
        return random(length, DEFAULT_CHARS);
    }

    /**
     * 根据指定的字符全集,生成指定长度的随机字符串
     *
     * @param length 随机字符串长度
     * @param chars  随机字符全集
     * @return
     */
    public static String random(int length, char[] chars) {
        if (chars == null || chars.length == 0) {
            throw new IllegalArgumentException("未指定字符全集,无法生成随机字符串");
        }
        return random(length, new String(chars));
    }

    /**
     * 根据指定的字符全集,生成指定长度的随机字符串
     *
     * @param length 随机字符串长度
     * @param chars  随机字符全集
     * @return
     */
    public static String random(int length, String chars) {
        if (length <= 0) {
            throw new IllegalArgumentException("无效的随机字符串长度: " + length);
        }
        if (ValidateUtil.isBlank(chars)) {
            throw new IllegalArgumentException("未指定字符全集,无法生成随机字符串");
        }
        return IntStream.range(0, length)
                .map(i -> RANDOMIZER.nextInt(chars.length()))
                .mapToObj(randomInt -> chars.substring(randomInt, randomInt + 1))
                .collect(Collectors.joining());
    }

    /**
     * 生成一个指定长度的的随机字节数组,并转换为16进制的字符串
     *
     * @param size 字节数组长度
     * @return
     */
    public static String randomHexString(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("无效的随机字符串长度: " + size);
        }
        try {
            final char[] hexChars = Hex.encodeHex(randomStringBytes(size));
            return String.valueOf(hexChars);
        } catch (final Exception e) {
            log.warn(">> 生成随机16进制字符串失败", e);
            return null;
        }
    }

    /**
     * 生成一个指定长度的的随机字节数组,并转换为base64 url encode的字符串
     *
     * @param size 字节数组长度
     * @return
     */
    public static String randomBase64UrlSafedString(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("无效的随机字符串长度: " + size);
        }
        return Base64.encodeBase64URLSafeString(randomStringBytes(size));
    }

    private static byte[] randomStringBytes(int size) {
        final byte[] randomBytes = new byte[size];
        RANDOMIZER.nextBytes(randomBytes);
        return randomBytes;
    }

}
