package com.lycc1.mibox.miboxtest.framework.support.delegate.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import com.house.mebikeops.framework.base.presenter.MvpPresenter;
import com.house.mebikeops.framework.base.view.MvpView;
import com.house.mebikeops.framework.support.delegate.MvpDelegateCallbackProxy;

/**
 * 具体的目标接口实现类
 * 
 * @author house
 *
 * @param <V>
 * @param <P>
 */
public class FragmentMvpDelegateImpl<V extends MvpView, P extends MvpPresenter<V>>
		implements FragmentMvpDelegate<V, P> {

	private MvpDelegateCallbackProxy<V, P> proxy;
	// 具体的目标接口实现类，需要持有创建Mvp实例
	private FragmentMvpDelegateCallback<V, P> delegateCallback;

	public FragmentMvpDelegateImpl(
			FragmentMvpDelegateCallback<V, P> delegateCallback) {
		if (delegateCallback == null) {
			throw new NullPointerException("delegateCallback is not null!");
		}
		this.delegateCallback = delegateCallback;
	}
	
	private MvpDelegateCallbackProxy<V, P> getDelegateProxy() {
		if (this.proxy == null) {
			this.proxy = new MvpDelegateCallbackProxy<V, P>(delegateCallback);
		}
		return this.proxy;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		getDelegateProxy().createPresenter();
		getDelegateProxy().attachView();
	}

	@Override
	public void onStart() {

	}

	@Override
	public void onPause() {

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStop() {

	}

	@Override
	public void onDestroyView() {
		//TODO 暂时注释掉，切换Fragment的时候会停止加载?
		//getDelegateProxy().detachView();
	}

	@Override
	public void onDestroy() {
		getDelegateProxy().detachView();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

	}

	@Override
	public void onAttach(Context context) {

	}

	@Override
	public void onDetach() {

	}

	@Override
	public void setUserVisibleHint (boolean isVisibleToUser) {
	}

	@Override
	public void onHiddenChanged (boolean hidden) {

	}


}
