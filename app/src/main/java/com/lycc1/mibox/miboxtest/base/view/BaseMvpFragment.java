package com.lycc1.mibox.miboxtest.base.view;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.house.mebikeops.R;
import com.house.mebikeops.base.presenter.BasePresenter;
import com.house.mebikeops.framework.base.view.MvpView;
import com.house.mebikeops.framework.support.delegate.view.MvpFragment;

public abstract class BaseMvpFragment<V extends MvpView, P extends BasePresenter<V>> extends
        MvpFragment<V, P> {
    //我们自己的Fragment需要缓存视图
    private View viewContent;//缓存视图
    private boolean isInit;

    private Handler mHandler;

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
            viewContent = inflater.inflate(R.layout.fragment_base, null);
            mContentLayout = viewContent.findViewById(R.id.parentLayout);
            inflater.inflate(getContentView(), mContentLayout, true);
            //initContentView(viewContent);
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

            initContentView(viewContent);
            onFragmentCreate( savedInstanceState);
            isPrepared = true;
            lazyFetchData();
            initData();
        }
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
        if (isVisibleToUser) {
            lazyFetchData();
        }
    }

    /**
     * 懒加载
     *  避免多次网络加载  加载数据
     */
    protected void lazyFetchData() {
        if (!isPrepared || !getUserVisibleHint() || mHasLoadedOnce) {
            return;
        }
        mHasLoadedOnce = true;
        lazyLoad();
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
