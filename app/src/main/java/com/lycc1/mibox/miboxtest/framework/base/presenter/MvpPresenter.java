package com.lycc1.mibox.miboxtest.framework.base.presenter;


import com.lycc1.mibox.miboxtest.framework.base.view.MvpView;

/**
 * 抽象为接口
 * 
 * @author house
 *
 */
public interface MvpPresenter<V extends MvpView> {

	/**
	 * 绑定视图
	 * 
	 * @param view
	 */
	public void attachView(V view);

	/**
	 * 解除绑定
	 */
	public void dettachView();
	
}
