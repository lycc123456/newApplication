package com.lycc1.mibox.miboxtest.framework.support.delegate.activity;


import com.lycc1.mibox.miboxtest.framework.base.presenter.MvpPresenter;
import com.lycc1.mibox.miboxtest.framework.base.view.MvpView;

public class ActivityMvpConfigurationInstance<V extends MvpView, P extends MvpPresenter<V>> {
	private P presenter;
	private Object customeConfigurationInstance;

	public ActivityMvpConfigurationInstance(P presenter,
			Object customeConfigurationInstance) {
		super();
		this.presenter = presenter;
		this.customeConfigurationInstance = customeConfigurationInstance;
	}

	public P getPresenter() {
		return presenter;
	}

	public void setPresenter(P presenter) {
		this.presenter = presenter;
	}

	public Object getCustomeConfigurationInstance() {
		return customeConfigurationInstance;
	}

	public void setCustomeConfigurationInstance(
			Object customeConfigurationInstance) {
		this.customeConfigurationInstance = customeConfigurationInstance;
	}

}
