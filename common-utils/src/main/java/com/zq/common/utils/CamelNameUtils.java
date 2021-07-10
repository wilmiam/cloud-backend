package com.zq.common.utils;

/**
 * 驼峰命名工具类
 *
 * @author wilmiam
 * @since 2021-07-09 18:04
 */
public class CamelNameUtils {

    private static final char SEPARATOR = '_';

    /**
     * 驼峰命名法工具
     *
     * @return "hello_world" == "helloWorld"
     */
    public static String toCamelCase(String name) {
        if (name == null) {
            return null;
        }

        name = name.toLowerCase();

        StringBuilder sb = new StringBuilder(name.length());
        boolean upperCase = false;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);

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
     * @return "hello_world" == "HelloWorld"
     */
    public static String toCapitalizeCamelCase(String name) {
        if (name == null) {
            return null;
        }
        name = toCamelCase(name);
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * 驼峰命名法工具
     *
     * @return "helloWorld" = "hello_world"
     */
    static String toUnderScoreCase(String name) {
        if (name == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);

            boolean nextUpperCase = true;

            if (i < (name.length() - 1)) {
                nextUpperCase = Character.isUpperCase(name.charAt(i + 1));
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
