package com.lycc1.mibox.miboxtest.framework.base.presenter.impl;


import android.app.Activity;
import com.lycc1.mibox.miboxtest.framework.base.presenter.MvpPresenter;
import com.lycc1.mibox.miboxtest.framework.base.view.MvpView;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * 抽象类 统一管理View层绑定和解除绑定
 *
 * @param <V>
 * @author house
 */
public class MvpBasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private WeakReference<Activity> weakContext;
    private WeakReference<V> weakView;
    private V proxyView;


    public MvpBasePresenter(Activity context) {
        this.weakContext = new WeakReference<Activity>(context);
    }

    public Activity getContext() {
        if (null == weakContext){
            return null;
        }
        return weakContext.get();
    }


    public V getView() {
//        if (proxyView == null) {
//            if (weakContext == null) {
//                this.weakView = new WeakReference<V>((V)this);
//            }
//            initProxyView((V)this);
//        }
        return proxyView;
    }

    /**
     * 用于检查View是否为空对象
     *
     * @return
     */
    public boolean isAttachView() {
        if (this.weakView != null && this.weakView.get() != null) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void attachView(V view) {
        this.weakView = new WeakReference<V>(view);
        initProxyView(view);
    }

    private void initProxyView(V view){
        MvpViewInvocationHandler invocationHandler = new MvpViewInvocationHandler(
                this.weakView.get());
        // 在这里采用动态代理
        proxyView = (V) Proxy.newProxyInstance(
                view.getClass().getClassLoader(), view.getClass()
                        .getInterfaces(), invocationHandler);
    }

    @Override
    public void dettachView() {
        if (this.weakView != null) {
            this.weakView.clear();
            this.weakView = null;
            this.weakContext.clear();
            this.weakContext = null;
        }
    }

    private class MvpViewInvocationHandler implements InvocationHandler {

        private MvpView mvpView;

        public MvpViewInvocationHandler(MvpView mvpView) {
            this.mvpView = mvpView;
        }

        @Override
        public Object invoke(Object arg0, Method arg1, Object[] arg2)
                throws Throwable {
            try {
                if (isAttachView()) {
                    return arg1.invoke(mvpView, arg2);
                }
            } catch (UndeclaredThrowableException e) {
                e.getUndeclaredThrowable();
            }
            return null;
        }

    }

}
