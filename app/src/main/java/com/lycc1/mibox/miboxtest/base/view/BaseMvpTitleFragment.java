package com.lycc1.mibox.miboxtest.base.view;


import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.house.mebikeops.R;
import com.house.mebikeops.base.presenter.BasePresenter;
import com.house.mebikeops.framework.base.view.MvpView;
import com.house.mebikeops.framework.support.delegate.view.MvpFragment;

public abstract class BaseMvpTitleFragment<V extends MvpView, P extends BasePresenter<V>> extends
        MvpFragment<V, P> {
    //我们自己的Fragment需要缓存视图
    private View viewContent;//缓存视图
    private boolean isInit;

    private Handler mHandler;
    private AppBarLayout titleBarLayout;
    private Toolbar toolbar;
    private TextView titleTextView;
    private TextView rightButton;
    private TextView leftButton;

    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce = false;
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;
    private FrameLayout mContentLayout;

    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (viewContent == null) {
            bundle = getArguments();
            viewContent = inflater.inflate(R.layout.fragment_base_title, null);
            mContentLayout = viewContent.findViewById(R.id.parentLayout);
            inflater.inflate(getContentView(), mContentLayout, true);
            //initContentView(viewContent);
            isPrepared = true;
            lazyLoad();
        }

        //判断Fragment对应的Activity是否存在这个视图
        ViewGroup parent = (ViewGroup) viewContent.getParent();
        if (parent != null) {
            //如果存在,那么我就干掉,重写添加,这样的方式我们就可以缓存视图
            parent.removeView(viewContent);
        }
        return viewContent;
    }

    public Handler getHandler(){
        if (mHandler == null) {
            mHandler = new Handler();
        }
        return mHandler;
    }

    protected abstract void onFragmentCreate(Bundle savedInstanceState);

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isInit) {
            this.isInit = true;
            initTitleBar(viewContent);
            initContentView(viewContent);
            onFragmentCreate( savedInstanceState);
            initData();
        }
    }

    /**
     * 初始化TitleBar
     */
    private void initTitleBar(View view) {
        //titleBarLayout = (AppBarLayout) findViewById(R.id.titleBarLayout);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        rightButton = (TextView) view.findViewById(R.id.rightButton);
        leftButton = (TextView) view.findViewById(R.id.leftTextButton);

        if (!isVisibleTitleBar()) {
            toolbar.setVisibility(View.GONE);
            return;
        }
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
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
        if (isSetBackClick()) {
            setLeftImageView(R.mipmap.back_icon);
        }

        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSetBackClick()) {
                    getActivity().finish();
                } else {
                    setLeftButtonClick();
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    protected void setLeftButtonClick() {
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
        toolbar.setBackgroundColor(ContextCompat.getColor(getContext(), resId));
    }

    /**
     * 设置按钮颜色
     *
     * @param resId
     */
    public void setRightButtonTextColoc(int resId) {
        rightButton.setTextColor(ContextCompat.getColor(getContext(), resId));
    }

    public void setRightButtonVisable(boolean visable) {
        rightButton.setVisibility(visable ? View.VISIBLE : View.GONE);
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

    public Bundle getBundle() {
        return bundle;
    }

    public abstract int getContentView();

    public void initData() {

    }

    public abstract void initContentView(View contentView);


    public <T extends View> T findView(int viewId) {
        return (T) mContentLayout.findViewById(viewId);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            resume();
        } else {
            pause();
        }
    }

    protected void resume() {

    }

    protected void pause() {
    }

    /**
     * 可见
     */
    protected void onVisible() {
    }

    /**
     * 不可见
     */
    protected void onInvisible() {
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 延迟加载 子类必须重写此方法
     */
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        removeHandler();
    }

    public void removeHandler(){
        // 移除消息队列中所有消息并释放
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
