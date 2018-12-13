package com.lycc1.mibox.miboxtest.base.presenter;


import android.app.Activity;
import com.lycc1.mibox.miboxtest.framework.base.presenter.impl.MvpBasePresenter;
import com.lycc1.mibox.miboxtest.framework.base.view.MvpView;

public abstract class BasePresenter<V extends MvpView> extends MvpBasePresenter<V> {

    public BasePresenter(Activity context) {
        super(context);
    }
}
