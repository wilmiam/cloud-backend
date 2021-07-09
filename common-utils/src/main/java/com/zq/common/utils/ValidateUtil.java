package com.zq.common.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证工具类
 *
 * @author wilmiam
 * @since 2021-07-09 18:05
 */
public class ValidateUtil {

    /**
     * URL验证正则表达式
     */
    private static final String URL_REGEX = "^(http|https)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-Z0-9\\.&%\\$\\-]+)*@)*((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|localhost|([a-zA-Z0-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{1,10}))(\\:[0-9]+)*(/($|[a-zA-Z0-9\\.\\,\\?\\'\\\\\\+&%\\$#\\=~_\\-]+))*$";

    /**
     * 安全SQL验证正则表达式
     */
    private static final String SAFE_SQL_REGEX = "[-|;|,|\\/|\\(|\\)|\\[|\\]|\\}|\\{|%|@|\\*|!|\\']";

    /**
     * 身份证验证加权因子数组，将前17位加权因子保存在数组里
     */
    private static final int[] IDCARD_WI = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    /**
     * 身份证验证校验码数组，这是除以11后，可能产生的11位余数、验证码，也保存
     */
    private static final int[] IDCARD_Y = new int[]{1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2};

    /**
     * 身份证验证，最后一位为X时，校验码所在数组的位置
     */
    private static final int IDCARD_MOD_X = 2;

    /**
     * 身份证验证，当校验码为10时，最后一位必须是X
     */
    private static final String IDCARD_LAST_X = "X";

    /**
     * 身份证校验正则表达式
     */
    private static final String IDCARD_REGEX = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";

    /**
     * 身份证号码长度
     */
    private static final int IDCARD_LENG = 18;

    /**
     * 单个IP的正则表达式
     */
    private static final String SINGLE_IP_REGEX = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

    /**
     * 以逗号分隔的多个IP的正则表达式
     */
    private static final String IP_REGEX = SINGLE_IP_REGEX + "(," + SINGLE_IP_REGEX + ")*";

    private static final Class<?>[] NUMBER_TYPES = {
            Byte.class, Short.class, Integer.class, Long.class, BigInteger.class, Float.class, Double.class, BigDecimal.class};


    /**
     * 判断对象是否为空
     *
     * @param obj 要判断的对象
     * @return 判断结果
     */
    public static boolean isBlank(final Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof String) {
            return "".equals(((String) obj).trim());
        }

        if (obj instanceof Collection) {
            final Collection collection = (Collection) obj;
            return collection.isEmpty();
        }

