package com.zq.common.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author wilmiam
 * @since 2019-04-15
 */
public class ImageUtils {

    /**
     * 有效的图片文件格式
     */
    private static final Set<String> FILE_EXTENSIONS = new HashSet<>();

    static {
        FILE_EXTENSIONS.add("bmp");
        FILE_EXTENSIONS.add("jpg");
        FILE_EXTENSIONS.add("png");
        FILE_EXTENSIONS.add("jpeg");
    }

    /**
     * 获取文件的后缀(格式), 如: xxxx.txt.png，则返回png
     *
     * @param filename
     * @return
     */
    public static String getFileExt(String filename) {
        if (ValidateUtil.isBlank(filename) || !filename.contains(".")) {
            return null;
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }

    /**
     * 判断文件名是否为有效的图片文件格式
     *
     * @param filename
     * @return
     */
    public static boolean isImgFile(String filename) {
        return isImgFileExt(getFileExt(filename));
    }

    /**
     * 判断是否为图片文件后缀名
     *
     * @param ext
     * @return
     */
    public static boolean isImgFileExt(final String ext) {
        return ext != null && FILE_EXTENSIONS.stream()
                .anyMatch(item -> item.equals(ext.toLowerCase()));
    }
}
