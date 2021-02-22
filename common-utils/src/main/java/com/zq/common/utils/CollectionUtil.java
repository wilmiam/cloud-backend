package com.zq.common.utils;

import java.util.Collection;

/**
 * 列表操作类
 *
 * @author wilmiam
 * @date 2015年10月29日
 */
public class CollectionUtil {

    /**
     * 判断对象是否在集合里
     *
     * @param searchObj       对象
     * @param list            集合
     * @param caseInsensetive 忽略大小写
     * @return 判断结果
     */
    public static boolean inCollection(final Object searchObj, final Collection<Object> list,
                                       final boolean caseInsensetive) {
        return CollectionUtil.getInCollectionId(searchObj, list, caseInsensetive) >= 0;
    }

    /**
     * 判断对象是否在集合里
     *
     * @param searchObj 对象
     * @param list      集合
     * @return 判断结果
     */
    public static boolean inCollection(final Object searchObj, final Collection<Object> list) {
        return inCollection(searchObj, list, false);
    }

    /**
     * 判断指定字符串是否属于指定字符串数组中的一个元素
     *
     * @param strSearch   字符串
     * @param stringArray 内部以逗号分割单词的字符串
     * @return 判断结果
     */
    public static boolean inArray(final String strSearch, final String stringArray) {
        return inArray(strSearch, StringUtils.splitString(stringArray, ","));
    }

    /**
     * 判断指定字符串是否属于指定字符串数组中的一个元素
     *
     * @param strSearch   字符串
     * @param stringArray 内部分割单词的字符串
     * @param strSplit    分割字符串
     * @return 判断结果
     */
    public static boolean inArray(final String strSearch, final String stringArray, final String strSplit) {
        return inArray(strSearch, StringUtils.splitString(stringArray, strSplit));
    }

    /**
     * 判断指定字符串是否属于指定字符串数组中的一个元素
     *
     * @param strSearch       字符串
     * @param stringArray     内部以逗号分割单词的字符串
     * @param strsplit        分割字符串
     * @param caseInsensetive 是否不区分大小写, true为不区分, false为区分
     * @return 判断结果
     */
    public static boolean inArray(final String strSearch, final String stringArray, final String strsplit,
                                  final boolean caseInsensetive) {
        return inArray(strSearch, StringUtils.splitString(stringArray, strsplit), caseInsensetive);
    }

    /**
     * 判断制定字符串是否属于制定字符串数组中的一个元素
     *
     * @param strSearch   字符串
     * @param stringArray 字符串数组
     * @return 判断结果
     */
    public static boolean inArray(final String strSearch, final String[] stringArray) {
        return inArray(strSearch, stringArray, false);
    }

    /**
     * 判断制定字符串是否属于制定字符串数组中的一个元素
     *
     * @param strSearch       字符串
     * @param stringArray     字符串数组
     * @param caseInsensetive 是否不区分大小写, true为不区分, false为区分
     * @return 判断结果
     */
    public static boolean inArray(final String strSearch, final String[] stringArray, final boolean caseInsensetive) {
        return getInArrayId(strSearch, stringArray, caseInsensetive) >= 0;
    }

    /**
     * 判断指定字符串在指定字符串数组中的位置
     *
     * @param searchStr       字符串
     * @param strArray        字符串数组
     * @param caseInsensetice 是否不区分大小写, true为不区分, false为区分
     * @return 字符串在指定字符串数组中的位置, 如不存在则返回-1
     */
    public static int getInArrayId(final String searchStr, final String[] strArray, final boolean caseInsensetice) {
        if (!ValidateUtil.isBlank(searchStr)) {
            for (int i = 0; i < strArray.length; i++) {
                final String tmpStr = strArray[i];
                if (!ValidateUtil.isBlank(tmpStr)) {
                    if (caseInsensetice) {
                        if (searchStr.toLowerCase().equals(strArray[i].toLowerCase())) {
                            return i;
                        }
                    } else {
                        if (searchStr.equals(strArray[i])) {
                            return i;
                        }
                    }
                }
            }
            return -1;
        }
        return -1;
    }

    /**
     * 判断指定字符串在指定字符串数组中的位置
     *
     * @param searchStr 字符串
     * @param strArray  字符串数组
     * @return 字符串在指定字符串数组中的位置, 如不存在则返回-1
     */
    public static int getInArrayId(final String searchStr, final String[] strArray) {
        return getInArrayId(searchStr, strArray, false);
    }

    /**
     * 查询对象在集合里的索引
     *
     * @param searchObj       对象
     * @param list            集合列表
     * @param caseInsensetice 区分大小写
     * @return 集合索引
     */
    public static int getInCollectionId(final Object searchObj, final Collection<Object> list,
                                        final boolean caseInsensetice) {
        if (!ValidateUtil.isBlank(searchObj)) {
            int index = 0;
            for (Object tempObj : list) {
                if (caseInsensetice && searchObj instanceof String && tempObj instanceof String) {
                    if (((String) searchObj).equalsIgnoreCase((String) tempObj)) {
                        return index;
                    }
                } else {
                    if (searchObj.equals(tempObj)) {
                        return index;
                    }
                }
                index++;
            }
        }
        return -1;
    }

    /**
     * 查询对象在集合里的索引
     *
     * @param searchObj 对象
     * @param list      集合列表
     * @return 对象在集合中的索引
     */
    public static int getInCollectionId(final Object searchObj, final Collection<Object> list) {
        return getInCollectionId(searchObj, list, false);
    }
}
