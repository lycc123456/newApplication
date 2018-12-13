package com.lycc1.mibox.miboxtest.common;

import android.app.Application;
import android.content.Context;
import com.lycc1.mibox.miboxtest.util.DeviceUtils;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.model.HttpHeaders;
import com.zhouyou.http.model.HttpParams;

/**
 * @author lycc1
 * @name MiBoxTest
 * @classPackage com.lycc1.mibox.miboxtest.common
 * @time 2018/12/13 14:36
 * @class describe
 * @class 中文描述
 */
public class BaseApplication extends Application {

    private static BaseApplication _instence;

    public static BaseApplication getInstance() {
        return _instence;
    }

    public Context getAppContext() {
        return _instence.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initHttp();

    }

    private void initHttp() {

        //全局设置请求参数
        HttpParams params = new HttpParams();
        params.put("deviceID", DeviceUtils.getAndroidID());
        params.put("deviceFrom", "1");

        //全局设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.put("token", CommData.TOKEN);
        headers.put("deviceType", "Android");
        headers.put("userId", CommData.USERID);
        headers.put("appVersion", CommData.version);


        EasyHttp.getInstance()//默认初始化
                .setBaseUrl(Config.API_URL)
                .debug("Http == > ", true)
                .addCommonHeaders(headers)//设置全局公共请求头
                .addCommonParams(params);//设置全局公共参数
    }
}
