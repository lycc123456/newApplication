package com.lycc1.mibox.miboxtest.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * Created by house on 2017/10/17.
 */

public class AppManager {
    private static AppManager manager;
    private Stack<Activity> activityStack;

    private AppManager() {
    }

    //单例
    public static AppManager getInstance() {

        if (manager == null) {
            synchronized (AppManager.class) {
                if (manager == null) {
                    manager = new AppManager();
                }
            }
        }
        return manager;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        if (activity != null && !activityStack.contains(activity))
            activityStack.add(activity);
    }


    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        int size = activityStack.size();
        for (int i = size - 1; i >= 0  ; i--) {
            if (activityStack.get(i).getClass().equals(cls)) {
                finishActivity(activityStack.get(i));
                return;
            }
        }
    }

    /**
     * 关闭指定数量的Activity
     *
     * @param count 数量
     */
    public void finishActivityByCount(int count) {
        for (int i = activityStack.size() - 1; i > 0; i++) {
            if (count > 0) {
                finishActivity(activityStack.get(i));
                count--;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack == null || activityStack.empty()) {
            return;
        }
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
