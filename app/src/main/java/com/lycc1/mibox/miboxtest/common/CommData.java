package com.lycc1.mibox.miboxtest.common;

/**
 * 缓存数据
 * Created by house on 2017/6/26.
 */
public class CommData {
    // token
    public static String TOKEN;
    public static String version;

    // 编号
    public static String ID;
    // 用户id
    public static String USERID;

    public static void clear() {
        TOKEN = "";
        ID = "";
        USERID = "";
    }
}
