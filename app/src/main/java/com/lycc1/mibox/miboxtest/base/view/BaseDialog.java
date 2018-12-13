package com.lycc1.mibox.miboxtest.base.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import com.house.mebikeops.R;
import com.house.mebikeops.callback.DialogCallback;


/**
 * Dialog父类
 * Created by house on 2017/6/28.
 */

public abstract class BaseDialog extends AppCompatDialogFragment {

    public Bundle bundle;

    private DialogCallback dialogCallback;

    private View contentView;

    private AppCompatDialog dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new AppCompatDialog(getActivity(), R.style.waitDialog);
        bundle = getArguments();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        contentView = LayoutInflater.from(getActivity()).inflate(contentViewId(),null);
        dialog.setContentView(contentView);

        bindView();

        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public Dialog getDialog(){
        return dialog;
    }

    public <T extends View> T findView(int resId){
        return contentView.findViewById(resId);
    }

    protected abstract int contentViewId();

    protected abstract void bindView();

    public DialogCallback getDialogCallback() {
        return dialogCallback;
    }

    public void setDialogCallback(DialogCallback dialogCallback) {
        this.dialogCallback = dialogCallback;
    }
}
