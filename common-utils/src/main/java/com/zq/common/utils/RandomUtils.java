package com.zq.common.utils;

public class RandomUtils {

    /**
     * 随机睡眠时间，单位秒
     *
     * @param min
     * @param max
     */
    public static void threadSleepSeconds(double min, double max) {
        try {
            min = min * 1000;
            max = max * 1000;
            Thread.sleep((long) (min + Math.random() * (max - min + 1)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 随机睡眠时间，单位分
     *
     * @param min
     * @param max
     */
    public static void threadSleepMinutes(double min, double max) {
        try {
            min = min * 60 * 1000;
            max = max * 60 * 1000;
            Thread.sleep((long) (min + Math.random() * (max - min + 1)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定范围的数字字符串
     *
     * @param min
     * @param max
     * @return
     */
    public static Integer getInteger(int min, int max) {
        return (int) (min + Math.random() * (max - min + 1));
    }

}
