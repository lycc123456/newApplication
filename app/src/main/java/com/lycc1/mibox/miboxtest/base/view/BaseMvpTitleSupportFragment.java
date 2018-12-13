package com.lycc1.mibox.miboxtest.base.view;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.house.mebikeops.R;
import com.house.mebikeops.base.presenter.BasePresenter;
import com.house.mebikeops.framework.base.view.MvpView;
import com.house.mebikeops.framework.support.delegate.view.MvpFragment;
import me.yokeyword.fragmentation.ExtraTransaction;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportFragmentDelegate;
import me.yokeyword.fragmentation.SupportHelper;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * 展示自定制的MySupportFragment，不继承SupportFragment
 */

public abstract class BaseMvpTitleSupportFragment<V extends MvpView, P extends BasePresenter<V>> extends
        MvpFragment<V, P> implements ISupportFragment {

    final SupportFragmentDelegate mDelegate = new SupportFragmentDelegate(this);
    protected FragmentActivity _mActivity;


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

    /**
     * 延迟加载 子类必须重写此方法
     */
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
    }

    public void removeHandler(){
        // 移除消息队列中所有消息并释放
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    @Override
    public SupportFragmentDelegate getSupportDelegate() {
        return mDelegate;
    }

    /**
     * Perform some extra transactions.
     * 额外的事务：自定义Tag，添加SharedElement动画，操作非回退栈Fragment
     */
    @Override
    public ExtraTransaction extraTransaction() {
        return mDelegate.extraTransaction();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDelegate.onAttach(activity);
        _mActivity = mDelegate.getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDelegate.onCreate(savedInstanceState);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return mDelegate.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDelegate.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mDelegate.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mDelegate.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mDelegate.onPause();
    }

    @Override
    public void onDestroyView() {
        mDelegate.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mDelegate.onDestroy();
        super.onDestroy();
        removeHandler();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mDelegate.onHiddenChanged(hidden);
        if (!hidden) {
            resume();
        } else {
            pause();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mDelegate.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * Causes the Runnable r to be added to the action queue.
     * <p>
     * The runnable will be run after all the previous action has been run.
     * <p>
     * 前面的事务全部执行后 执行该Action
     *
     * @deprecated Use {@link #post(Runnable)} instead.
     */
    @Deprecated
    @Override
    public void enqueueAction(Runnable runnable) {
        mDelegate.enqueueAction(runnable);
    }

    /**
     * Causes the Runnable r to be added to the action queue.
     * <p>
     * The runnable will be run after all the previous action has been run.
     * <p>
     * 前面的事务全部执行后 执行该Action
     */
    @Override
    public void post(Runnable runnable) {
        mDelegate.post(runnable);
    }

    /**
     * Called when the enter-animation end.
     * 入栈动画 结束时,回调
     */
    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        mDelegate.onEnterAnimationEnd(savedInstanceState);
    }


    /**
     * Lazy initial，Called when fragment is first called.
     * <p>
     * 同级下的 懒加载 ＋ ViewPager下的懒加载  的结合回调方法
     */
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        mDelegate.onLazyInitView(savedInstanceState);
    }

    /**
     * Called when the fragment is visible.
     * 当Fragment对用户可见时回调
     * <p>
     * Is the combination of  [onHiddenChanged() + onResume()/onPause() + setUserVisibleHint()]
     */
    @Override
    public void onSupportVisible() {
        mDelegate.onSupportVisible();
    }

    /**
     * Called when the fragment is invivible.
     * <p>
     * Is the combination of  [onHiddenChanged() + onResume()/onPause() + setUserVisibleHint()]
     */
    @Override
    public void onSupportInvisible() {
        mDelegate.onSupportInvisible();
    }

    /**
     * Return true if the fragment has been supportVisible.
     */
    @Override
    final public boolean isSupportVisible() {
        return mDelegate.isSupportVisible();
    }

    /**
     * Set fragment animation with a higher priority than the ISupportActivity
     * 设定当前Fragmemt动画,优先级比在SupportActivity里高
     */
    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return mDelegate.onCreateFragmentAnimator();
    }

    /**
     * 获取设置的全局动画 copy
     *
     * @return FragmentAnimator
     */
    @Override
    public FragmentAnimator getFragmentAnimator() {
        return mDelegate.getFragmentAnimator();
    }

    /**
     * 设置Fragment内的全局动画
     */
    @Override
    public void setFragmentAnimator(FragmentAnimator fragmentAnimator) {
        mDelegate.setFragmentAnimator(fragmentAnimator);
    }

    /**
     * 按返回键触发,前提是SupportActivity的onBackPressed()方法能被调用
     *
     * @return false则继续向上传递, true则消费掉该事件
     */
    @Override
    public boolean onBackPressedSupport() {
        return mDelegate.onBackPressedSupport();
    }

    /**
     * 类似 {@link Activity#setResult(int, Intent)}
     * <p>
     * Similar to {@link Activity#setResult(int, Intent)}
     *
     * @see #startForResult(ISupportFragment, int)
     */
    @Override
    public void setFragmentResult(int resultCode, Bundle bundle) {
        mDelegate.setFragmentResult(resultCode, bundle);
    }

    /**
     * 类似  {@link Activity#onActivityResult(int, int, Intent)}
     * <p>
     * Similar to {@link Activity#onActivityResult(int, int, Intent)}
     *
     * @see #startForResult(ISupportFragment, int)
     */
    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        mDelegate.onFragmentResult(requestCode, resultCode, data);
    }

    /**
     * 在start(TargetFragment,LaunchMode)时,启动模式为SingleTask/SingleTop, 回调TargetFragment的该方法
     * 类似 {@link Activity#onNewIntent(Intent)}
     * <p>
     * Similar to {@link Activity#onNewIntent(Intent)}
     *
     * @param args putNewBundle(Bundle newBundle)
     * @see #start(ISupportFragment, int)
     */
    @Override
    public void onNewBundle(Bundle args) {
        mDelegate.onNewBundle(args);
    }

    /**
     * 添加NewBundle,用于启动模式为SingleTask/SingleTop时
     *
     * @see #start(ISupportFragment, int)
     */
    @Override
    public void putNewBundle(Bundle newBundle) {
        mDelegate.putNewBundle(newBundle);
    }


    /****************************************以下为可选方法(Optional methods)******************************************************/
    // 自定制Support时，可移除不必要的方法

    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput() {
        mDelegate.hideSoftInput();
    }

    /**
     * 显示软键盘,调用该方法后,会在onPause时自动隐藏软键盘
     */
    protected void showSoftInput(final View view) {
        mDelegate.showSoftInput(view);
    }

    /**
     * 加载根Fragment, 即Activity内的第一个Fragment 或 Fragment内的第一个子Fragment
     *
     * @param containerId 容器id
     * @param toFragment  目标Fragment
     */
    public void loadRootFragment(int containerId, ISupportFragment toFragment) {
        mDelegate.loadRootFragment(containerId, toFragment);
    }

    public void loadRootFragment(int containerId, ISupportFragment toFragment, boolean addToBackStack, boolean allowAnim) {
        mDelegate.loadRootFragment(containerId, toFragment, addToBackStack, allowAnim);
    }

    public void start(ISupportFragment toFragment) {
        mDelegate.start(toFragment);
    }

    /**
     * @param launchMode Similar to Activity's LaunchMode.
     */
    public void start(final ISupportFragment toFragment, @LaunchMode int launchMode) {
        mDelegate.start(toFragment, launchMode);
    }

    /**
     * Launch an fragment for which you would like a result when it poped.
     */
    public void startForResult(ISupportFragment toFragment, int requestCode) {
        mDelegate.startForResult(toFragment, requestCode);
    }

    /**
     * Start the target Fragment and pop itself
     */
    public void startWithPop(ISupportFragment toFragment) {
        mDelegate.startWithPop(toFragment);
    }

    /**
     * @see #popTo(Class, boolean)
     * +
     * @see #start(ISupportFragment)
     */
    public void startWithPopTo(ISupportFragment toFragment, Class<?> targetFragmentClass, boolean includeTargetFragment) {
        mDelegate.startWithPopTo(toFragment, targetFragmentClass, includeTargetFragment);
    }

    public void replaceFragment(ISupportFragment toFragment, boolean addToBackStack) {
        mDelegate.replaceFragment(toFragment, addToBackStack);
    }

    public void pop() {
        mDelegate.pop();
    }

    /**
     * Pop the last fragment transition from the manager's fragment
     * back stack.
     * <p>
     * 出栈到目标fragment
     *
     * @param targetFragmentClass   目标fragment
     * @param includeTargetFragment 是否包含该fragment
     */
    public void popTo(Class<?> targetFragmentClass, boolean includeTargetFragment) {
        mDelegate.popTo(targetFragmentClass, includeTargetFragment);
    }

    /**
     * 获取栈内的fragment对象
     */
    public <T extends ISupportFragment> T findChildFragment(Class<T> fragmentClass) {
        return SupportHelper.findFragment(getChildFragmentManager(), fragmentClass);
    }
}
