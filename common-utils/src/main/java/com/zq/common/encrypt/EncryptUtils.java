package com.zq.common.encrypt;

import com.zq.common.exception.ServerErrorException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.KeyGenerator;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

/**
 * 加密工具类
 *
 * @author wilmiam
 * @since 2013-11-04
 */
public class EncryptUtils {

    private static final Logger log = LoggerFactory.getLogger(EncryptUtils.class);

    /**
     * 字符编码
     */
    private static final String CHARSET_NAME = "UTF-8";

    /**
     * 用来将字节转换成 16 进制表示的字符
     */
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * authcode密钥长度
     */
    private static final int CRYPT_KEY_LENGTH = 255;

    private static final int CRYPT_KEY_OFFSET = 256;

    private static final int HEX_RADIUS = 16;

    /**
     * MD5 加密字符串
     *
     * @param sourceStr
     * @return
     */
    public static String md5Encrypt(final String sourceStr) {
        return md5Encrypt(sourceStr, CHARSET_NAME);
    }

    /**
     * MD5加密字符串
     *
     * @param sourceStr 原始
     * @return 加密之后字符串
     */
    public static String md5Encrypt(final String sourceStr, String coding) {
        if (StringUtils.isBlank(sourceStr)) {
            return null;
        }

        byte[] sourceByte;
        try {
            sourceByte = sourceStr.getBytes(coding);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            sourceByte = sourceStr.getBytes();
        }

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(sourceByte);
            // MD5 的计算结果是一个 128 位的长整数，用字节表示就是 16 个字节
            final byte[] tmp = md.digest();

            // 每个字节用 16 进制表示的话，使用两个字符，所以表示成 16 进制需要 32 个字符
            // 16 << 1 相当于 16*2
            final char[] str = new char[16 << 1];

            // 表示转换结果中对应的字符位置
            int k = 0;

            // 从第一个字节开始，对 MD5 的每一个字节转换成 16 进制字符的转换
            for (int i = 0; i < HEX_RADIUS; i++) {
                // 取第 i 个字节
                final byte byte0 = tmp[i];

                // 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移
                str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];

                // 取字节中低 4 位的数字转换
                str[k++] = HEX_DIGITS[byte0 & 0xf];
            }
            // 换后的结果转换为字符串
            return new String(str);
        } catch (final NoSuchAlgorithmException e) {
            log.error(">> MD5加密错误", e);
        }

        return null;
    }


    /**
     * DES加密字符串
     *
     * @param sourceStr 原文字符串
     * @param keyStr    密钥字符串
     * @return
     */
    public static String desEncode(final String sourceStr, final String keyStr) {
        return DesUtils.encrypt(sourceStr, keyStr);
    }


    /**
     * DES解密
     *
     * @param sourceStr 原文字符串
     * @param keyStr    密钥字符串
     * @return
     */
    public static String desDecode(final String sourceStr, final String keyStr) {
        return DesUtils.decrypt(sourceStr, keyStr);
    }

    public static Key getDesKey(String keyStr) {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("DesUtils");
            generator.init(new SecureRandom(keyStr.getBytes(CHARSET_NAME)));
            return generator.generateKey();
        } catch (Exception e) {
            log.error("getDesKey error", e.getMessage());
            return null;
        }
    }

    /**
     * 创建RSA私钥/公钥对
     */
    public static Map<String, String> getRsaKey() {
        try {
            Map<String, Key> keyMap = RsaUtils.genKeyPair();
            Map<String, String> key = new HashMap<String, String>(2);
            String publicKey = RsaUtils.getPublicKey(keyMap);
            String privateKey = RsaUtils.getPrivateKey(keyMap);
            key.put("publicKey", publicKey);
            key.put("privateKey", privateKey);

            return key;
        } catch (Exception e) {
            log.error("getRsaKey error", e);
            return null;
        }
    }


    /**
     * 公钥加密(注意：加密后base64加密了)
     *
     * @param source
     * @param publicKey
     * @return
     */
    public static String rsaEncryptByPublicKey(String source, String publicKey) {
        try {
            byte[] data = source.getBytes(CHARSET_NAME);
            byte[] encodedData = RsaUtils.encryptByPublicKey(data, publicKey);
            return Base64.getEncoder().encodeToString(encodedData);
        } catch (Exception e) {
            log.error("rsaEncryptByPublicKey error", e);
            return null;
        }
    }

    /**
     * 私钥解密(注意：encodedData是base64加密后的才行)
     *
     * @param encodedData
     * @param privateKey
     * @return
     */
    public static String rsaDecodeByPrivateKey(String encodedData, String privateKey) {
        try {
            byte[] decodedData = RsaUtils.decryptByPrivateKey(Base64.getDecoder().decode(encodedData), privateKey);
            return new String(decodedData, CHARSET_NAME);
        } catch (Exception e) {
            log.error("rsaDecodeByPrivateKey error", e);
            return null;
        }
    }

    /**
     * BASE64加密字符串
     *
     * @param sourceStr 原始字符串
     * @return 加密后的字符串
     */
    public static String base64Encode(final String sourceStr) {
        try {
            byte[] data = sourceStr.getBytes(CHARSET_NAME);
            return Base64.getEncoder().encodeToString(data);
        } catch (UnsupportedEncodingException e) {
            log.error(">> base64 encode error", e);
            throw new ServerErrorException("编码失败");
        }
    }

    /**
     * 使用BASE64进行解密
     *
     * @param base64Str base64字符串
     * @return 解密字符串
     */
    public static String base64Decode(final String base64Str) {
        byte[] decode = Base64.getDecoder().decode(base64Str);
        try {
            return new String(decode, CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            log.error("base64 decode error", e);
            throw new ServerErrorException("编码失败");
        }
    }

    /**
     * SHA1加密
     *
     * @param sourceStr 原文
     * @return
     */
    public static String sha1Encrypt(final String sourceStr) {
        if (StringUtils.isBlank(sourceStr)) {
            return "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(sourceStr.getBytes(CHARSET_NAME));
            byte[] hash = digest.digest();
            return byteArrayToHexString(hash);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            log.error("sha1Encrypt error", e);
        }
        return "";
    }

    /**
     * SHA加密
     *
     * @param sourceStr 原文
     * @return
     */
    public static String shaEncrypt(final String sourceStr) {
        if (StringUtils.isBlank(sourceStr)) {
            return "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA");
            digest.reset();
            digest.update(sourceStr.getBytes(CHARSET_NAME));
            byte[] hash = digest.digest();
            return byteArrayToHexString(hash);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            log.error("shaEncrypt error", e);
        }
        return "";
    }

    /**
     * 对字符串进行URL编码
     *
     * @param sourceStr 需要编码的字符串
     * @param enc       编码格式
     * @return
     */
    public static String urlEncode(String sourceStr, String enc) {
        try {
            return URLEncoder.encode(sourceStr, enc);
        } catch (UnsupportedEncodingException e) {
            log.error("urlEncode error", e);
            return null;
        }
    }

    /**
     * 对字符串进行URL编码
     *
     * @param sourceStr 需要编码的字符串
     * @param enc       编码格式
     * @return
     */
    public static String urlDecode(String sourceStr, String enc) {
        try {
            return URLDecoder.decode(sourceStr, enc);
        } catch (UnsupportedEncodingException e) {
            log.error("urlDecode error", e);
            return null;
        }
    }

    /**
     * authcode解密
     *
     * @param string 密文
     * @param key    密钥
     * @return
     * @throws Exception
     */
    public static String authCodeDecode(String string, String key) {
        //随机密钥长度 取值 0-32;
        // 加入随机密钥，可以令密文无任何规律，即便是原文和密钥完全相同，加密结果也会每次不同，增大破解难度。（实际上就是iv）
        // 取值越大，密文变动规律越大，密文变化 = 16 的 $ckey_length 次方
        // 当此值为 0 时，则不产生随机密钥
        final int ckeyLength = 4;

        //密匙
        key = md5Encrypt(key);
        String keya = md5Encrypt(key.substring(0, 16));
        String keyb = md5Encrypt(key.substring(16, 32));
        String keyc = string.substring(0, ckeyLength);

        String cryptkey = keya + md5Encrypt(keya + keyc);
        int cryptkeyLen = cryptkey.length();

        try {
            string = new String(Base64.getDecoder().decode(string.substring(ckeyLength)), CHARSET_NAME);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        int stringLen = string.length();

        List<Integer> rndkey = new ArrayList<>();
        StringBuilder result = new StringBuilder();
        Integer[] box = new Integer[CRYPT_KEY_OFFSET];
        for (int i = 0; i < box.length; i++) {
            box[i] = i;
        }

        for (int i = 0; i <= CRYPT_KEY_LENGTH; i++) {
            rndkey.add((int) cryptkey.charAt(i % cryptkeyLen));
        }

        for (int j = 0, i = 0; i < CRYPT_KEY_OFFSET; i++) {
            j = (j + box[i] + rndkey.get(i)) % 256;
            int tmp = box[i];
            box[i] = box[j];
            box[j] = tmp;
        }

        for (int k = 0, j = 0, i = 0; i < stringLen; i++) {
            k = (k + 1) % 256;
            j = (j + box[k]) % 256;
            int tmp = box[k];
            box[k] = box[j];
            box[j] = tmp;
            int a = (int) string.charAt(i);
            int b = box[(box[k] + box[j]) % 256];
            char r = (char) (a ^ b);
            result.append(r);
        }

        String secString = result.toString();
        String substringBegin = secString.substring(0, 10);

        boolean hasBeginTime = Integer.parseInt(substringBegin) == 0;
        boolean beginBeforeNow = Integer.parseInt(substringBegin) - System.currentTimeMillis() / 1000 > 0;

        boolean notExpire = hasBeginTime || beginBeforeNow;

        String sign = (md5Encrypt(secString.substring(26) + keyb)).substring(0, 16);
        boolean validSign = secString.substring(10, 26).equals(sign);

        return (notExpire && validSign) ? secString.substring(26) : "";
    }


    /**
     * authCode加密方式
     *
     * @param string 元字符串
     * @param key    密钥
     * @param expiry
     * @return
     * @throws Exception
     */
    public static String authCodeEncode(String string, String key, int expiry) {
        //随机密钥长度 取值 0-32;
        // 加入随机密钥，可以令密文无任何规律，即便是原文和密钥完全相同，加密结果也会每次不同，增大破解难度。（实际上就是iv）
        // 取值越大，密文变动规律越大，密文变化 = 16 的 $ckey_length 次方
        // 当此值为 0 时，则不产生随机密钥
        final int ckeyLength = 4;

        //密匙
        key = md5Encrypt(key);
        String keya = md5Encrypt(key.substring(0, 16));
        String keyb = md5Encrypt(key.substring(16));
        String keyc = md5Encrypt(String.valueOf(System.currentTimeMillis() / 1000)).substring((32 - ckeyLength));

        String cryptkey = keya + md5Encrypt(keya + keyc);
        int cryptkeyLen = cryptkey.length();

        //sprintf('%010d', $expiry ? $expiry + time() : 0).substr(md5($string.$keyb), 0, 16).$string
        string = String.format("%010d", expiry > 0 ? expiry + System.currentTimeMillis() / 1000 : 0) + md5Encrypt(string + keyb).substring(0, 16) + string;
        int stringLen = string.length();

        List<Integer> rndkey = new ArrayList<>();
        StringBuilder result = new StringBuilder();
        Integer[] box = new Integer[CRYPT_KEY_OFFSET];
        for (int i = 0; i < box.length; i++) {
            box[i] = i;
        }

        for (int i = 0; i <= CRYPT_KEY_LENGTH; i++) {
            rndkey.add((int) cryptkey.charAt(i % cryptkeyLen));
        }

        for (int j = 0, i = 0; i < CRYPT_KEY_OFFSET; i++) {
            j = (j + box[i] + rndkey.get(i)) % CRYPT_KEY_OFFSET;
            int tmp = box[i];
            box[i] = box[j];
            box[j] = tmp;
        }

        for (int k = 0, j = 0, i = 0; i < stringLen; i++) {
            k = (k + 1) % CRYPT_KEY_OFFSET;
            j = (j + box[k]) % CRYPT_KEY_OFFSET;
            int tmp = box[k];
            box[k] = box[j];
            box[j] = tmp;
            int a = (int) string.charAt(i);
            int b = box[(box[k] + box[j]) % CRYPT_KEY_OFFSET];
            char r = (char) (a ^ b);
            result.append(r);
        }

        byte[] bytes;
        try {
            bytes = result.toString().getBytes(CHARSET_NAME);
            return keyc + Base64.getEncoder().encodeToString(bytes);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String byteArrayToHexString(final byte[] digest) {
        StringBuilder sb = new StringBuilder();
        for (byte digestTemp : digest) {
            String shaHex = Integer.toHexString(digestTemp & 0xFF);
            if (shaHex.length() < 2) {
                sb.append(0);
            }
            sb.append(shaHex);
        }
        return sb.toString();
    }

    public static byte[] hexStringToByteArray(String sourceString) {
        byte[] digest = new byte[sourceString.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = sourceString.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }

        return digest;
    }

    public static String aesEncode(String content, String keyBytes) {
        return AesUtil.aesEncode(content, keyBytes);
    }

    public static String aesDecode(String content, String keyBytes) {
        return AesUtil.aesDecode(content, keyBytes);
    }

}
