package com.zq.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


/**
 * 字符串工具类
 *
 * @author wilmiam
 * @date 2015年10月29日
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    private static final char SEPARATOR = '_';

    /**
     * 对字符串做掩码
     *
     * @param str     字符串
     * @param overlay 掩码字符串
     * @param start   起始位置
     * @param end     结束位置
     * @return
     */
    public static String overlay(final String str, String overlay, int start, int end) {
        if (str == null) {
            return null;
        }
        if (overlay == null) {
            overlay = "";
        }
        final int len = str.length();
        if (start < 0) {
            start = 0;
        }
        if (start > len) {
            start = len;
        }
        if (end < 0) {
            end = 0;
        }
        if (end > len) {
            end = len;
        }
        if (start > end) {
            final int temp = start;
            start = end;
            end = temp;
        }
        return str.substring(0, start) +
                overlay +
                str.substring(end);
    }

    /**
     * 分割字符串
     *
     * @param strContent 字符串内容
     * @param strSplit   分隔符
     * @return 字符串数组
     */
    public static String[] splitString(final String strContent, final String strSplit) {
        if (!ValidateUtil.hasBlank(strContent, strSplit)) {
            if (!strContent.contains(strSplit)) {
                return new String[]{strContent};
            }
            return strContent.split(strSplit);
        }
        return new String[0];
    }

    /**
     * 将字符串分割成指定大小的数组
     *
     * @param strContent 字符串内容
     * @param strSplit   分隔符
     * @param count      指定数组大小
     * @return 字符串数组
     */
    public static String[] splitString(final String strContent, final String strSplit, final int count) {
        final String[] result = new String[count];

        final String[] splited = splitString(strContent, strSplit);

        for (int i = 0; i < count; i++) {
            if (i < splited.length) {
                result[i] = splited[0];
            } else {
                result[i] = null;
            }
        }
        return result;
    }

    /**
     * 将二进制数组转换成字符串
     *
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        StringBuffer sb = new StringBuffer();
        String tmpStr;
        for (byte value : b) {
            tmpStr = Integer.toHexString(value & 0xff);
            if (tmpStr.length() == 1) {
                sb.append(0).append(tmpStr);
            } else {
                sb.append(tmpStr);
            }
        }
        return sb.toString();
    }

    public static byte[] hex2byte(String hex) {
        byte[] ret = new byte[8];
        byte[] tmp = hex.getBytes();
        for (int i = 0; i < 8; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0}));
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1}));
        return (byte) (_b0 ^ _b1);
    }

    public static String substringBetween(final String str, final String open, final String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        final int start = str.indexOf(open);
        if (start != -1) {
            final int end = str.indexOf(close, start + open.length());
            if (end != -1) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }

    /**
     * 获取定长的字符串
     *
     * @param val
     * @param len
     * @return
     */
    public static String getFixedLengthSeq(String val, int len) {
        int valLen = val.length();
        if (valLen < len) {
            StringBuffer seq = new StringBuffer();
            for (int i = 0; i < len - valLen; i++) {
                seq.append("0");
            }
            seq.append(val);
            return seq.toString();
        } else {
            return val.substring(0, len);
        }
    }

    /**
     * 将数组转换成字符串
     *
     * @param array     数组
     * @param separator 分隔符
     * @return
     */
    public static String arrayToString(String[] array, String separator) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String str : array) {
            stringBuffer.append(str);
            stringBuffer.append(separator);
        }
        return stringBuffer.substring(0, stringBuffer.length() - separator.length());
    }

    /**
     * 将数组转换成字符串
     *
     * @param array 数组
     * @return
     */
    public static String arrayToString(String[] array) {
        return arrayToString(array, ",");
    }

    /**
     * 给数字左边补0
     *
     * @param number 数字
     * @param digit  获取得长度
     *               如：getNumberCut(1, 5) 返回 00001
     *               number 的位数大于 digit 自动截取number后digit位 如：getNumberCut(123456, 5)  返回23456
     * @return
     */
    public static String getNumberCut(int number, int digit) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumIntegerDigits(digit);
        nf.setMinimumIntegerDigits(digit);
        return nf.format(number);
    }

    /**
     * 字符串的压缩
     *
     * @param str 待压缩的字符串
     * @return 返回压缩后的字符串
     * @throws IOException
     */
    public static String compress(String str) throws IOException {
        if (null == str || str.length() <= 0) {
            return str;
        }
        // 创建一个新的 byte 数组输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 使用默认缓冲区大小创建新的输出流
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        // 将 b.length 个字节写入此输出流
        gzip.write(str.getBytes());
        gzip.close();
        // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
        return out.toString("ISO-8859-1");
    }

    /**
     * 字符串的解压
     *
     * @param str 对字符串解压
     * @return 返回解压缩后的字符串
     * @throws IOException
     */
    public static String unCompress(String str) throws IOException {
        if (null == str || str.length() <= 0) {
            return str;
        }
        // 创建一个新的 byte 数组输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 创建一个 ByteArrayInputStream，使用 buf 作为其缓冲区数组
        ByteArrayInputStream in = new ByteArrayInputStream(str
                .getBytes(StandardCharsets.ISO_8859_1));
        // 使用默认缓冲区大小创建新的输入流
        GZIPInputStream gzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n = 0;
        while ((n = gzip.read(buffer)) >= 0) {// 将未压缩数据读入字节数组
            // 将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此 byte数组输出流
            out.write(buffer, 0, n);
        }
        // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
        return out.toString("UTF-8");
    }

    /**
     * 校验String是否全是中文
     *
     * @param str 被校验的字符串
     * @return true代表全是汉字
     */
    public static boolean checkChese(String str) {
        boolean res = true;
        char[] cTemp = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {
            if (!isChinese(cTemp[i])) {
                res = false;
                break;
            }
        }
        return res;
    }

    /**
     * 判定输入的是否是汉字
     *
     * @param c 被校验的字符
     * @return true代表是汉字
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    /**
     * 检查字符串长度
     *
     * @param str       字符串
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @return 检查结果
     */
    public static boolean checkLength(final String str, final int minLength, final int maxLength) {
        if (maxLength < minLength) {
            return false;
        }

        if (ValidateUtil.isBlank(str)) {
            if (minLength == 0) {
                return true;
            }
            return false;
        }
        final int length = str.length();
        if (length >= minLength && length <= maxLength) {
            return true;
        }
        return false;
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    static String toUnderScoreCase(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }
}
