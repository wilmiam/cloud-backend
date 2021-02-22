package com.zq.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bernix Ning
 * @since 2019-03-19
 */
public class ParamMapBuilder {

    /**
     * The largest power of two that can be represented as an {@code int}.
     *
     * @since 10.0
     */
    private static final int MAX_POWER_OF_TWO = 1 << (Integer.SIZE - 2);

    private Map<String, Object> map;

    private ParamMapBuilder(int expectedSize) {
        map = new HashMap<>(capacity(expectedSize));
    }

    public static ParamMapBuilder create() {
        return create(16);
    }

    public static ParamMapBuilder create(int size) {
        return new ParamMapBuilder(size);
    }

    public ParamMapBuilder put(String key, Object value) {
        map.put(key, value);
        return this;
    }

    public Map<String, Object> build() {
        return map;
    }

    /**
     * Returns a capacity that is sufficient to keep the map from being resized as
     * long as it grows no larger than expectedSize and the load factor is >= its
     * default (0.75).
     */
    public static int capacity(int expectedSize) {
        if (expectedSize < 3) {
            if (expectedSize <= 0) {
                throw new RuntimeException("无效的map size: " + expectedSize);
            }
            return expectedSize + 1;
        }
        if (expectedSize < MAX_POWER_OF_TWO) {
            // This is the calculation used in JDK8 to resize when a putAll
            // happens; it seems to be the most conservative calculation we
            // can make.  0.75 is the default load factor.
            return (int) ((float) expectedSize / 0.75F + 1.0F);
        }
        // any large value
        return Integer.MAX_VALUE;
    }
}
