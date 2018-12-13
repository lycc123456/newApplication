package com.lycc1.mibox.miboxtest.framework.support.delegate.fragment;


import com.house.mebikeops.framework.base.presenter.MvpPresenter;
import com.house.mebikeops.framework.base.view.MvpView;
import com.house.mebikeops.framework.support.delegate.MvpDelegateCallback;

/**
 * 扩展目标接口 针对不同的模块，目标接口有差异
 * 
 * @author house
 *
 */
public interface FragmentMvpDelegateCallback<V extends MvpView, P extends MvpPresenter<V>>
		extends MvpDelegateCallback<V, P> {
	
}
