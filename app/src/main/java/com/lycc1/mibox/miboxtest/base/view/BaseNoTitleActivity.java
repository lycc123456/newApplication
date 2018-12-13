package com.lycc1.mibox.miboxtest.base.view;


import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.lycc1.mibox.miboxtest.util.AppManager;
import com.lycc1.mibox.miboxtest.util.ScreenManager;

/**
 * Created by house on 2017/6/27.
 */

public abstract class BaseNoTitleActivity extends AppCompatActivity {

    private LinearLayout parentLayout;
    private ConstraintLayout mContentLayout;

    private Handler mHandler;

    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        return mHandler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenManager.getScreenManager().pushActivity(this);
//        Window window = getWindow();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            // Translucent status bar
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
//        StatusBarUtils.setMeizuStatusBarDarkIcon(this, true);
//        StatusBarUtils.setMiuiStatusBarDarkMode(this, true);

        // 设置强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getDelegate().setContentView(R.layout.base_no_title_activity);

        setStatusBar(true);

        mContentLayout = findViewById(R.id.contentLayout);
        parentLayout = findViewById(R.id.parentLayout);

        onActivityCreate(savedInstanceState);

        AppManager.getInstance().addActivity(this);
    }

    public void setStatusBar(boolean lightStatusBar){
        StatusBarCompat.setStatusBarColor(this, Color.TRANSPARENT, lightStatusBar);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
    }

    protected abstract void onActivityCreate(Bundle savedInstanceState);

    public <T extends View> T findView(int viewId) {
        return (T) mContentLayout.findViewById(viewId);
    }

    public LinearLayout getCoordinatorLayout() {
        return parentLayout;
    }

    public ConstraintLayout getContentLayout() {
        return mContentLayout;
    }

    public void setContentBackground(int color) {
        mContentLayout.setBackgroundResource(color);
    }

    @Override
    public void setContentView(int layoutResID) {
        mContentLayout.removeAllViews();

        getLayoutInflater().inflate(layoutResID, mContentLayout, true);

        bindViews();
    }

    @Override
    public void setContentView(View view) {
        mContentLayout.removeAllViews();
        mContentLayout.addView(view);
        bindViews();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mContentLayout.addView(view, params);

        bindViews();
    }

    public ViewGroup getContentRoot() {
        return mContentLayout;
    }


    /**
     * 绑定控件
     */
    protected abstract void bindViews();

    ;

    /**
     * 设置全屏
     *
     * @return
     */
    protected boolean setFlagScreenOn() {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandler();
        AppManager.getInstance().finishActivity(this);
        ScreenManager.getScreenManager().popActivity(this);
    }


    public void removeHandler() {
        // 移除消息队列中所有消息并释放
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

}