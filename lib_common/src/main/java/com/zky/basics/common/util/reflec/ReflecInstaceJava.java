package com.zky.basics.common.util.reflec;

/**
 * Created by lk
 * Date 2020/8/16
 * Time 09:23
 * Detail:
 */
public class ReflecInstaceJava {
    /**
     * Kotlin 无参实例化
     *
     * @param tClass class
     * @param <T>    类型
     * @return T 对象
     */
    public static <T> T instaceOf(Class<T> tClass) {
        if (tClass == null) {
            return null;
        }
        T instance = null;
        try {
            instance = (T) tClass.getClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }
}
