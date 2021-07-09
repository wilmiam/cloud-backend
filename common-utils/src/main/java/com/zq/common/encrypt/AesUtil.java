package com.zq.common.encrypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @author wilmiam
 * @since 2021-07-09 17:55
 */
public class AesUtil {

    private static final Logger log = LoggerFactory.getLogger(AesUtil.class);
    private static final String ALGORITHM_STRING = "AES/ECB/PKCS5Padding";

    /**
     * 字符编码
     */
    private static final String CHARSET_NAME = "UTF-8";

    private static SecretKeySpec getSecretKeySpec(String password) throws Exception {
        byte[] rByte;
        if (password != null) {
            rByte = password.getBytes(CHARSET_NAME);
        } else {
            rByte = new byte[24];
        }
        return new SecretKeySpec(rByte, "AES");
    }

    /**
     * @param content
     * @param password 必须是16位长度
     * @return
     */
    private static byte[] encrypt(String content, String password) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_STRING);
            byte[] byteContent = content.getBytes(CHARSET_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(password));
            return cipher.doFinal(byteContent);
        } catch (Exception e) {
            log.error("encrypt error", e);
        }
        return null;
    }

    /**
     * @param content
     * @param password 必须是16位长度
     * @return
     */
    private static byte[] decrypt(byte[] content, String password) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_STRING);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(password));
            return cipher.doFinal(content);
        } catch (Exception e) {
            log.error("decrypt error", e);
        }
        return null;
    }

    /**
     * 加密
     */
    public static String aesEncode(String content, String keyBytes) {
        String key = EncryptUtils.md5Encrypt(keyBytes).substring(8, 24);
        return Base64.getEncoder().encodeToString((encrypt(content, key)));
    }

    /**
     * 解密
     */
    public static String aesDecode(String content, String keyBytes) {
        String key = EncryptUtils.md5Encrypt(keyBytes).substring(8, 24);
        byte[] b = decrypt(Base64.getDecoder().decode(content), key);
        if (b == null) {
            return null;
        }
        try {
            return new String(b, CHARSET_NAME);
        } catch (Exception e) {
            log.error("aesDecode error", e);
            return null;
        }
    }

}
