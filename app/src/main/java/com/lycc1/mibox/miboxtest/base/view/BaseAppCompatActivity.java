package com.lycc1.mibox.miboxtest.base.view;


import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.githang.statusbar.StatusBarCompat;

/**
 * Created by house on 2017/6/27.
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity {

    private LinearLayout parentLayout;
    private ConstraintLayout mContentLayout;

    private AppBarLayout titleBarLayout;
    private Toolbar toolbar;
    private TextView titleTextView;
    private TextView rightButton;
    private TextView leftButton;
    private RelativeLayout rlSearchToolbar;

    //搜索et
    private EditText etSearchToolbar;

    private ImageView etSearchToolbarBg;
    private ImageView rightImgButton;

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

        getDelegate().setContentView(R.layout.base_activity);

        setStatusBar(true);

        mContentLayout = findViewById(R.id.contentLayout);
        parentLayout = findViewById(R.id.parentLayout);

        initTitleBar();

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
     * 初始化TitleBar
     */
    private void initTitleBar() {
        //titleBarLayout = (AppBarLayout) findViewById(R.id.titleBarLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        rightButton = (TextView) findViewById(R.id.rightButton);
        leftButton = (TextView) findViewById(R.id.leftTextButton);
        rightImgButton = (ImageView) findViewById(R.id.rightImgButton);

        rlSearchToolbar = (RelativeLayout) findViewById(R.id.rl_search_toolbar);
        etSearchToolbar = (EditText) findViewById(R.id.et_search_toolbar);
        etSearchToolbarBg = (ImageView) findViewById(R.id.iv_toolbar_edittext_bg);



        if (!isVisibleTitleBar()) {
            toolbar.setVisibility(View.GONE);
            return;
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRightButtonClickListener(view);
            }
        });
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLeftButtonClickListener(view);
            }
        });
        rightImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRightImgButtonClickListener(view);
            }
        });

        etSearchToolbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setOnSearchChangedListener(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                setAfterSearchChangedListener(s.toString());
            }
        });

        etSearchToolbar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    setSearchClickListener();
                    return true;
                }
                return false;
            }
        });

        if (isSetBackClick()) {
            setLeftImageView(R.mipmap.back_icon);
        }

        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSetBackClick()) {
                    finish();
                } else {
                    setLeftButtonClick();
                }
            }
        });
    }


    protected void setLeftButtonClick() {
    }

    protected void setSearchToolbarInputType(int inputType){
        etSearchToolbar.setInputType(inputType);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    protected boolean isVisibleTitleBar() {
        return true;
    }

    /**
     * 设置Toolbar背景颜色
     *
     * @param resId
     */
    public void setTitleBackground(int resId) {
        toolbar.setBackgroundColor(ContextCompat.getColor(this, resId));
    }
    public void setSearchTextColoc(int resId) {
        etSearchToolbar.setTextColor(ContextCompat.getColor(this, resId));
    }
    public void setSearchHintColoc(int resId) {
        etSearchToolbar.setHintTextColor(ContextCompat.getColor(this, resId));
    }

    public void setSearchVisable(boolean visable) {
        rlSearchToolbar.setVisibility(visable ? View.VISIBLE : View.GONE);
    }

    public void setSearchBackground(int drawableId) {
        etSearchToolbarBg.setBackground(ContextCompat.getDrawable(this, drawableId));
    }

    public void setRightImgButton(int rightRes) {
        rightImgButton.setVisibility(View.VISIBLE);
        rightImgButton.setImageResource(rightRes);
        rightImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRightImgButtonClickListener(view);
            }
        });
    }

    /**
     * 设置最右侧按钮的点击事件
     *
     * @param view
     */
    public void setRightImgButtonClickListener(View view) {

    }


    public void setRightImgButtonVisable(boolean visable) {
        rightImgButton.setVisibility(visable ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置按钮颜色
     *
     * @param resId
     */
    public void setRightButtonTextColoc(int resId) {
        rightButton.setTextColor(ContextCompat.getColor(this, resId));
    }

    public void setRightButtonVisable(boolean visable) {
        rightButton.setVisibility(visable ? View.VISIBLE : View.GONE);
    }


    public void setSearchClickListener() {

    }

    /**
     * 设置中间title
     *
     * @param title
     */
    public void setTitle(String title) {
        titleTextView.setText(title);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
    }

    /**
     * 设置中间title
     *
     * @param titleId
     */
    public void setTitle(int titleId) {
        titleTextView.setText(getString(titleId));
        toolbar.setTitle("");
    }

    public void setLeftImageView(int resId) {
        toolbar.setNavigationIcon(resId);
    }

    public void setRightButtonBackground(int resId) {
        rightButton.setBackgroundResource(resId);
    }

    protected boolean isSetBackClick() {
        return true;
    }

    /**
     * 设置右侧第一个按钮的点击事件
     *
     * @param view
     */
    public void setRightLeftButtonClickListener(View view) {

    }

    /**
     * 设置最右侧按钮的点击事件
     *
     * @param view
     */
    public void setRightButtonClickListener(View view) {

    }

    /**
     * 设置最左侧按钮的点击事件
     *
     * @param view
     */
    public void setLeftButtonClickListener(View view) {

    }

    /**
     * 搜索监听
     */
    public void setOnSearchChangedListener(CharSequence s, int start, int before, int count) {
    }
    public void setAfterSearchChangedListener(String s) {
    }

    public void setRightButton(int rightRes) {
        rightButton.setText(getString(rightRes));
    }

    public void setRightButton(String rightText) {
        rightButton.setText(rightText);
    }


    public void setLeftButton(int rightRes) {
        leftButton.setVisibility(View.VISIBLE);
        leftButton.setText(getString(rightRes));
    }

    public void setLeftButton(String rightText) {
        leftButton.setVisibility(View.VISIBLE);
        leftButton.setText(rightText);
    }

    public void setLeftButtonVisable(boolean isVisable) {
        leftButton.setVisibility(isVisable ? View.VISIBLE : View.GONE);
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