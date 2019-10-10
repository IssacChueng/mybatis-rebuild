package cn.jeff.study.util;

import java.lang.reflect.Field;

/**
 * @author swzhang
 * @date 2019/09/27
 */
public class ReflectUtlis {

    public static <T> T getFieldObject(String name, Object obj) {
        Class<?> cls = obj.getClass();
        Field field = getField(name, cls);
        if (field == null) {
            return null;
        } else {
            return getObjectFromField(field, obj);
        }
    }

    public static Field getField(String name, Class<?> cls) {
        Field nameField = null;
        try {
            nameField = cls.getDeclaredField(name);
            return nameField;
        } catch (NoSuchFieldException e) {
            Class<?> superclass = cls.getSuperclass();
            if (superclass == null) {
                return null;
            }
            nameField = getField(name, superclass);
            return nameField;
        }
    }

    private static <T> T getObjectFromField(Field field, Object obj) {
        field.setAccessible(true);
        try {
            return (T) field.get(obj);
        } catch (IllegalAccessException e) {
            //ignore
        }
        return null;
    }
}
