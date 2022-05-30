package com.zq.common.encrypt;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wilmiam
 * @since 2021-07-09 17:56
 */
public class RsaUtils {

    /**
     * 获取公钥的key
     */
    public static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 获取私钥的key
     */
    public static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 加密算法RSA
     */
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    public static String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMTaoTuj4LU9WAMeaVWcpwgyMcdvAA3JRDcG0+pWG086c+WPdSggNNZaVw3szCKOTnWvNc6SoqjpjpbQpC57uag67VzKWLmsZoF6SXjCARyRaEkfK2VRHTfkVpyd8FF16gebVhhyjbkkja9JVEekwqOGzfmnfSKfx5LwvcSxdiSrAgMBAAECgYBZgGHQQPk4zhRHDrurnhbfhhrV5yTqH7kxH5yYLeAqzJPHKsuEm+gKEXcFMMW7bGJF5YycSFVGYTJgZapQLBbDlrZdM8SjxsNyrCKI3v3LNQDsqs5x751HfFVvTme7wroN/uJszUaQJPagEUckMkHvpv7XWoL3Wbz7oy94T3ENoQJBAPAhj2yo9jRZv5JRlYy5BFwqYpxSWqGjzr2k2YiGqB9/y/pDpDx3q42FaBcOlOOeh/My+iVNLcezqgj+U0yx79ECQQDR3Oz9ckCm2q7AMCLFmp9cs4dws6DLim35awOvLIXtm/Z1tRNyuLqb6g2VM4O/QiTu64F3+ljKiOWHAcgxqUe7AkEArTuYy4vs6gFhCb6fg8Cp24+cSifDSF7zM67sW+jA+tBoJ+iKYDD46wS1/gQ/9yGT9Cfve998ylfbr9dB4s9vMQJAOH/uHd3gogtF+N/8vI6AUQjUcfcqVyIRsZCqEUM/W1Ud6VqyvbQWKVu+BGk2EwvPvbMRzCdOOFja0pocN6KHeQJAQPwlDo1IHJI5F60CvfIG8dIwtGexMnd4NNHQ4KH0peK9jUCPkkpW0No5ZEtKNgfdPk23erfyx5cGqocvnoUpoQ==";
    public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDE2qE7o+C1PVgDHmlVnKcIMjHHbwANyUQ3BtPqVhtPOnPlj3UoIDTWWlcN7Mwijk51rzXOkqKo6Y6W0KQue7moOu1cyli5rGaBekl4wgEckWhJHytlUR035FacnfBRdeoHm1YYco25JI2vSVRHpMKjhs35p30in8eS8L3EsXYkqwIDAQAB";

    /**
     * 生成密钥对(公钥和私钥)
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Key> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Key> keyMap = new HashMap<>(4);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    /**
     * 校验数字签名
     *
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.getDecoder().decode(sign));
    }

    /**
     * <p>
     * 公钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param publicKey     公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
        return byteTransfer(getCipherByRsaPublicKey(Cipher.DECRYPT_MODE, publicKey), encryptedData, MAX_ENCRYPT_BLOCK);
    }

    /**
     * <p>
     * 公钥加密
     * </p>
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        return byteTransfer(getCipherByRsaPublicKey(Cipher.ENCRYPT_MODE, publicKey), data, MAX_ENCRYPT_BLOCK);
    }

    private static Cipher getCipherByRsaPublicKey(int mode, String publicKey) throws GeneralSecurityException {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        return cipher;
    }

    /**
     * <P>
     * 私钥解密
     * </p>
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
        return byteTransfer(getCipherByRsaPrivateKey(Cipher.DECRYPT_MODE, privateKey), encryptedData, MAX_DECRYPT_BLOCK);
    }

    /**
     * 私钥加密
     *
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        return byteTransfer(getCipherByRsaPrivateKey(Cipher.ENCRYPT_MODE, privateKey), data, MAX_ENCRYPT_BLOCK);
    }

    private static Cipher getCipherByRsaPrivateKey(int mode, String key) throws GeneralSecurityException {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(mode, privateK);
        return cipher;
    }

    /**
     * <p>
     * 获取私钥
     * </p>
     *
     * @param keyMap 密钥对
     * @return
     */
    public static String getPrivateKey(Map<String, Key> keyMap) {
        Key key = keyMap.get(PRIVATE_KEY);
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * 获取公钥
     *
     * @param keyMap 密钥对
     * @return
     */
    public static String getPublicKey(Map<String, Key> keyMap) {
        Key key = keyMap.get(PUBLIC_KEY);
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * 根据cipher的模式, 对数据进行分段加密/解密
     *
     * @param cipher
     * @param data
     * @return
     * @throws GeneralSecurityException,IOException
     */
    private static byte[] byteTransfer(Cipher cipher, byte[] data, int maxBlock) throws GeneralSecurityException, IOException {
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 根据cipher的模式, 对数据分段加密/解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxBlock) {
                cache = cipher.doFinal(data, offSet, maxBlock);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxBlock;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

}