        return false;
    }

    /**
     * 判断对象是否不为空
     *
     * @param obj
     * @return
     */
    public static boolean isNotBlank(final Object obj) {
        return !isBlank(obj);
    }

    /**
     * 检查参数是否有空
     *
     * @param objects 待验证对象，可以是多个
     * @return 验证结果
     */
    public static boolean hasBlank(final Object... objects) {
        for (final Object obj : objects) {
            if (isBlank(obj)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Base64字符串
     *
     * @param str Base64字符串
     * @return 判断结果
     */
    public static boolean isBase64(final String str) {
        return isMatch(str, "[A-Za-z0-9\\+\\/\\=]");
    }

    /**
     * 判断是否是Email地址字符串
     *
     * @param strEmail URL地址字符串
     * @return 判断结果
     */
    public static boolean isEmail(final String strEmail) {
        return isMatch(strEmail, "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$");
    }

    /**
     * 验证手机号码正确性
     *
     * @param strMobilePhone 手机号码
     * @return
     */
    public static boolean isMobilePhoneNo(final String strMobilePhone) {
        return isMatch(strMobilePhone, "^0?(13[0-9]|14[5-9]|15[012356789]|166|17[0-8]|18[0-9]|19[8-9])[0-9]{8}$");
    }

    /**
     * 是否为手机号码
     *
     * @param phone
     * @return
     */
    public static boolean isInternationalPhoneNo(final String phone) {
        return isMatch(phone, "^[0-9]{4,14}$");
    }

    /**
     * 判断是否为正整数字符串
     *
     * @param intStr 数字字符串
     * @return 判断结果
     */
    public static boolean isInt(final String intStr) {
        return isMatch(intStr, "^[0-9]*$");
    }

    /**
     * 判断是否为IP字符串
     *
     * @param ipStr ip字符串
     * @return 判断结果
     */
    public static boolean isIp(final String ipStr) {
        return isMatch(ipStr, "^((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)$");
    }

    /**
     * 判断字符串是否和正则表达式相匹配,大小写敏感
     *
     * @param str   字符串
     * @param regEx 正则表达式
     * @return 判断结果
     */
    public static boolean isMatch(final String str, final String regEx) {
        return isMatch(str, regEx, false);
    }

    /**
     * 判断字符串是否和正则表达式相匹配
     *
     * @param str             字符串
     * @param regEx           正则表达式
     * @param caseInsensetive 是否不区分大小写, true为不区分, false为区分
     * @return 判断结果
     */
    public static boolean isMatch(final String str, final String regEx, final boolean caseInsensetive) {
        if (ValidateUtil.isNotBlank(str) && ValidateUtil.isNotBlank(regEx)) {
            Pattern pattern;
            if (caseInsensetive) {
                pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
            } else {
                pattern = Pattern.compile(regEx);
            }
            final Matcher matcher = pattern.matcher(str);
            return matcher.find();
        }
        return false;
    }

    /**
     * 判断是否是数字
     *
     * @param strNumber 数字字符串
     * @return 判断结果
     */
    public static boolean isNumber(final String strNumber) {
        return isMatch(strNumber, "^\\d+$");
    }

    /**
     * 判断是否是Sql危险字符
     *
     * @param sqlStr sql字符串
     * @return 判断结果
     */
    public static boolean isSafeSqlString(final String sqlStr) {
        return isMatch(sqlStr, SAFE_SQL_REGEX);
    }

    /**
     * 判断是否是URL地址字符串
     *
     * @param strUrl URL地址字符串
     * @return 判断结果
     */
    public static boolean isURL(final String strUrl) {
        return isMatch(strUrl, URL_REGEX);
    }

    /**
     * 检查对象类型是不是Number类型
     *
     * @param type
     * @return
     */
    public static boolean isNumber(Class<?> type) {
        // Class为final类,其equals不能重写,而Class的equals方法是直接继承自Object类
        //     return (this == obj)
        // 所以在比较Class是否相等时,使用equals和使用==是一样的
        return Arrays.stream(NUMBER_TYPES).anyMatch(numberType -> numberType.equals(type));
    }

    /**
     * 身份证15位编码规则：dddddd yymmdd xx p
     * dddddd：6位地区编码
     * yymmdd: 出生年(两位年)月日，如：910215
     * xx: 顺序编码，系统产生，无法确定
     * p: 性别，奇数为男，偶数为女
     * <p>
     * 身份证18位编码规则：dddddd yyyymmdd xxx y
     * dddddd：6位地区编码
     * yyyymmdd: 出生年(四位年)月日，如：19910215
     * xxx：顺序编码，系统产生，无法确定，奇数为男，偶数为女
     * y: 校验码，该位数值可通过前17位计算获得
     * <p>
     * 前17位号码加权因子为 Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ]
     * 验证位 Y = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ]
     * 如果验证码恰好是10，为了保证身份证是十八位，那么第十八位将用X来代替
     * 校验位计算公式：Y_P = mod( ∑(Ai×Wi),11 )
     * i为身份证号码1...17 位; Y_P为校验码Y所在校验码数组位置
     */
    public static boolean isIdCardNo(final String idCardNo) {
        if (!isMatch(idCardNo, IDCARD_REGEX)) {
            return false;
        }

        if (idCardNo.length() == IDCARD_LENG) {
            // 用来保存前17位各自乖以加权因子后的总和
            int idCardWiSum = 0;
            for (int i = 0; i < IDCARD_WI.length; i++) {
                idCardWiSum += Integer.parseInt(idCardNo.substring(i, i + 1)) * IDCARD_WI[i];
            }

            // 计算出校验码所在数组的位置
            int idCardMod = idCardWiSum % 11;

            // 得到最后一位身份证号码
            String idCardLast = idCardNo.substring(17);
            if (idCardMod == IDCARD_MOD_X) {
                return IDCARD_LAST_X.equalsIgnoreCase(idCardLast);
            } else {
                //用计算出的验证码与最后一位身份证号码匹配，如果一致，说明通过，否则是无效的身份证号码
                return String.valueOf(IDCARD_Y[idCardMod]).equals(idCardLast);
            }
        }

        return true;
    }

    /**
     * 判断字符串是否是以逗号分隔的IP字符串。
     * <p>
     * 逗号必须是英文半角, 单个ip也认为符合要求, 空字符串返回false
     * </p>
     *
     * @param ipStr
     * @return
     */
    public static boolean isComaSplitIp(String ipStr) {
        boolean result = false;
        if (ValidateUtil.isNotBlank(ipStr)) {
            result = ipStr.matches(IP_REGEX);
        }
        return result;
    }
}
