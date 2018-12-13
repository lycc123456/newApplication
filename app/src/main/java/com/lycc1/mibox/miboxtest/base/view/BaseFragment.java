package com.lycc1.mibox.miboxtest.base.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.house.mebikeops.R;

public abstract class BaseFragment extends Fragment {
    //我们自己的Fragment需要缓存视图
    private View viewContent;//缓存视图
    private boolean isInit;

    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce = false;
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;

    private FrameLayout mContentLayout;

    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (viewContent == null) {
            viewContent = inflater.inflate(R.layout.fragment_base, null);
            mContentLayout = viewContent.findViewById(R.id.parentLayout);
            inflater.inflate(getContentView(), mContentLayout, true);
            initContentView(viewContent);
        }


        //判断Fragment对应的Activity是否存在这个视图
        ViewGroup parent = (ViewGroup) viewContent.getParent();
        if (parent != null) {
            //如果存在,那么我就干掉,重写添加,这样的方式我们就可以缓存视图
            parent.removeView(viewContent);
        }
        return viewContent;
    }

    protected abstract void onFragmentCreate(Bundle savedInstanceState);

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isInit) {
            this.isInit = true;
            bundle = getArguments();
            initContentView(viewContent);
            onFragmentCreate(savedInstanceState);
            initData();
        }
        isPrepared = true;
        lazyFetchData();

    }

    public Bundle getBundle() {
        return bundle;
    }

    public abstract int getContentView();

    public void initData() {
    }

    public abstract void initContentView(View contentView);

    public <T extends View> T findView(int viewId) {
        return (T) viewContent.findViewById(viewId);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            lazyFetchData();
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


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
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

    /**
     *  懒加载
     *  不考虑多次加载数据问题
     */
    protected void lazyFetchDataNoNet() {
        if (!isPrepared || !getUserVisibleHint() ) {
            return;
        }
        lazyLoad();
    }
    /**
     * 延迟加载 子类必须重写此方法
     */
    protected void lazyLoad() {

    }


    /**
     * 广播类型
     *
     * @return
     */
    protected String getMarqueeType() {
        return "1";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.gc();
    }
}
