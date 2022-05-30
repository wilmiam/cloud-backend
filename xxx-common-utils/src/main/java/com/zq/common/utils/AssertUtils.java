package com.zq.common.utils;

import cn.hutool.core.util.StrUtil;
import com.zq.common.exception.BusinessException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * 断言验证帮助类
 *
 * @author wilmiam
 * @since 2018-04-03
 */
public final class AssertUtils {

    private static final String[] IMG_EXTS = {"png", "jpg", "jpeg"};

    /**
     * Don't let anyone instantiate this class
     */
    private AssertUtils() {
    }

    /**
     * 判断给定的文件名后缀是否为图片
     *
     * @param ext    文件名后缀, 不带点
     * @param errMsg 错误信息
     */
    public static void isImgExt(String ext, String errMsg) {
        isImgExt(ext, 400, errMsg);
    }

    /**
     * 判断给定的文件名后缀是否为图片
     *
     * @param ext     文件名后缀, 不带点
     * @param errCode 断言失败的错误代码
     * @param errMsg  错误信息
     */
    public static void isImgExt(String ext, int errCode, String errMsg) {
        if (StrUtil.isBlank(ext) || Arrays.stream(IMG_EXTS).noneMatch(img -> img.equalsIgnoreCase(ext))) {
            throw new BusinessException(errCode, errMsg);
        }
    }

    /**
     * UnifiedExceptionHandler
     * 判断一个布尔表达式, 若表达式为{@code true}则抛出指定错误信息的{@code BusinessException}.
     *
     * @param expression 布尔表达式
     * @param message    断言失败时的错误信息
     * @throws BusinessException
     */
    public static void notTrue(boolean expression, String message) throws BusinessException {
        notTrue(expression, 400, message);
    }

    /**
     * 判断一个布尔表达式, 若表达式为{@code true}则抛出指定错误信息的{@code BusinessException}.
     *
     * @param expression 布尔表达式
     * @param errCode    断言失败时的错误代码
     * @param message    断言失败时的错误信息
     * @throws BusinessException
     */
    public static void notTrue(boolean expression, int errCode, String message) throws BusinessException {
        if (expression) {
            throw new BusinessException(errCode, message);
        }
    }

    /**
     * 判断一个布尔表达式, 若表达式为{@code false}则抛出指定错误信息的{@code BusinessException}.
     *
     * @param expression 布尔表达式
     * @param message    断言失败时的错误信息
     * @throws BusinessException
     */
    public static void isTrue(boolean expression, String message) throws BusinessException {
        isTrue(expression, 400, message);
    }

    /**
     * 判断一个布尔表达式, 若表达式为{@code false}则抛出指定错误信息的{@code BusinessException}.
     *
     * @param expression 布尔表达式
     * @param errCode    断言失败时的错误代码
     * @param message    断言失败时的错误信息
     * @throws BusinessException
     */
    public static void isTrue(boolean expression, int errCode, String message) throws BusinessException {
        if (!expression) {
            throw new BusinessException(errCode, message);
        }
    }

    /**
     * 如果对象为{@code null}, 则抛出异常
     *
     * @param object 要判断的对象
     * @throws BusinessException
     */
    public static void notNull(Object object) throws BusinessException {
        notNull(object, "不能处理空对象");
    }

    /**
     * 如果对象为{@code null}, 则抛出异常
     *
     * @param object  要判断的对象
     * @param message 断言失败时的错误信息
     * @throws BusinessException
     */
    public static void notNull(Object object, String message) throws BusinessException {
        notNull(object, 400, message);
    }

    /**
     * 如果对象为{@code null}, 则抛出异常
     *
     * @param object  要判断的对象
     * @param errCode 断言失败时的错误代码
     * @param errMsg  断言失败时的错误信息
     * @throws BusinessException
     */
    public static void notNull(Object object, int errCode, String errMsg) throws BusinessException {
        if (object == null) {
            throw new BusinessException(errCode, errMsg);
        }
    }

    /**
     * 如果字符串为{@code null}、空字符串或仅包含空白字符, 则抛出异常
     *
     * @param text 要进行检查的字符串
     * @throws BusinessException
     */
    public static void hasText(String text) throws BusinessException {
        hasText(text, 400, "参数不能为空字符串");
    }

