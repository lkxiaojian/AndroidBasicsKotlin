package com.zky.basics.common.util;

/**
 * Created by lk
 * Date 2019-11-20
 * Time 18:30
 * Detail:
 */
public class DataUtils {
    /**
     * 根据文件路径获取文件的名称
     *
     * @param path
     * @return
     */
    public static String getNameByPath(String path) {
        if (InfoVerify.isEmpty(path)) {
            return "";
        }
        String[] list = path.split("/");
        return list[list.length-1];

    }
}
