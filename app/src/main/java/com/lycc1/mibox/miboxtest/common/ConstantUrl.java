package com.lycc1.mibox.miboxtest.common;


/**
 * 接口声明
 * Created by house on 2017/6/26.
 */

public class ConstantUrl {

    /**
     * 车辆  盒子  控制接口
     * operation：
     * 1=鸣笛 2=设防 3=撤防 4=启动 5=重启盒子 6=断电 7=开仓锁 8=关仓锁 9=开轮锁 10=查询结果 11=更新盒子
     */
    public static final String COMMON_CAR_IMEI_OPERATION =  "/api/send/box";


    /**
     * 用户 发送验证码
     */
    public static final String SEND_MESSAGE = "/api/send/message";

    /**
     * 用户 登录
     */
    public static final String USER_LOGIN =  "/api/login";
    /**
     * 用户登录
     * V3.5 修改
     */
    public static final String USER_LOGIN_NEW =  "/api/v2/login";
    /**
     * 用户信息
     */
    public static final String USER_INFO =  "/api/user";


}