    /**
     * 如果字符串为{@code null}、空字符串或仅包含空白字符, 则抛出异常
     *
     * @param text    要进行检查的字符串
     * @param message 断言失败时的错误信息
     * @throws BusinessException
     */
    public static void hasText(String text, String message) throws BusinessException {
        hasText(text, 400, message);
    }

    /**
     * 如果字符串为{@code null}、空字符串或仅包含空白字符, 则抛出异常
     *
     * @param text    要进行检查的字符串
     * @param errCode 断言失败时的错误代码
     * @param errMsg  错误信息
     * @throws BusinessException
     */
    public static void hasText(String text, int errCode, String errMsg) throws BusinessException {
        if (StrUtil.isBlank(text)) {
            throw new BusinessException(errCode, errMsg);
        }
    }

    /**
     * 如果数组为{@code null}或长度为0, 则抛出异常
     *
     * @param array   要进行检查的数组
     * @param message 断言失败时的错误信息
     * @param <T>     数组的数据类型
     * @throws BusinessException
     */
    public static <T> void notEmpty(T[] array, String message) throws BusinessException {
        notEmpty(array, 400, message);
    }

    /**
     * 如果数组为{@code null}或长度为0, 则抛出异常
     *
     * @param array   要进行检查的数组
     * @param errCode 断言失败时的错误代码
     * @param errMsg  错误信息
     * @param <T>     数组的数据类型
     * @throws BusinessException
     */
    public static <T> void notEmpty(T[] array, int errCode, String errMsg) throws BusinessException {
        if (array == null || array.length == 0) {
            throw new BusinessException(errCode, errMsg);
        }
    }

    /**
     * 如果数组里包含有{@code null}的元素, 则抛出异常. 注意: 若数组本身为{@code null}则不会进行处理, 直接返回
     *
     * @param array   要进行检查的数组
     * @param message 断言失败时的错误信息
     * @param <T>     数组的数据类型
     * @throws BusinessException
     */
    public static <T> void noNullElements(T[] array, String message) throws BusinessException {
        noNullElements(array, 400, message);
    }

    /**
     * 如果数组里包含有{@code null}的元素, 则抛出异常. 注意: 若数组本身为{@code null}则不会进行处理, 直接返回
     *
     * @param array   要进行检查的数组
     * @param errCode 断言失败时的错误代码
     * @param errMsg  错误信息
     * @param <T>     数组的数据类型
     * @throws BusinessException
     */
    public static <T> void noNullElements(T[] array, int errCode, String errMsg) throws BusinessException {
        if (array != null) {
            for (T element : array) {
                if (element == null) {
                    throw new BusinessException(errCode, errMsg);
                }
            }
        }
    }

    /**
     * 如果集合为{@code null},或者不包含任何元素,则抛出异常
     *
     * @param collection 要进行检查的集合
     * @param message    断言失败时的错误信息
     * @throws BusinessException
     */
    public static void notEmpty(Collection<?> collection, String message) throws BusinessException {
        notEmpty(collection, 400, message);
    }

    /**
     * 如果集合为{@code null},或者不包含任何元素,则抛出异常
     *
     * @param collection 要进行检查的集合
     * @param errCode    断言失败时的错误代码
     * @param errMsg     错误信息
     * @throws BusinessException
     */
    public static void notEmpty(Collection<?> collection, int errCode, String errMsg) throws BusinessException {
        if (collection == null || collection.isEmpty()) {
            throw new BusinessException(errCode, errMsg);
        }
    }

    /**
     * 如果键值对为{@code null},或者不包含任何键值,则抛出异常
     *
     * @param map     要进行检查的键值对
     * @param message 断言失败时的错误信息
     * @throws BusinessException
     */
    public static void notEmpty(Map<?, ?> map, String message) throws BusinessException {
        notEmpty(map, 400, message);
    }

    /**
     * 如果键值对为{@code null},或者不包含任何键值,则抛出异常
     *
     * @param map     要进行检查的键值对
     * @param errCode 断言失败时的错误代码
     * @param errMsg  错误信息
     * @throws BusinessException
     */
    public static void notEmpty(Map<?, ?> map, int errCode, String errMsg) throws BusinessException {
        if (map == null || map.isEmpty()) {
            throw new BusinessException(errCode, errMsg);
        }
    }

}
