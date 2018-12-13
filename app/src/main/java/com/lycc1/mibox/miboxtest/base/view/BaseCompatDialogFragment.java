package com.lycc1.mibox.miboxtest.base.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.*;
import com.house.mebikeops.common.CommData;
import com.house.mebikeops.util.DensityUtil;


/**
 * Created by house on 2017/8/31.
 */

public abstract class BaseCompatDialogFragment extends AppCompatDialogFragment {

    private View contentView;
    private Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = LayoutInflater.from(getActivity()).inflate(setContentView(), null);
        return contentView;
    }

    protected abstract int setContentView();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setCancelable(cancelable());
        // 设置dialog宽度
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        if (getWidth() == 0) {
            if (CommData.getScreen() != null) {
                params.width = CommData.getScreen()[0] - DensityUtil.dip2px(getContext(), 20);
            }
        } else {
            params.width = getWidth();
        }

//        if (getTop() > 0) {
//            window.setGravity(Gravity.TOP);
//            params.y = (int) (getTop() + getContext().getResources().getDimension(R.dimen.toolBarHeight));
//        }

        window.setAttributes(params);

        bundle = getArguments();
        bindView();
    }

    protected boolean cancelable() {
        return true;
    }

    protected int getTop() {
        return 0;
    }

    protected int getWidth() {
        return 0;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public View getContentView() {
        return contentView;
    }

    protected abstract void bindView();

    @Override
    public void show(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void dismiss() {
        dismissAllowingStateLoss();
    }

}
