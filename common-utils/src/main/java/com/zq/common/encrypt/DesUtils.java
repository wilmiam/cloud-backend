package com.zq.common.encrypt;

import com.zq.common.exception.ServerErrorException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author wilmiam
 * @since 2013-11-03
 */
public class DesUtils {

    private static final Logger log = LoggerFactory.getLogger(DesUtils.class);

    private static final String CHARSET = "UTF-8";

    private static byte[] desEncrypt(byte[] plainText, byte[] rawKeyData) throws GeneralSecurityException {
        return getDesCipher(Cipher.ENCRYPT_MODE, rawKeyData).doFinal(plainText);
    }

    private static byte[] desDecrypt(byte[] encryptText, byte[] rawKeyData) throws GeneralSecurityException {
        return getDesCipher(Cipher.DECRYPT_MODE, rawKeyData).doFinal(encryptText);
    }

    private static Cipher getDesCipher(int mode, byte[] rawKeyData) throws GeneralSecurityException {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(rawKeyData);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(mode, key, sr);
        return cipher;
    }

    public static String encrypt(String input, String key) {
        try {
            return Base64.getEncoder().encodeToString(desEncrypt(input.getBytes(CHARSET), key.getBytes(CHARSET)));
        } catch (GeneralSecurityException | IOException e) {
            log.error(">> DES encrypt error", e);
            throw new ServerErrorException("解密失败");
        }
    }

    public static String decrypt(String input, String key) {
        try {
            byte[] result = Base64.getDecoder().decode(input);
            return new String(desDecrypt(result, key.getBytes(CHARSET)), CHARSET);
        } catch (GeneralSecurityException | IOException e) {
            log.error(">> DES decrypt error", e);
            throw new ServerErrorException("解密失败");
        }
    }

    /**
     * 对使用.net的 System.Security.Cryptography.DESCryptoServiceProvider 类进行DES加密的字符串进行解密
     *
     * @param message
     * @param key
     * @return
     */
    public static String dotNetDecrypt(String message, String key) {
        if (StringUtils.isBlank(message)) {
            return message;
        }
        try {
            byte[] bytesrc = Base64.getDecoder().decode(message);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(StandardCharsets.UTF_8));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes(StandardCharsets.UTF_8));

            // 使用秘钥和IV向量初始化加密算法
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

            byte[] retByte = cipher.doFinal(bytesrc);
            return new String(retByte);
        } catch (GeneralSecurityException e) {
            log.error(">> DES decrypt error", e);
            throw new RuntimeException("解密失败");
        }
    }

    public static byte[] dotNetEncrypt(String message, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(StandardCharsets.UTF_8));

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

        return cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
    }

}
