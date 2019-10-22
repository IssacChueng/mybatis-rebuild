package cn.jeff.study.util;

/**
 * @author swzhang
 * @date 2019/10/22
 */
public class MixUtils {
    public static <T> T valueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }
}
